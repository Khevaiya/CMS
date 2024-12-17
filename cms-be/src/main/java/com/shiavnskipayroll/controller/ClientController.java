package com.shiavnskipayroll.controller;

import com.shiavnskipayroll.dto.request.ClientMasterRequestDTO;
import com.shiavnskipayroll.dto.response.ClientMasterResponseDTO;
import com.shiavnskipayroll.serviceimpl.ClientMasterServiceImpl;
import org.keycloak.TokenVerifier;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/client")
@CrossOrigin(origins = "http://localhost:5173")
public class ClientController {

	private final ClientMasterServiceImpl clientMasterServiceImpl;

	public ClientController(ClientMasterServiceImpl clientMasterServiceImpl) {
		this.clientMasterServiceImpl = clientMasterServiceImpl;

	}

	//@PreAuthorize("hasRole('client_admin')")
	@PostMapping("/create")
	public ResponseEntity<String> createClient(@ModelAttribute ClientMasterRequestDTO clientMasterRequestDTO,
			@RequestParam(value = "clientSowSigned", required = false) MultipartFile clientSowSigned) {
		try {
			System.out.println("createClient"+clientMasterRequestDTO.toString());
			clientMasterServiceImpl.createClient(clientMasterRequestDTO, clientSowSigned);
			return new ResponseEntity<>("Client created successfully", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>("Error creating client: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	//----------------------------30oct------------------------------------
	@GetMapping("/displayClientSowSignedById/{id}")
	public ResponseEntity<byte[]> getClientImageData(@PathVariable String id) {
		try {
			// Retrieve the image content (assuming it's PNG in this example)
			byte[] content = clientMasterServiceImpl.getAllFileDataOfClient(id);
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"clientImage.png\"")
					.header(HttpHeaders.CONTENT_TYPE, "image/png") // Predefined content type
					.body(content);
		} catch (Exception e) {
			throw new RuntimeException("Failed to get image in displayClientImageById", e);
		}
	}

	// --------------------------------------------------------------------------------------------------

	@GetMapping("/all")
	public ResponseEntity<List<ClientMasterResponseDTO>> getAllClients() {
		List<ClientMasterResponseDTO> clients = clientMasterServiceImpl.getAllClient();
		return new ResponseEntity<>(clients, HttpStatus.OK);
	}

	// --------------------------------------------------------------------------------------------------

	@GetMapping("/{id}")
	public ResponseEntity<ClientMasterResponseDTO> getClientById(@PathVariable String id) {
		ClientMasterResponseDTO client = clientMasterServiceImpl.getClientById(id);
		return new ResponseEntity<>(client, HttpStatus.OK);
	}

	// --------------------------------------------------------------------------------------------------

	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateClient(@PathVariable String id,
			@ModelAttribute ClientMasterRequestDTO clientMasterRequestDTO,
			@RequestParam(value = "clientSowSigned", required = false) MultipartFile clientSowSigned) {
		try {
			System.out.println("update client:" + clientMasterRequestDTO.toString());

			clientMasterServiceImpl.updateClient(id, clientMasterRequestDTO, clientSowSigned);
			return new ResponseEntity<>("Client updated successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error updating client: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// --------------------------------------------------------------------------------------------------

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteClient(@PathVariable String id) {
		try {

			clientMasterServiceImpl.deleteClient(id);
			return new ResponseEntity<>("Client deleted successfully", HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>("Error deleting client: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// --------------------------------------------------------------------------------------------------

	@GetMapping("/getUserByAccessToken")
	public ResponseEntity<String> getUserByAccessToken(@RequestHeader("Authorization") String token) {
		System.out.println("getUserByAccessToken: " + token);

		// Extract the actual token, removing "Bearer " if it's present
		String accessTokenString = token.startsWith("Bearer ") ? token.substring(7) : token;

		try {
			// Log the raw token for debugging
			System.out.println("Raw access token: " + accessTokenString);

			// Decode the access token to extract user details
			AccessToken accessToken = TokenVerifier.create(accessTokenString, AccessToken.class).getToken();

			// Get user details from the token
			String username = accessToken.getPreferredUsername(); // User's username

			return ResponseEntity.ok(username);
		} catch (VerificationException e) {
			System.err.println("Token verification failed: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid access token: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Error retrieving user information: " + e.getMessage());
			e.printStackTrace(); // Log the stack trace for debugging
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error retrieving user information: " + e.getMessage());
		}

	}

}
