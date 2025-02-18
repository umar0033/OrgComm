package com.OrgComm.OrgComm.repository;


import com.OrgComm.OrgComm.entity.TaskAssignments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskAssignmentsRepository extends JpaRepository<TaskAssignments,Long> {


}
