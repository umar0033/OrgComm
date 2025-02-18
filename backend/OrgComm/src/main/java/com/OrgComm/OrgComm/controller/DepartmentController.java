package com.OrgComm.OrgComm.controller;

import com.OrgComm.OrgComm.entity.Employee;
import com.OrgComm.OrgComm.repository.OrganizationRepository;
import com.OrgComm.OrgComm.entity.Department;
import com.OrgComm.OrgComm.entity.Organization;
import com.OrgComm.OrgComm.security.JwtTokenUtil;
import com.OrgComm.OrgComm.service.DepartmentService;
import com.OrgComm.OrgComm.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private OrganizationService organizationService;

    @GetMapping("/all")
    public List<Department> findAll() {
        return departmentService.findAll();
    }

    @GetMapping("/find")
    public Department findById(Long id) {
        return departmentService.findById(id);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ORGANIZATION')")
    public ResponseEntity<String> createDepartment(@RequestHeader("Authorization") String authorizationHeader,@RequestBody Department department) {
        String token = authorizationHeader.startsWith("Bearer ") ?
                authorizationHeader.substring(7) : null;

        System.out.println("Token from department controller: " + token);

        if (token == null) {
            return ResponseEntity.status(401).body(null); // Unauthorized if token is missing or invalid
        }

        // Extract username from token (you can modify this based on your JWT structure)
        String username = jwtTokenUtil.extractUsername(token);
        System.out.println("Username from department controller: " + username);
        if (username == null) {
            return ResponseEntity.status(401).body(null); // Unauthorized if token is invalid
        }
        Long oid = organizationService.findByUsername(username).getOid();
        department.setOrganization(organizationService.findById(oid));

        // get manager

        Employee manager= department.getManagerid();
        System.out.println("Manager from department controller:  " + manager);
        department.setManagerid(manager);

        if(departmentService.createDepartment(department)){
            return ResponseEntity.ok("Department created successfully");
        }
        return ResponseEntity.badRequest().body("Department with name already exists");
    }

    @PostMapping("/updateDepartment")
    public ResponseEntity<String> updateDepartment(
            @RequestBody Department department,
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.startsWith("Bearer ") ?
                    authorizationHeader.substring(7) : null;

            System.out.println("Token from department controller: " + token);

            if (token == null) {
                return ResponseEntity.status(401).body(null); // Unauthorized if token is missing or invalid
            }

            // Extract username from token (you can modify this based on your JWT structure)
            String username = jwtTokenUtil.extractUsername(token);
            System.out.println("Username from department controller: " + username);
            if (username == null) {
                return ResponseEntity.status(401).body(null); // Unauthorized if token is invalid
            }


            Organization organization = organizationService.findByUsername(username);
            department.setOrganization(organization);

            System.out.println("Department Manager : " + department.getManagerid().getEid());

            // Now create the employee
            if (departmentService.updateDepartment(department)) {
                return ResponseEntity.ok("Department updated successfully");
            }
            return ResponseEntity.badRequest().body("department not updated");

        } catch (Exception e) {
            // If there’s an error, such as an invalid token, return an error response
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token or unauthorized access");
        }
    }


    @DeleteMapping("/deleteDepartment")
    public ResponseEntity<?> deleteDepartmentById(
            @RequestBody Department department,
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Extract the token from the Authorization header
            String token = authorizationHeader.startsWith("Bearer ") ?
                    authorizationHeader.substring(7) : null;

            System.out.println("Token from department controller: " + token);

            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Missing or invalid token");
            }

            // Extract username from token
            String username = jwtTokenUtil.extractUsername(token);
            System.out.println("Username from department controller: " + username);

            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid token");
            }

            // Retrieve the organization linked to the username
            Organization organization = organizationService.findByUsername(username);
            System.out.println("Organization from department controller: " + organization);
            Long oid=organization.getOid();
            if (organization == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Organization not found for username: " + username);
            }


            if (department == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found with ID: " + department.getDid());
            }
            department.setOrganization(organization);
            // Check if the employee belongs to the organization
            if (!department.getOrganization().getOid().equals(oid)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this department");
            }

            // Delete the employee
            departmentService.deleteDepartment(department.getDid());
            return ResponseEntity.ok("Department with ID " + department.getDid() + " deleted successfully");

        } catch (Exception e) {
            // Handle exceptions and return appropriate error message
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error occurred: " + e.getMessage());
        }
    }

}
