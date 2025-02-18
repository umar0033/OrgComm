package com.OrgComm.OrgComm.service;


import com.OrgComm.OrgComm.entity.Tasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.OrgComm.OrgComm.repository.TasksRepository;

import java.util.List;

@Service
public class TasksService {

    @Autowired
    private TasksRepository taskRepository;

    public List<Tasks> findAll() {
        return taskRepository.findAll();
    }


    public Tasks findById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    public List<Tasks> findTasksByOid(Long oid){
        return taskRepository.findTasksByOid(oid);
    }


    public boolean createTask(Tasks task){;
        if(taskRepository.findByTaskname(task.getTname())!=null){
            return false;
        }
        taskRepository.save(task);
        return true;
    }

}
