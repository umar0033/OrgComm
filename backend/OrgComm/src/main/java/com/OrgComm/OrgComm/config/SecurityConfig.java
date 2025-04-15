package com.OrgComm.OrgComm.config;


import com.OrgComm.OrgComm.security.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF as we're using JWT
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                    corsConfig.addAllowedOrigin("http://localhost:3000"); // Allow requests from React app
                    corsConfig.addAllowedMethod("*"); // Allow all HTTP methods (GET, POST, etc.)
                    corsConfig.addAllowedHeader("*"); // Allow all headers
                    corsConfig.addAllowedHeader("Authorization"); // Allow Authorization header
                    corsConfig.setAllowCredentials(true); // Allow credentials like cookies or Authorization headers
                    var source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
                    source.registerCorsConfiguration("/**", corsConfig); // Apply CORS settings to all endpoints
                    return corsConfig;
                }))
                .authorizeRequests(authz -> authz
                        .requestMatchers("/**").permitAll() // Allow unauthenticated access to authentication endpoints
                        .anyRequest().authenticated() // Require authentication for other requests
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless session for JWT
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // No encoding or hashing
    }
}
