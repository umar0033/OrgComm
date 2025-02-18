package com.OrgComm.OrgComm.repository;

import com.OrgComm.OrgComm.entity.Employee;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    // Add a method to find an employee by its id
    @Query(value = "SELECT * FROM employee WHERE eid = ?1", nativeQuery = true)
    Employee findByEid(Long id);

    @Query(value = "SELECT * FROM employee WHERE eusername = ?1", nativeQuery = true)
    Employee findByEusername(String eusername);

    @Query(value = "SELECT * FROM employee", nativeQuery = true)
    List<Employee> findAll();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO employee (ename, eaddress, ecreatedat, eusername, epassword, oid) VALUES (:ename, :eaddress, :ecreatedat, :eusername, :epassword, :oid)", nativeQuery = true)
    int insertEmployee(
            @Param("ename") String ename,
            @Param("eaddress") String eaddress,
            @Param("ecreatedat") String ecreatedat,
            @Param("eusername") String eusername,
            @Param("epassword") String epassword,
            @Param("oid") Long oid
    );

    @Modifying
    @Transactional
    @Query(value="INSERT INTO employee (ename, eaddress, ecreatedat, eusername, epassword, oid, did, managerid) VALUES (:ename, :eaddress, :ecreatedat, :eusername, :epassword, :oid, :did, :managerid)", nativeQuery = true)
    int insertEmployeeWithDAM(@Param("ename") String ename,
                              @Param("eaddress") String eaddress,
                              @Param("ecreatedat") String ecreatedat,
                              @Param("eusername") String eusername,
                              @Param("epassword") String epassword,
                              @Param("oid") Long oid,
                              @Param("did") Long did,
                              @Param("managerid") Long managerid);


    @Query(value = "SELECT * FROM employee WHERE oid = ?1", nativeQuery = true)
    List<Employee> findEmployeesByOid(Long oid);


    // write a query to update employee based on eid
    @Modifying
    @Transactional
    @Query(value = "UPDATE employee SET ename = :ename, eaddress = :eaddress, ecreatedat = :ecreatedat, eusername = :eusername, epassword = :epassword, oid = :oid, did = :did, managerid = :managerid WHERE eid = :eid", nativeQuery = true)
    int updateEmployee(
            @Param("eid") Long eid,
            @Param("ename") String ename,
            @Param("eaddress") String eaddress,
            @Param("ecreatedat") String ecreatedat,
            @Param("eusername") String eusername,
            @Param("epassword") String epassword,
            @Param("oid") Long oid,
            @Param("did") Long did,
            @Param("managerid") Long managerid
    );

}
