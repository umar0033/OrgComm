package com.OrgComm.OrgComm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ChannelMembers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cmid;

    @ManyToOne
    @JoinColumn(name = "cid", referencedColumnName = "cid", nullable = false)
    private Channels channel;

    @OneToOne
    @JoinColumn(name = "eid", referencedColumnName = "eid", nullable = false)
    private Employee employee;

    public ChannelMembers() {
    }

}
