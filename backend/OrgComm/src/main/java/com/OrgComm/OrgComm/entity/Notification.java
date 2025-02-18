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
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nid;

    private String message;

    @ManyToOne
    @JoinColumn(name="oid",referencedColumnName = "oid")
    private Organization organization;
}
