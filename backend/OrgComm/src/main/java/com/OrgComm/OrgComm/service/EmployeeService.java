package com.OrgComm.OrgComm.service;

import com.OrgComm.OrgComm.entity.Employee;
import com.OrgComm.OrgComm.entity.Organization;
import com.OrgComm.OrgComm.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import com.OrgComm.OrgComm.repository.EmployeeRepository;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee findById(Long id) {
        return employeeRepository.findByEid(id);
    }

    public Employee findByUsername(String eusername) {
        return employeeRepository.findByEusername(eusername);
    }

    public Employee createEmployee(Employee employee){
        Organization organization = organizationRepository.findByOid(employee.getOrganization().getOid());
        employee.setOrganization(organization);
        if(employeeRepository.findByEusername(employee.getEusername())!=null){
            return null;
        }
        employeeRepository.insertEmployee(employee.getEname(),employee.getEaddress(),employee.getEcreatedat(),employee.getEusername(),employee.getEpassword(),employee.getOrganization().getOid());
        return employee;
    }

    public Boolean Login(Employee employee){
        Employee employee1 = employeeRepository.findByEusername(employee.getEusername());
        if(employee1 == null){
            return false;
        }

        return employee1.getEpassword().equals(employee.getEpassword());
    }


    public boolean existsByUsername(String username) {
        return findByUsername(username) != null;
    }


    public List<Employee> findAllEmployeesByOid(Long oid) {

        List<Employee> temp= employeeRepository.findEmployeesByOid(oid);
        return temp;
    }

    public boolean deleteEmployee(Long eid){
        Employee employee = employeeRepository.findByEid(eid);
        if(employee == null){
            return false;
        }
        employeeRepository.delete(employee);
        return true;
    }


    // update employee
    public boolean updateEmployee(Employee employee) {
        // Find the existing employee by ID
        Employee existingEmployee = employeeRepository.findByEid(employee.getEid());
        System.out.println("employee from employee service : "+existingEmployee);
        if (existingEmployee == null) {
            return false; // Employee does not exist
        }

        // Update fields
        existingEmployee.setEname(employee.getEname());
        existingEmployee.setEaddress(employee.getEaddress());
        existingEmployee.setEpassword(employee.getEpassword());
        // Add more fields as necessary

        // Save the updated employee
        employeeRepository.save(existingEmployee);
        return true; // Update successful
    }

}
