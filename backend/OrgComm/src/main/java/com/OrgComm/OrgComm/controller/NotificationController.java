package com.OrgComm.OrgComm.controller;


import com.OrgComm.OrgComm.entity.Notification;
import com.OrgComm.OrgComm.security.JwtTokenUtil;
import com.OrgComm.OrgComm.service.NotificationService;
import com.OrgComm.OrgComm.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private OrganizationService organizationService;

    @PostMapping("/sendNotification")
    @PreAuthorize("hasRole('ROLE_ORGANIZATION')")
    public ResponseEntity<String> sendNotification(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Notification notification) {
        try {
            // Extract the token from the Authorization header
            String token = authorizationHeader.startsWith("Bearer ") ?
                    authorizationHeader.substring(7) : null;

            System.out.println("Token from Notification controller: " + token);

            // Check if the token is null or invalid
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token missing or invalid");
            }

            // Extract username from the token
            String username = jwtTokenUtil.extractUsername(token);
            System.out.println("Username from Notification controller: " + username);

            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }

            // Retrieve the organization ID (oid) based on the username
            Long oid = organizationService.findByUsername(username).getOid();
            System.out.println("Organization ID from Notification controller: " + oid);

            notification.setOrganization(organizationService.findById(oid));
            if(notificationService.sendNotification(notification)) {
                return ResponseEntity.status(HttpStatus.OK).body("Notification sent successfully");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send notification");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send notification due to exception");
        }
    }
}
