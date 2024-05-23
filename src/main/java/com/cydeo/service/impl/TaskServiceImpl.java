package com.cydeo.service.impl;

import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.Task;
import com.cydeo.enums.Status;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ProjectRepository projectRepository;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.projectRepository = projectRepository;
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
        taskToUpdate.setTaskStatus(taskInDb.getTaskStatus());
        taskToUpdate.setAssignedDate(taskInDb.getAssignedDate());
        //taskToUpdate.setProject(projectRepository.findByProjectCode(taskDTO.getProject().getProjectCode()));
        taskRepository.save(taskToUpdate);
    }

    @Override
    public void updateStatus(TaskDTO taskDTO) {
        Task task = taskRepository.findById(taskDTO.getId()).orElseThrow();
        task.setTaskStatus(taskDTO.getTaskStatus());
        taskRepository.save(task);
    }

    @Override
    public List<TaskDTO> listPendingTasks() {
        List<Task> pendingTasks = taskRepository.findAllByTaskStatusNot(Status.COMPLETE);

        return pendingTasks.stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> listAllTasksByStatus(Status taskStatus) {
        List<Task> tasks = taskRepository.findAllByTaskStatus(taskStatus);
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
    public List<TaskDTO> listAllTasksByProjectId(Long projectId) {
        List<Task> tasks = taskRepository.findAllByProject_Id(projectId);
        return tasks.stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }
}
