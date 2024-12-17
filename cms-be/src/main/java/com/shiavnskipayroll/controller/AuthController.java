package com.shiavnskipayroll.controller;
import com.shiavnskipayroll.configurations.UserStatusService;
import com.shiavnskipayroll.dto.request.LoginRequestDTO;
import com.shiavnskipayroll.dto.request.AdminRegisterRequestDTO;
import com.shiavnskipayroll.entity.Admin;
import com.shiavnskipayroll.exceptions.UserAlreadyExistsException;
import com.shiavnskipayroll.repository.UserRepository;
import com.shiavnskipayroll.utility.AccessTokenRequest;
import com.shiavnskipayroll.utility.KeycloakUtility;
import com.shiavnskipayroll.utility.RefreshTokenRequest;
import org.keycloak.TokenVerifier;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.util.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/auth")
public class AuthController {
	private final KeycloakUtility keycloakUtility;

	private final UserRepository userRepository;
	private final UserStatusService userStatusService;

	@Value("${keycloak.realm}")
	private String realm;

	@Value("${keycloak.client-id}")
	private String clientId;

	public AuthController(KeycloakUtility keycloakUtility, UserRepository userRepo, UserRepository userRepository,
			UserStatusService userStatusService) {
		this.keycloakUtility = keycloakUtility;
		this.userRepository = userRepository;
		this.userStatusService = userStatusService;

	}

	@PostMapping("/register")
	public ResponseEntity<String> registerUserdd(@RequestBody AdminRegisterRequestDTO userDTO) {

		UserRepresentation user = new UserRepresentation();
		user.setUsername(userDTO.getUsername());
		user.setEmail(userDTO.getEmail());
		user.setEnabled(true);

		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());

		CredentialRepresentation credential = new CredentialRepresentation();
		credential.setTemporary(false); // This sets if the password is temporary or not
		credential.setType(CredentialRepresentation.PASSWORD);
		credential.setValue(userDTO.getPassword());

		user.setCredentials(Collections.singletonList(credential));

		Keycloak keycloak = keycloakUtility.getKeycloakInstance();
		Response response = keycloak.realm(realm).users().create(user);

		if (response.getStatus() == 201) {

			String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

			saveUser(userDTO, userId);
			RoleRepresentation role = keycloak.realm(realm).roles().get(userDTO.getRole()).toRepresentation();
			keycloak.realm(realm).users().get(userId).roles().realmLevel().add(Collections.singletonList(role));
			return new ResponseEntity<>(HttpStatus.CREATED);
		} else {

			return ResponseEntity.status(response.getStatus())
					.body("Failed to create user. Status: " + response.getStatus());
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {

		Keycloak keycloakInstance = keycloakUtility.getKeycloakInstanceToken(loginRequestDTO);

		AccessTokenResponse token = keycloakInstance.tokenManager().grantToken();

		// Decode the access token to extract user ID (subject)
		String accessTokenString = token.getToken(); // Get the access token string
		AccessToken accessToken;
		try {
			accessToken = TokenVerifier.create(accessTokenString, AccessToken.class).getToken();
		} catch (VerificationException e) {
			// Handle exception if token verification fails
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
		}
		// ----------------------------------------------------------
		// Extract client-specific roles (replace "your-client-id" with actual client
		// ID)
		Set<String> clientRoles = accessToken.getResourceAccess(clientId) != null
				? accessToken.getResourceAccess(clientId).getRoles()
				: Collections.emptySet();
		// ----------------------------------------------------------------
		// Extract user ID from the access token
		String userId = accessToken.getSubject();
		// Update the user status to logged in
		userStatusService.logUserIn(userId);
		Map<String, Object> response = new HashMap<>();
		response.put("token", token);
		response.put("Role", clientRoles);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/refresh")
	public ResponseEntity<String> refreshToken(@RequestBody RefreshTokenRequest refreshToken) {
		// Call the utility method to refresh the token
		return new ResponseEntity(keycloakUtility.refreshAccessToken(refreshToken.getRefreshToken()), HttpStatus.OK);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestBody AccessTokenRequest token) {
		Keycloak keycloak = keycloakUtility.getKeycloakInstance();

		try {
			String refreshToken = token.getRefreshToken();

			String accessTokenString = token.getToken();

			// Decode the access token to extract user details
			AccessToken accessToken = TokenVerifier.create(accessTokenString, AccessToken.class).getToken();

			// Get the user ID and session ID from the access token
			String userId = accessToken.getSubject();

			// keycloakUtility.revokeAccessToken(accessTokenString);

			// Perform logout by terminating the user's session
			keycloak.realm(realm).users().get(userId).logout();

			// Log the user out in your service

			// userStatusService.logUserOut(userId);

			// Note: Refresh token invalidation can be managed by setting the refresh token
			// expiration in Keycloak's realm settings.
			return ResponseEntity.ok("Tokens invalidated and session terminated");

		} catch (VerificationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid access token: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(); // Log the stack trace for debugging
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error during logout: " + e.getMessage());
		}
	}

	public void saveUser(AdminRegisterRequestDTO userDTO, String keycloakId) {
		Admin admin = new Admin();
		admin.setKeycloakId(keycloakId);
		admin.setRole(userDTO.getRole());
		admin.setPassword(userDTO.getPassword());
		admin.setFirstName(userDTO.getFirstName());
		admin.setLastName(userDTO.getLastName());
		admin.setEmail(userDTO.getEmail());
		admin.setUsername(userDTO.getUsername());
		userRepository.save(admin);
		System.out.println("user save succesfullly");
	}

	public String registerUser(AdminRegisterRequestDTO userDTO) {
		System.out.println("keycloak in");
		UserRepresentation user = new UserRepresentation();
		user.setUsername(userDTO.getUsername());
		user.setEmail(userDTO.getEmail());
		user.setEnabled(true);
		System.out.println("nnjlnlns" + userDTO.getUsername());
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());

		CredentialRepresentation credential = new CredentialRepresentation();
		credential.setTemporary(false); // This sets if the password is temporary or not
		credential.setType(CredentialRepresentation.PASSWORD);
		credential.setValue(userDTO.getPassword());
		System.out.println("nnjlnlns" + userDTO.getPassword());
		user.setCredentials(Collections.singletonList(credential));

		Keycloak keycloak = keycloakUtility.getKeycloakInstance();

		Response response = keycloak.realm(realm).users().create(user);

		if (response.getStatus() != 201) {

			throw new UserAlreadyExistsException("email already exits in employee");
		}
		String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

		saveUser(userDTO, userId);
		RoleRepresentation role = keycloak.realm(realm).roles().get(userDTO.getRole()).toRepresentation();
		keycloak.realm(realm).users().get(userId).roles().realmLevel().add(Collections.singletonList(role));
		return userId;
	}

	public String deleteUser(String userId) {

		Keycloak keycloak = keycloakUtility.getKeycloakInstance();

		UserResource userResource = keycloak.realm(realm).users().get(userId);
		userResource.remove();
		keycloak.close();
		return "User deleted successfully.";

	}

	// -----------------------------------------------------------------------------------
	public String updateUser(String userId, AdminRegisterRequestDTO updateUserDTO) {
		Keycloak keycloak = keycloakUtility.getKeycloakInstance();

		UserResource userResource = keycloak.realm(realm).users().get(userId);
		UserRepresentation userRepresentation = userResource.toRepresentation();

		if (updateUserDTO.getFirstName() != null) {
			userRepresentation.setFirstName(updateUserDTO.getFirstName());
		}
		if (updateUserDTO.getLastName() != null) {
			userRepresentation.setLastName(updateUserDTO.getLastName());
		}
		if (updateUserDTO.getEmail() != null) {
			userRepresentation.setEmail(updateUserDTO.getEmail());
		}

		if (updateUserDTO.getPassword() != null && !updateUserDTO.getPassword().isEmpty()) {
			CredentialRepresentation credential = new CredentialRepresentation();
			credential.setTemporary(false);
			credential.setType(CredentialRepresentation.PASSWORD);
			credential.setValue(updateUserDTO.getPassword());
			userResource.resetPassword(credential);
		}

		if (updateUserDTO.getRole() != null) {

			List<RoleRepresentation> existingRoles = userResource.roles().realmLevel().listAll();
			for (RoleRepresentation existingRole : existingRoles) {
				userResource.roles().realmLevel().remove(Collections.singletonList(existingRole));
			}

			RoleRepresentation newRole = keycloak.realm(realm).roles().get(updateUserDTO.getRole()).toRepresentation();
			userResource.roles().realmLevel().add(Collections.singletonList(newRole)); // Add new role
		}

		userResource.update(userRepresentation);
		keycloak.close();
		return "User updated successfully";

	}
}
