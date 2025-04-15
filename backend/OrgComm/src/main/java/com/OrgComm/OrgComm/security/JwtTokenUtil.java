package com.OrgComm.OrgComm.security;

// Import necessary JWT and Spring libraries
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

// @Component allows Spring to automatically create an instance of this class
@Component
public class JwtTokenUtil {
    // @Value annotation reads values from application.properties
    // Secret key for signing the JWT
    @Value("${jwt.secret}")
    private String secret;

    // Token expiration time in milliseconds
    private final Long expiration = 86400000L;  // 24 hours in milliseconds

    // Generate token for a user
    public String generateToken(UserDetails userDetails, String userType) {
        // Create a map to store additional claims
        Map<String, Object> claims = new HashMap<>();
        // Add user type to claims (can be "EMPLOYEE" or "ORGANIZATION")
        claims.put("userType", userType);

        // Create and return the token
        return createToken(claims, userDetails.getUsername());
    }

    // Internal method to create the actual JWT token
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                // Add all custom claims
                .setClaims(claims)
                // Set the subject (usually username)
                .setSubject(subject)
                // Set the token creation time
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // Set token expiration time
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                // Sign the token with a secure key
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                // Convert to a compact, URL-safe string
                .compact();
    }

    // Create a secure signing key from the secret
    private Key getSigningKey() {
        // Convert secret to a secure HMAC-SHA key
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Extract username from the token
    public String extractUsername(String token) {
        // Use a method reference to get the subject (username)
        return extractClaim(token, Claims::getSubject);
    }

    // Extract user type from the token
    public String extractUserType(String token) {
        // Get the "userType" claim directly from the token
        return (String) extractAllClaims(token).get("userType");
    }

    // Extract token expiration date
    public Date extractExpiration(String token) {
        // Use a method reference to get the expiration
        return extractClaim(token, Claims::getExpiration);
    }

    // Generic method to extract any claim from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        // Extract all claims first
        final Claims claims = extractAllClaims(token);
        // Apply the specific claim extraction logic
        return claimsResolver.apply(claims);
    }

    // Parse and extract all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                // Use the signing key to verify the token
                .setSigningKey(getSigningKey())
                .build()
                // Parse the token
                .parseClaimsJws(token)
                // Get the body (payload) of the token
                .getBody();
    }

    // Check if the token is expired
    private Boolean isTokenExpired(String token) {
        // Compare expiration date with current time
        return extractExpiration(token).before(new Date());
    }

    // Validate the entire token
    public Boolean validateToken(String token, UserDetails userDetails) {
        // Extract username from token
        final String username = extractUsername(token);

        // Check if:
        // 1. Username matches the user details
        // 2. Token is not expired
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}