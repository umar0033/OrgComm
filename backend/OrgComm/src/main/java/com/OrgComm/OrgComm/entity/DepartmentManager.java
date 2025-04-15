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
public class DepartmentManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dmid;

    @OneToOne
    @JoinColumn(name = "eid",referencedColumnName = "eid")
    private Employee employee;

    @OneToOne
    @JoinColumn(name="did",referencedColumnName = "did")
    private Department department;
}
