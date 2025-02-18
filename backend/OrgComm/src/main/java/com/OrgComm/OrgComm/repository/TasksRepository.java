package com.OrgComm.OrgComm.repository;

import com.OrgComm.OrgComm.entity.Tasks;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TasksRepository extends JpaRepository<Tasks,Long> {

    @Query(value = "SELECT * FROM tasks WHERE tid = ?1", nativeQuery = true)
    Tasks findByTid(Long tid);

    @Query(value = "SELECT * FROM tasks WHERE tname = ?1", nativeQuery = true)
    Tasks findByTaskname(String taskname);

    @Modifying
    @Transactional
    @Query(value="INSERT INTO tasks(taskname, taskdescription, taskdeadline, taskstatus, priority, assigned_to, assigned_by, oid) VALUES (:taskname, :taskdescription, :taskdeadline, :taskstatus, :priority, :assigned_to, :assigned_by, :oid)", nativeQuery = true)
    int insertTask(
            @Param("taskname") String taskname,
            @Param("taskdescription") String taskdescription,
            @Param("taskdeadline") String taskdeadline,
            @Param("taskstatus") String taskstatus,
            @Param("priority") int priority,
            @Param("assigned_to") Long assigned_to,
            @Param("assigned_by") Long assigned_by,
            @Param("oid") Long oid
    );

    // get all tasks based on oid
    @Query(value = "Select * from tasks where oid = ?1", nativeQuery = true)
    List<Tasks> findTasksByOid(Long oid);
}
