package com.OrgComm.OrgComm.repository;


import com.OrgComm.OrgComm.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {


}
