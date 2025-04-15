package com.OrgComm.OrgComm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eid;
    @Column(nullable = false)
    private String ename;
    @Column(nullable = false)
    private String eaddress;
    private String ecreatedat;
    @Column(unique = true,nullable = false)
    private String eusername;
    @Column(nullable = false)
    private String epassword;

    private String eposition;





    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employee> subordinates;

    // an organization can have many employees
    @ManyToOne
    @JoinColumn(name = "oid",nullable = false)
    @JsonIgnore
    private Organization organization;

    // a department can have many employees
    @ManyToOne
    @JoinColumn(name = "did")
    private Department department;

    @OneToOne(mappedBy = "managerid", cascade = CascadeType.ALL, orphanRemoval = true)
    private Department managedDepartments;

    @OneToOne(mappedBy = "assignedTo", cascade = CascadeType.ALL, orphanRemoval = true)
    private TaskAssignments taskAssignments;

    @OneToOne(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    private Channels channels;

    // a manager assigned tasks to many employees
    @OneToMany(mappedBy = "assignedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tasks> assignedTasks;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private ChannelMembers channelMembers;


    public Employee() {
        this.ecreatedat = new Date().toString();
    }


    public Employee(String ename, String eaddress, String eusername, String epassword, Organization organization) {
        this.ename = ename;
        this.eaddress = eaddress;
        this.ecreatedat = new Date().toString();
        this.eusername = eusername;
        this.epassword = epassword;
        this.organization = organization;
    }
    public Employee(String ename, String eaddress, String eusername, String epassword, Organization organization, Department department,Employee manager) {
        this.ename = ename;
        this.eaddress = eaddress;
        this.ecreatedat = new Date().toString();
        this.eusername = eusername;
        this.epassword = epassword;
        this.organization = organization;
        this.department = department;
        this.manager = manager;
    }

    public Organization getOrganization() {
        return organization;
    }
}
