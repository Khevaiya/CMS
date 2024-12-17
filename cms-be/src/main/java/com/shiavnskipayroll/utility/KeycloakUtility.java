package com.shiavnskipayroll.utility;

import com.shiavnskipayroll.dto.request.LoginRequestDTO;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class KeycloakUtility {

	private Keycloak keycloak;

	@Value("${keycloak.server-url}")
	private String serverUrl;

	@Value("${keycloak.realm}")
	private String realm;

	@Value("${keycloak.client-id}")
	private String clientId;

	@Value("${keycloak.client-secret}")
	private String clientSecret;

	@Value("${keycloak.grant-type}")
	private String grantType;

	public Keycloak getKeycloakInstance() {
		return KeycloakBuilder.builder().serverUrl(serverUrl).realm(realm).clientId(clientId).clientSecret(clientSecret)
				.grantType(grantType).build();
	}

	public Keycloak getKeycloakInstanceToken(LoginRequestDTO loginRequestDTO) {
		return KeycloakBuilder.builder().serverUrl(serverUrl).realm(realm).clientId(clientId).clientSecret(clientSecret)
				.grantType(OAuth2Constants.PASSWORD).username(loginRequestDTO.getUsername())
				.password(loginRequestDTO.getPassword()).build();
	}

	public ResponseEntity<?> refreshAccessToken(String refreshToken) {


		String refreshUrl = serverUrl + "/realms/" + realm + "/protocol/openid-connect/token";

		// Prepare request body for refreshing the token
		String body = "client_id=" + clientId + "&client_secret=" + clientSecret + "&refresh_token=" + refreshToken
				+ "&grant_type=" + OAuth2Constants.REFRESH_TOKEN;

		// Create an HttpClient
		HttpClient httpClient = HttpClient.newHttpClient();

		// Create an HttpRequest for refreshing the token
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(refreshUrl))
				.header("Content-Type", "application/x-www-form-urlencoded")
				.POST(HttpRequest.BodyPublishers.ofString(body)).build();

		try {
			// Send the request to refresh the token
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			// Check response status
			if (response.statusCode() == 200) {
				// Successful refresh
				System.out.println("Access token refreshed successfully.");
				System.out.println("Access token refreshed successfully." + response.body());
                
				
				// Return the new tokens as a response entity
				return ResponseEntity.ok(response.body());
			} else {
				// Handle error
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// Handle exception
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while refreshing the access token.");
		}
	}

	public void revokeAccessToken(String accessToken) {
		String revokeUrl = serverUrl + "/realms/" + realm + "/protocol/openid-connect/revoke";

		// Create the request body with token_type_hint
		String body = "client_id=" + clientId + "&client_secret=" + clientSecret + "&token=" + accessToken
				+ "&token_type_hint=access_token"; // Or refresh_token

		// Create an HttpClient
		HttpClient httpClient = HttpClient.newHttpClient();

		// Create an HttpRequest for revocation
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(revokeUrl))
				.header("Content-Type", "application/x-www-form-urlencoded")
				.POST(HttpRequest.BodyPublishers.ofString(body)).build();

		try {
			// Send the request to revoke the token
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			// Print response status and headers for debugging

			// If body is empty, mention that it's empty
			if (response.body().isEmpty()) {
				System.out.println("Response body is empty.");
			} else {
				System.out.println("Response body: " + response.body());
			}

			if (response.statusCode() == 204) {
				// Token successfully revoked
				return;

			} else {
				// Handle error in revocation
				System.err.println("Error during token revocation. Status code: " + response.statusCode());
				System.err.println("Error response: " + response.body());
			}
		} catch (Exception e) {
			e.printStackTrace();
			// Handle exception
		}
	}
//    public boolean checkSessionValidityInKeycloak(String sessionState) throws IOException, InterruptedException {
//        // Define the URL for the Keycloak Admin REST API to check user session
//        String sessionCheckUrl = serverUrl + "/admin/realms/" + realm + "/sessions/" + sessionState;
//
//        // Prepare the HTTP client
//        HttpClient client = HttpClient.newHttpClient();
//        String adminToken=getAdminToken(clientId,clientSecret);
//        
//
//        // Create the GET request to check the session
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(sessionCheckUrl))
//                .header("Authorization", "Bearer " + adminToken) // Use the admin token for authentication
//                .header("Content-Type", "application/json")
//                .GET()
//                .build();
//
//        try {
//            // Send the request to check session validity
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//            // Check the response status
//            if (response.statusCode() == 200) {
//                // If the response is 200 OK, the session is valid
//                System.out.println("Session is valid.");
//                return true; // Session is valid
//            } else if (response.statusCode() == 404) {
//                // If the response is 404, the session does not exist
//                System.out.println("Session not found, it might be invalid or logged out.");
//                return false; // Session is invalid
//            } else {
//                // Handle other status codes
//                System.err.println("Failed to check session validity. Status code: " + response.statusCode());
//                return false; // Assume session is invalid on error
//            }
//        } catch (Exception e) {
//            System.err.println("Error checking session validity: " + e.getMessage());
//            return false; // Assume session is invalid if an error occurs
//        }
//    }
//
//    public  String getAdminToken(String clientId, String clientSecret) throws IOException, InterruptedException {
//        HttpClient client = HttpClient.newHttpClient();
//        String TOKEN_URL = serverUrl+"/auth/realms/"+realm +"/protocol/openid-connect/token";
//     
//        // Prepare the request body
//        String requestBody = String.format("grant_type=client_credentials&client_id=%s&client_secret=%s",
//                URLEncoder.encode(clientId, StandardCharsets.UTF_8),
//                URLEncoder.encode(clientSecret, StandardCharsets.UTF_8));
//        
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(TOKEN_URL))
//                .header("Content-Type", "application/x-www-form-urlencoded")
//                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
//                .build();
//        
//        // Send the request and get the response
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//        // Check for successful response
//        if (response.statusCode() == 200) {
//            String responseBody = response.body();
//            System.out.println("Admin Token Response: " + responseBody);
//            return responseBody; // This will contain your access token in JSON format
//        } else {
//            System.err.println("Failed to obtain admin token. Status code: " + response.statusCode());
//            System.err.println("Response body: " + response.body());
//            return null;
//        }
//    }
//
//
//public boolean isUserSessionActive(AccessToken accessToken) throws IOException, InterruptedException {
//    String sessionState = accessToken.getSessionState();
//    
//    // Assume we have a method to check session validity in Keycloak
//    boolean isValidSession = checkSessionValidityInKeycloak(sessionState);
//
//    if (isValidSession) {
//        System.out.println("User session is active.");
//        return true; // Session is valid
//    } else {
//        System.out.println("User session is inactive or has been logged out.");
//        return false; // Session is invalid
//    }
//}

//    public boolean isTokenExpired(AccessToken accessToken) {
//    	
//    	
//    	
//        // Get the current time in seconds (because token expiration is in seconds)
//    	  long currentTimeInSeconds = System.currentTimeMillis() / 1000;
//        
//        // Get the token's expiration time in seconds
//        long expirationTimeInSeconds = accessToken.getExp();
//
//        // Log the token expiration time for debugging
//        System.out.println("Current time (in seconds): " + currentTimeInSeconds);
//        System.out.println("accessToken.getSessionState();"+accessToken.getSessionState()+"Token expiration time (in seconds): " + expirationTimeInSeconds);
//
//        // Check if the current time has passed the token's expiration time
//        if (expirationTimeInSeconds  < currentTimeInSeconds+300) {
//            System.out.println("Token has expired.");
//            return true; // Token is expired
//        } else {
//            System.out.println("Token is still valid.");
//            return false; // Token is valid
//        }
//    }

}
