package com.OrgComm.OrgComm.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Tasks{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tid;

    @Column(nullable = false)
    private String tname;


    private String tdescription;

    private String tcreatedat;

    private String tdeadline;


    private int priority;



    // tell me that task is assigned by whom to who
    @ManyToOne
    @JoinColumn(name = "assigned_by")
    private Employee assignedBy;

    @ManyToOne
    @JoinColumn(name = "oid")
    private Organization organization;

    @OneToMany(mappedBy="tasks",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskAssignments> taskAssignments;

    public Tasks() {
        this.tcreatedat = new Date().toString();
    }

    public Tasks(String tname, String tdescription, String tdeadline, int priority, Employee assignedBy, Organization organization) {
        this.tname = tname;
        this.tdescription = tdescription;
        this.tdeadline = tdeadline;
        this.priority = priority;
        this.assignedBy = assignedBy;
        this.organization = organization;
        this.tcreatedat = new Date().toString();
    }





}
