package com.OrgComm.OrgComm.security;


import com.OrgComm.OrgComm.entity.Employee;
import com.OrgComm.OrgComm.entity.Organization;
import com.OrgComm.OrgComm.repository.EmployeeRepository;
import com.OrgComm.OrgComm.repository.OrganizationRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    // Repositories to fetch user information
    private final EmployeeRepository employeeRepository;
    private final OrganizationRepository organizationRepository;

    // Constructor injection of repositories
    public CustomUserDetailsService(
            EmployeeRepository employeeRepository,
            OrganizationRepository organizationRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.organizationRepository = organizationRepository;
    }

    // This method is called by Spring Security during authentication
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // First, try to find the user in the Employee repository
        Employee employee = employeeRepository.findByEusername(username);

        // If employee exists, create a UserDetails object for an employee
        if (employee != null) {
            return User
                    .withUsername(employee.getEusername())
                    .password(employee.getEpassword())
                    .roles("EMPLOYEE")
                    .build();
        }

        // If not an employee, try to find in Organization repository
        Organization organization = organizationRepository.findByOusername(username);

        // If organization exists, create a UserDetails object for an organization
        if (organization != null) {
            return User
                    .withUsername(organization.getOusername())
                    .password(organization.getOpassword())
                    .roles("ORGANIZATION")
                    .build();
        }

        // If no user found in either repository, throw an exception
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}