package com.OrgComm.OrgComm.repository;

import com.OrgComm.OrgComm.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long> {
    @Query(value = "SELECT * FROM department WHERE did = ?1", nativeQuery = true)
    Department findByDid(Long id);

    @Query(value = "SELECT * FROM department WHERE dname = ?1", nativeQuery = true)
    Department findByDname(String dname);

    @Query(value = "SELECT * FROM department", nativeQuery = true)
    List<Department> findAll();


    @Query(value = "INSERT INTO department (dname, dcreatedat, oid, manager_id) VALUES (:dname, :dcreatedat, :oid, :managerId)", nativeQuery = true)
    int insertDepartment(
            @Param("dname") String dname,
            @Param("dcreatedat") String dcreatedat,
            @Param("oid") Long oid,
            @Param("managerId") Long managerId // Allow null if no manager is assigned
    );

}
