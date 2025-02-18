package com.OrgComm.OrgComm.controller;

import com.OrgComm.OrgComm.dto.EmployeeDTO;
import com.OrgComm.OrgComm.entity.Employee;
import com.OrgComm.OrgComm.entity.Organization;
import com.OrgComm.OrgComm.security.JwtTokenUtil;
import com.OrgComm.OrgComm.service.EmployeeService;
import com.OrgComm.OrgComm.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private OrganizationService organizationService;

    @PostMapping("/create")
    public ResponseEntity<String> createEmployee(
                                                 @RequestBody Employee employee,
                                                 @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.startsWith("Bearer ") ?
                    authorizationHeader.substring(7) : null;

            System.out.println("Token from employee controller: " + token);

            if (token == null) {
                return ResponseEntity.status(401).body(null); // Unauthorized if token is missing or invalid
            }

            // Extract username from token (you can modify this based on your JWT structure)
            String username = jwtTokenUtil.extractUsername(token);
            System.out.println("Username from employee controller: " + username);
            if (username == null) {
                return ResponseEntity.status(401).body(null); // Unauthorized if token is invalid
            }


            Organization organization = organizationService.findByUsername(username);
            employee.setOrganization(organization);

            // Now create the employee
            if (employeeService.createEmployee(employee) != null) {
                return ResponseEntity.ok("Employee created successfully");
            }
            return ResponseEntity.badRequest().body("Employee with username already exists");

        } catch (Exception e) {
            // If there’s an error, such as an invalid token, return an error response
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token or unauthorized access");
        }
    }


    @GetMapping("/getAllEmployees")
    public ResponseEntity<?> getEmployeesByOrganization(
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Extract the token from the Authorization header
            String token = authorizationHeader.startsWith("Bearer ") ?
                    authorizationHeader.substring(7) : null;

            System.out.println("Token from employee controller: " + token);

            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Missing or invalid token");
            }

            // Extract username from token
            String username = jwtTokenUtil.extractUsername(token);
            System.out.println("Username from employee controller: " + username);

            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid token");
            }

            // Retrieve the organization linked to the username
            Organization organization = organizationService.findByUsername(username);
            if (organization == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Organization not found for username: " + username);
            }

            // Fetch employees belonging to the organization
            List<Employee> employees = employeeService.findAllEmployeesByOid(organization.getOid());


            if (employees.isEmpty()) {
                return ResponseEntity.ok("No employees found for the organization");
            }

            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            // Handle exceptions and return appropriate error message
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error occurred: " + e.getMessage());
        }
    }


    @DeleteMapping("/deleteEmployee")
    public ResponseEntity<?> deleteEmployeeById(
            @RequestBody Employee employee,
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Extract the token from the Authorization header
            String token = authorizationHeader.startsWith("Bearer ") ?
                    authorizationHeader.substring(7) : null;

            System.out.println("Token from employee controller: " + token);

            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Missing or invalid token");
            }

            // Extract username from token
            String username = jwtTokenUtil.extractUsername(token);
            System.out.println("Username from employee controller: " + username);

            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid token");
            }

            // Retrieve the organization linked to the username
            Organization organization = organizationService.findByUsername(username);
            System.out.println("Organization from employee controller: " + organization);
            Long oid=organization.getOid();
            if (organization == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Organization not found for username: " + username);
            }


            if (employee == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found with ID: " + employee.getEid());
            }
            employee.setOrganization(organization);
            // Check if the employee belongs to the organization
            if (!employee.getOrganization().getOid().equals(oid)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this employee");
            }

            // Delete the employee
            employeeService.deleteEmployee(employee.getEid());
            return ResponseEntity.ok("Employee with ID " + employee.getEid() + " deleted successfully");

        } catch (Exception e) {
            // Handle exceptions and return appropriate error message
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/updateEmployee")
    public ResponseEntity<String> updateEmployee(
            @RequestBody Employee employee,
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.startsWith("Bearer ") ?
                    authorizationHeader.substring(7) : null;

            System.out.println("Token from employee controller: " + token);

            if (token == null) {
                return ResponseEntity.status(401).body(null); // Unauthorized if token is missing or invalid
            }

            // Extract username from token (you can modify this based on your JWT structure)
            String username = jwtTokenUtil.extractUsername(token);
            System.out.println("Username from employee controller: " + username);
            if (username == null) {
                return ResponseEntity.status(401).body(null); // Unauthorized if token is invalid
            }


            Organization organization = organizationService.findByUsername(username);
            employee.setOrganization(organization);

            System.out.println("Organziation from employee controller:  ");

            // Now create the employee
            if (employeeService.updateEmployee(employee)) {
                return ResponseEntity.ok("Employee updated successfully");
            }
            return ResponseEntity.badRequest().body("Employee not updated");

        } catch (Exception e) {
            // If there’s an error, such as an invalid token, return an error response
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token or unauthorized access");
        }
    }



}
