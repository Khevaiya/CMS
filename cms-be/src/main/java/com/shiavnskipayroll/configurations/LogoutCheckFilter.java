package com.shiavnskipayroll.configurations;

import com.shiavnskipayroll.utility.KeycloakUtility;
import org.keycloak.TokenVerifier;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class LogoutCheckFilter extends OncePerRequestFilter {
	
	

    private final UserStatusService userStatusService;
    
    @Autowired
    private KeycloakUtility keycloakUtility;

    public LogoutCheckFilter(UserStatusService userStatusService) {
        this.userStatusService = userStatusService;
    }
	@Override
	protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
			jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain)
			throws jakarta.servlet.ServletException, IOException {
		
		String token = request.getHeader("Authorization");
      
      if (token != null && token.startsWith("Bearer ")) {
          // Extract the token
    	  
          String accessTokenString = token.substring(7); // Remove "Bearer " prefix
     
          // Decode the token to extract user ID (your decoding logic may vary)
          String userId=null;
		try {
		AccessToken	accessToken = TokenVerifier.create(accessTokenString, AccessToken.class).getToken();
	     userId = accessToken.getSubject();
	     
		} catch (VerificationException e) {
			e.printStackTrace();
		}
          
          // Check if the user is logged out
          if (!userStatusService.isUserLoggedIn(userId)) {
              // Respond with 401 Unauthorized if the user is logged out	
        	  response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User is logged out");
              return; // Stop processing further
          }
      }


   //  Proceed with the request if the user is logged in
  
      filterChain.doFilter(request, response);
		
		
	}
}