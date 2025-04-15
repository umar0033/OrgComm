package com.OrgComm.OrgComm.service;


import com.OrgComm.OrgComm.entity.Notification;
import com.OrgComm.OrgComm.repository.NotificationRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public boolean sendNotification(Notification notification) {
        if(notificationRepository.save(notification)==null) {
            return false;
        }
        return true;
    }

}
