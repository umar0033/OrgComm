package com.OrgComm.OrgComm.controller;


import com.OrgComm.OrgComm.dto.TasksDTO;
import com.OrgComm.OrgComm.entity.Employee;
import com.OrgComm.OrgComm.entity.TaskAssignments;
import com.OrgComm.OrgComm.entity.Tasks;
import com.OrgComm.OrgComm.security.JwtTokenUtil;
import com.OrgComm.OrgComm.service.EmployeeService;
import com.OrgComm.OrgComm.service.OrganizationService;
import com.OrgComm.OrgComm.service.TasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TasksController {

    @Autowired
    private TasksService tasksService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private EmployeeService employeeService;


    public List<Tasks> findAll() {
        return tasksService.findAll();
    }


    @GetMapping("/allTasks")
    public ResponseEntity<?> getAllTasksWithOid(
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Extract the token from the Authorization header
            String token = authorizationHeader.startsWith("Bearer ") ?
                    authorizationHeader.substring(7) : null;

            System.out.println("Token from Task controller: " + token);

            // Check if the token is null or invalid
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token missing or invalid");
            }

            // Extract username from the token
            String username = jwtTokenUtil.extractUsername(token);
            System.out.println("Username from Task controller: " + username);

            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }

            // Retrieve the organization ID (oid) based on the username
            Long oid = organizationService.findByUsername(username).getOid();
            System.out.println("Organization ID from Task controller: " + oid);

            // Retrieve tasks associated with the oid
            List<Tasks> tasks = tasksService.findTasksByOid(oid);
            if (tasks != null && !tasks.isEmpty()) {
                // Return the tasks if found
                return ResponseEntity.ok(tasks);
            } else {
                // Return a message if no tasks are found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No tasks found for the organization");
            }

        } catch (Exception e) {
            // Handle errors (e.g., invalid token, unexpected exceptions)
            System.err.println("Error in getAllTasksWithOid: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token or unauthorized access");
        }
    }


    @PostMapping("/create")
    public ResponseEntity<String> createTask(
            @RequestBody TasksDTO taskdto,
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Extract the token from the Authorization header
            String token = authorizationHeader.startsWith("Bearer ") ?
                    authorizationHeader.substring(7) : null;

            System.out.println("Token from Task controller: " + token);

            // Check if the token is null or invalid
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token missing or invalid");
            }

            // Extract username from the token
            String username = jwtTokenUtil.extractUsername(token);
            System.out.println("Username from Task controller: " + username);

            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }

            // Retrieve the organization ID (oid) based on the username
            Long oid = organizationService.findByUsername(username).getOid();
            System.out.println("Organization ID from Task controller: " + oid);

            String tname = taskdto.getTname();
            List<Long> assignedTo = taskdto.getAssignedTo();

            Tasks task= new Tasks();
            task.setTname(tname);
            task.setAssignedBy(employeeService.findByUsername(username));

            // Set the organization ID (oid) for the task
            task.setOrganization(organizationService.findById(oid));
            System.out.println("Task Organization from Task controller: " + task.getOrganization());

            TaskAssignments taskAssignments = new TaskAssignments();

            // make a loop of size equals to the size of assignedTo list
            // and assign the task to each employee
            for (Long id : assignedTo) {
                taskAssignments.setAssignedTo(employeeService.findById(id));
                task.getTaskAssignments().add(taskAssignments);
            }

            // Create the task
            if (tasksService.createTask(task)) {
                return ResponseEntity.ok("Task created successfully");
            }
            return ResponseEntity.badRequest().body("Task with name already exists");

        } catch (Exception e) {
            // Handle errors (e.g., invalid token, unexpected exceptions)
            System.err.println("Error in createTask: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token or unauthorized access");
        }
    }



}
