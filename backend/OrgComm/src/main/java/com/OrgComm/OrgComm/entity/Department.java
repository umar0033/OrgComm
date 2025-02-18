package com.OrgComm.OrgComm.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long did;

    @Column(unique=true,nullable = false)
    private String dname;

    private String dcreatedat;

    @OneToOne
    @JoinColumn(name = "manager_id", unique = true)
    private Employee managerid;

    @ManyToOne
    @JoinColumn(name="parent_department_id")
    private Department parentDepartment;

    @OneToMany(mappedBy = "parentDepartment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Department> subDepartments;

    @ManyToOne
    @JoinColumn(name = "oid",nullable = false)
    @JsonIgnore
    private Organization organization;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employee> employees;

    public Department() {
        this.dcreatedat = new Date().toString();
    }

    public Department(String dname, Organization organization) {
        this.dname = dname;
        this.dcreatedat = new Date().toString();
        this.organization = organization;
    }

    public Department(String dname, Organization organization, Department parentDepartment) {
        this.dname = dname;
        this.dcreatedat = new Date().toString();
        this.organization = organization;
        this.parentDepartment = parentDepartment;
    }

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getDcreatedat() {
        return dcreatedat;
    }

    public void setDcreatedat(String dcreatedat) {
        this.dcreatedat = dcreatedat;
    }

    public Employee getManagerid() {
        return managerid;
    }

    public void setManagerid(Employee managerid) {
        this.managerid = managerid;
    }

    public Department getParentDepartment() {
        return parentDepartment;
    }

    public void setParentDepartment(Department parentDepartment) {
        this.parentDepartment = parentDepartment;
    }

    public List<Department> getSubDepartments() {
        return subDepartments;
    }

    public void setSubDepartments(List<Department> subDepartments) {
        this.subDepartments = subDepartments;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public String toString() {
        return "Department{" +
                "did=" + did +
                ", dname='" + dname + '\'' +
                ", dcreatedat='" + dcreatedat + '\'' +
                ", managerid=" + managerid +
                ", parentDepartment=" + parentDepartment +
                ", subDepartments=" + subDepartments +
                ", organization=" + organization +
                ", employees=" + employees +
                '}';
    }
}
