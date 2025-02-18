package com.OrgComm.OrgComm.service;

import com.OrgComm.OrgComm.entity.Department;
import com.OrgComm.OrgComm.entity.Employee;
import com.OrgComm.OrgComm.entity.Organization;
import com.OrgComm.OrgComm.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private OrganizationService organizationService;
    @Autowired EmployeeService employeeService;

    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    public Department findById(Long id) {
        return departmentRepository.findByDid(id);
    }

    public boolean createDepartment(Department department) {
        if (departmentRepository.findByDname(department.getDname()) != null) {
            return false;
        }
        if (organizationService.findById(department.getOrganization().getOid()) == null) {
            return false;
        }

        // Safely retrieve the manager ID or set it to null
        Long managerId = department.getManagerid() != null ? department.getManagerid().getEid() : null;

        System.out.println("Manager ID: " + managerId); // Debugging log
        System.out.println(department.getDname() + " | " + department.getDcreatedat() + " | " + department.getOrganization().getOid());

        // Execute the query
        departmentRepository.save(department);
        return true;
    }


    public boolean updateDepartment(Department department) {
        if (departmentRepository.findByDid(department.getDid()) == null) {
            return false;
        }
        if (organizationService.findById(department.getOrganization().getOid()) == null) {
            return false;
        }

        // Safely retrieve the manager ID or set it to null
        Long managerId = department.getManagerid() != null ? department.getManagerid().getEid() : null;

        System.out.println("Manager ID: " + managerId); // Debugging log
        System.out.println(department.getDname() + " | " + department.getDcreatedat() + " | " + department.getOrganization().getOid());

        // Execute the query
        departmentRepository.save(department);
        return true;
    }

    public boolean deleteDepartment(Long did) {
        Department department = departmentRepository.findByDid(did);
        if (department == null) {
            return false;
        }
        departmentRepository.delete(department);
        return true;
    }


}
