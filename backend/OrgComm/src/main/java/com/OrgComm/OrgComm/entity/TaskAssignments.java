package com.OrgComm.OrgComm.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssignments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taid;


    private int tstaus;

    private String tcompletedat;

    @ManyToOne
    @JoinColumn(name="tid",referencedColumnName = "tid")
    private Tasks tasks;

    @OneToOne
    @JoinColumn(name = "assigned_to",referencedColumnName = "eid")
    private Employee assignedTo;
}
