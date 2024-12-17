package com.shiavnskipayroll.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
//@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private final JwtAuthConverter jwtAuthConverter;
    
    private final  UserStatusService userStatusService;

    public SecurityConfig(JwtAuthConverter jwtAuthConverter, UserStatusService userStatusService) {
        this.jwtAuthConverter = jwtAuthConverter;
        this.userStatusService=userStatusService;
    }
    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable) // Updated syntax for disabling CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**","/**").permitAll()
                        .requestMatchers(
                        		"/api/v1/timesheets/employee/{id}",
                        		"/api/v1/timesheetProject/create",
                        		"/api/v1/employee/getById/{id}",
                        		"/api/v1/project/getEmployeesOfProjectById/{Id}",
                        		"/api/v1/project/getEmployeeProjectsByEmployeeId/{Id}",
                        		"/api/v1/timesheetProject/employee/{employeeId}",
                        		"/api/v1/timesheetProject/getLoginUserMongoAutoUniqueIdIdByAccessToken")
                                
                        .hasRole("client_user")
                        .anyRequest().hasRole("client_admin")
                       
                ) 
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthConverter)
                        )
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }


    @Bean
    public JwtDecoder jwtDecoder() {
        // Use the issuer URL of your OAuth2 provider (e.g., Keycloak)
        String jwkSetUri = serverUrl+"/realms/"+realm+"/protocol/openid-connect/certs"; // Update with your JWKS URI
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }
    @Bean
    public LogoutCheckFilter logoutCheckFilter() {
        return new LogoutCheckFilter(userStatusService);
    }


}