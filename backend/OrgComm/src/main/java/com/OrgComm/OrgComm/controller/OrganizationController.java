package com.OrgComm.OrgComm.controller;

import com.OrgComm.OrgComm.dto.OrganizationDTO;
import com.OrgComm.OrgComm.entity.Organization;
import com.OrgComm.OrgComm.security.JwtTokenUtil;
import com.OrgComm.OrgComm.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @GetMapping("/organizations")
    public List<Organization> getAllOrganizations() {
        return organizationService.findAll();
    }

    @PostMapping("/organizations")
    public ResponseEntity<String> createOrganization(@RequestBody Organization organization) {
        Organization savedOrganization = organizationService.createOrganization(organization);
        return ResponseEntity.status(200).body("Orgainzation created successfully");
    }

    @GetMapping("/organizations/{id}")
    public ResponseEntity<Organization> getOrganizationById(@PathVariable Long id) {
        Organization organization = organizationService.findById(id);
        return ResponseEntity.ok(organization);
    }

    @GetMapping("/organization/byname")
    @PreAuthorize("hasRole('ROLE_ORGANIZATION')") // Adjust this role as per your application
    public ResponseEntity<Organization> getOrganizationByName(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        System.out.println("Umar");
        // Extract token from "Bearer token"
        String token = authorizationHeader.startsWith("Bearer ") ?
                authorizationHeader.substring(7) : null;

        System.out.println("Token from org controller: " + token);

        if (token == null) {
            return ResponseEntity.status(401).body(null); // Unauthorized if token is missing or invalid
        }

        // Extract username from token (you can modify this based on your JWT structure)
        String username = jwtTokenUtil.extractUsername(token);
        System.out.println("Username from org controller: " + username);
        if (username == null) {
            return ResponseEntity.status(401).body(null); // Unauthorized if token is invalid
        }

        // You can now find the organization by its name, using the username or directly using the name
        Organization organization = organizationService.findByUsername(username);
        System.out.println("Organization from org controller: " + organization);
        if (organization == null) {
            return ResponseEntity.status(404).body(null); // Not found if organization is not found
        }

        return ResponseEntity.ok(organization);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody OrganizationDTO organizationDTO){
        boolean isAuthenticated = organizationService.login(organizationDTO);
        if(isAuthenticated){
            return ResponseEntity.status(200).body("Login successful");
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @PostMapping("/organizations/channels")
    public ResponseEntity<String> createAllChannelsForDepartments(@RequestBody Organization organization){
        organizationService.createAllChannelsForDepartments(organization);
        return ResponseEntity.status(200).body("Channels created successfully");
    }

}
