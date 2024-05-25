package com.cydeo.service.impl;

import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Task;
import com.cydeo.enums.Status;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserService userService;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, UserService userService) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.userService = userService;
    }


    @Override
    public List<TaskDTO> listAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(task -> taskMapper.convertToDto(task)).collect(Collectors.toList());
    }

    @Override
    public TaskDTO findById(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        return taskMapper.convertToDto(task);
    }

    @Override
    public void save(TaskDTO taskDTO) {
        taskDTO.setTaskStatus(Status.OPEN);
        taskDTO.setAssignedDate(LocalDate.now());
        Task task = taskMapper.convertToEntity(taskDTO);
        //Project selectedProject = projectRepository.findByProjectCode(taskDTO.getProject().getProjectCode());
        //task.setProject(selectedProject);
        taskRepository.save(task);
    }

    @Override
    public void deleteById(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        task.setIsDeleted(true);
        taskRepository.save(task);
    }

    @Override
    public void update(TaskDTO taskDTO) {
        Task taskToUpdate = taskMapper.convertToEntity(taskDTO);
        Task taskInDb = taskRepository.findById(taskDTO.getId()).orElseThrow();
        taskToUpdate.setTaskStatus(taskDTO.getTaskStatus()==null ?  taskInDb.getTaskStatus() :  taskDTO.getTaskStatus() );
        taskToUpdate.setAssignedDate(taskInDb.getAssignedDate());
        //taskToUpdate.setProject(projectRepository.findByProjectCode(taskDTO.getProject().getProjectCode()));
        taskRepository.save(taskToUpdate);
    }

    @Override
    public List<TaskDTO> listPendingTasks() {
        UserDTO loggedEmployee =  userService.findByUserName("john@employee.com");

        List<Task> pendingTasks = taskRepository.findAllByTaskStatusNotAndAssignedEmployee_Id(Status.COMPLETE, loggedEmployee.getId());

        return pendingTasks.stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> listAllTasksByStatus(Status taskStatus) {
        UserDTO loggedEmployee = userService.findByUserName("john@employee.com");
        List<Task> tasks = taskRepository.findAllByTaskStatusAndAssignedEmployee_Id(taskStatus, loggedEmployee.getId());
        return tasks.stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> listCompletedTasksByProjectId(Long projectId) {
        List<Task> tasks = taskRepository.totalCompletedTasks(projectId);
        return tasks.stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> listNonCompletedTasksByProjectId(Long projectId) {
        List<Task> tasks = taskRepository.totalNonCompletedTasks(projectId);
        return tasks.stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> listAllTasksByProjectCode(String projectCode) {
        List<Task> tasks = taskRepository.findAllByProject_ProjectCode(projectCode);
        return tasks.stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void complete(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        task.setTaskStatus(Status.COMPLETE);
        taskRepository.save(task);
    }

    @Override
    public List<TaskDTO> listAllTasksByAssignedEmployeeUsername(String username) {
        List<Task> tasks = taskRepository.findAllByAssignedEmployee_UserName(username);
        return tasks.stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }
}
