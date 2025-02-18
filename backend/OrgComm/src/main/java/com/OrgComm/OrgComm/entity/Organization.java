package com.OrgComm.OrgComm.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long oid;
    @Column(nullable = false)
    String oname;
    @Column(nullable = false)
    String oaddress;
    String ocreatedat;
    @Column(unique = true,nullable = false)
    String ousername;
    @Column(nullable = false)
    String opassword;

    String odescription;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employee> employees;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Department> departments;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Tasks> tasks;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Channels> channels;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications;


    public Organization() {
        this.ocreatedat = new Date().toString();
    }

    public Organization(Long id,String oname, String oaddress, String ousername, String opassword) {
        this.oid = id;
        this.oname = oname;
        this.oaddress = oaddress;
        this.ocreatedat = new Date().toString();
        this.ousername = ousername;
        this.opassword = opassword;
    }



    @Override
    public String toString() {
        return "organization{" +
                "oid=" + oid +
                ", oname='" + oname + '\'' +
                ", oaddress='" + oaddress + '\'' +
                ", ocreatedat='" + ocreatedat + '\'' +
                ", ousername='" + ousername+ '\'' +
                ", opassword='" + opassword + '\'' +
                '}';
    }


    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public String getOname() {
        return oname;
    }

    public void setOname(String oname) {
        this.oname = oname;
    }

    public String getOaddress() {
        return oaddress;
    }

    public void setOaddress(String oaddress) {
        this.oaddress = oaddress;
    }

    public String getOcreatedat() {
        return ocreatedat;
    }

    public void setOcreatedat(String ocreatedat) {
        this.ocreatedat = ocreatedat;
    }

    public String getOusername() {
        return ousername;
    }

    public void setOusername(String ousername) {
        this.ousername = ousername;
    }

    public String getOpassword() {
        return opassword;
    }

    public void setOpassword(String opassword) {
        this.opassword = opassword;
    }
    @JsonIgnore
    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Department> getDepartments() {
        //print all departments
        for(int i=0;i<departments.size();i++){
            System.out.println(departments.get(i));
        }
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public List<Tasks> getTasks() {
        return tasks;
    }

    public void setTasks(List<Tasks> tasks) {
        this.tasks = tasks;
    }

    public List<Channels> getChannels() {
        return channels;
    }

    public void setChannels(List<Channels> channels) {
        this.channels = channels;
    }
}
