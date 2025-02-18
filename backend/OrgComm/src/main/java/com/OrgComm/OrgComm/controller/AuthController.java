package com.OrgComm.OrgComm.controller;


import com.OrgComm.OrgComm.entity.Employee;
import com.OrgComm.OrgComm.entity.Organization;
import com.OrgComm.OrgComm.service.EmployeeService;
import com.OrgComm.OrgComm.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import com.OrgComm.OrgComm.security.JwtTokenUtil;

// REST Controller for authentication-related endpoints
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private EmployeeService employeeService;

    // Authentication Manager provided by Spring Security
    private final AuthenticationManager authenticationManager;

    // JWT Token utility for generating tokens
    private final JwtTokenUtil jwtTokenUtil;

    // Custom UserDetailsService to load user-specific data
    private final UserDetailsService userDetailsService;

    // Constructor injection of dependencies
    public AuthController(
            AuthenticationManager authenticationManager,
            JwtTokenUtil jwtTokenUtil,
            UserDetailsService userDetailsService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody LoginRequest loginRequest
    ) {
        try {
            // Attempt to authenticate the user
            // This uses the configured AuthenticationManager

            System.out.println(loginRequest.getUsername());
            System.out.println(loginRequest.getPassword());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            System.out.println("Authentication successful");

            // If authentication is successful, load user details
            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(loginRequest.getUsername());
            System.out.println(userDetails);
            // Determine user type based on granted authorities
            String userType = userDetails.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE"))
                    ? "EMPLOYEE" : "ORGANIZATION";

            // Generate JWT token
            final String token = jwtTokenUtil.generateToken(userDetails, userType);
            System.out.println("Token : " + token);


            // find organization based on username
            Organization organization = organizationService.findByUsername(loginRequest.getUsername());
            // Return successful authentication response
            return ResponseEntity.ok(new AuthenticationResponse(token, userType,loginRequest.getUsername(),organization));

        } catch (BadCredentialsException e) {
            // Handle invalid credentials
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid username or password"));
        } catch (Exception e) {
            // Handle other authentication errors
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Authentication failed"));
        }
    }

    // Registration endpoint for organizations
    @PostMapping("/register/organization")
    public ResponseEntity<?> registerOrganization(
            @RequestBody Organization registrationRequest
    ) {
        try {
            // Implement organization registration logic
            Organization newOrganization = organizationService.createOrganization(registrationRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(newOrganization);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Registration failed: " + e.getMessage()));
        }
    }

    // Registration endpoint for employees
    @PostMapping("/register/employee")
    public ResponseEntity<?> registerEmployee(
            @RequestBody Employee registrationRequest
    ) {
        try {
            // Implement employee registration logic
            Employee newEmployee = employeeService.createEmployee(registrationRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(newEmployee);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Registration failed: " + e.getMessage()));
        }
    }
}

// Data Transfer Object for Login Request
class LoginRequest {
    private String username;
    private String password;

    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

// Data Transfer Object for Authentication Response
class AuthenticationResponse {
    private String token;
    private String userType;
    private  String username;
    private Organization organization;

    // Constructor
    public AuthenticationResponse(String token, String userType,String username,Organization organization) {
        this.token = token;
        this.userType = userType;
        this.organization=organization;
        this.username=username;
    }

    // Getters
    public String getToken() { return token; }
    public String getUserType() { return userType; }
    public String getUsername() { return username; }
}

// Error Response DTO
class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
}
