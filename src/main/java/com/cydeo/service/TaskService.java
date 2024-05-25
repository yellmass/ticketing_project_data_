package com.cydeo.service;

import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;

import java.util.List;

public interface TaskService {
    List<TaskDTO> listAllTasks();
    TaskDTO findById(Long taskId);
    void save(TaskDTO taskDTO);
    void deleteById(Long taskId);
    void update(TaskDTO taskDTO);
    List<TaskDTO> listPendingTasks();
    List<TaskDTO> listAllTasksByStatus(Status taskStatus);
    List<TaskDTO> listCompletedTasksByProjectId(Long projectId);
    List<TaskDTO> listNonCompletedTasksByProjectId(Long projectId);
    List<TaskDTO> listAllTasksByProjectCode(String projectCode);
    void complete(Long taskId);
    List<TaskDTO> listAllTasksByAssignedEmployeeUsername(String username);

}
