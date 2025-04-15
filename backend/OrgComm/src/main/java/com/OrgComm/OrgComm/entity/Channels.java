package com.OrgComm.OrgComm.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Channels {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cid;

    private String cname;


    private String ccreatedat;

    @ManyToOne
    @JoinColumn(name="oid",referencedColumnName = "oid",nullable = false)
    private Organization organization;

    @OneToOne
    @JoinColumn(name="admin_id",referencedColumnName = "eid")
    private Employee admin;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChannelMembers> channelMembers;





    public Channels() {
        this.ccreatedat = new Date().toString();
    }
}
