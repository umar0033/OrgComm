package com.OrgComm.OrgComm.dto;


import com.OrgComm.OrgComm.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TasksDTO {
    private String tname;

    // a list of employees to whom the task is assigned
    private List<Long> assignedTo;


}
