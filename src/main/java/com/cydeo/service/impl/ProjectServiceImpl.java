package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final TaskService taskService;
    private final UserService userService;
    private final UserMapper userMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper, TaskService taskService, UserService userService, UserMapper userMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.taskService = taskService;
        this.userService = userService;
        this.userMapper = userMapper;
    }


    @Override
    public ProjectDTO getByProjectCode(String code) {
        Project project = projectRepository.findByProjectCode(code).orElseThrow();

        return projectMapper.convertToDto(project);
    }

    @Override
    public List<ProjectDTO> listAllProjects() {

        List<Project> projects = projectRepository.findAll(Sort.by("projectCode"));

        return projects.stream().map(projectMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void save(ProjectDTO dto) {
        dto.setProjectStatus(Status.OPEN);
        Project projectToSave = projectMapper.convertToEntity(dto);
        //project.setProjectStatus(Status.OPEN);
        //project.setAssignedManager(userRepository.findByUserName(dto.getAssignedManager().getUserName()));
//        Optional<Project> foundProject = projectRepository.findByProjectCode(projectToSave.getProjectCode());
//        boolean foundProjectsDeleted = foundProject.stream().allMatch(project -> project.getIsDeleted()==true);
//        if (foundProject.isEmpty() || foundProjectsDeleted==true){
//                projectRepository.save(projectToSave);
//        }
        projectRepository.save(projectToSave);
    }

    @Override
    public void update(ProjectDTO dto) {
        Project projectToUpdate = projectMapper.convertToEntity(dto);
        Project projectInDb = projectRepository.findByProjectCode(dto.getProjectCode()).orElseThrow();
        projectToUpdate.setId(projectInDb.getId());
        projectToUpdate.setProjectStatus(projectInDb.getProjectStatus());
        projectRepository.save(projectToUpdate);
    }

    @Override
    public void delete(String code) {
        Project project = projectRepository.findByProjectCode(code).orElseThrow();
        project.setIsDeleted(true);

        project.setProjectCode(project.getProjectCode() + "-" +project.getId());

        taskService.listAllTasksByProjectCode(project.getProjectCode())
                .forEach(taskDTO ->{
                    taskService.deleteById(taskDTO.getId());
                });
        projectRepository.save(project);
    }

    @Override
    public void complete(String code) {
        Project project = projectRepository.findByProjectCode(code).orElseThrow();
        project.setProjectStatus(Status.COMPLETE);
        taskService.listAllTasksByProjectCode(code).forEach(taskDTO -> taskService.complete(taskDTO.getId()));
        projectRepository.save(project);
    }

    @Override
    public List<ProjectDTO> listProjectsByManager() {
        UserDTO managerDTO = userService.findByUserName("harold@manager.com");
        User manager = userMapper.convertToEntity(managerDTO);

        List<Project> projects = projectRepository.findAllByAssignedManagerOrderByProjectCode(manager);

        return projects.stream()
                .map(project  -> {
                    ProjectDTO projectDTO = projectMapper.convertToDto(project);
                    projectDTO.setUnfinishedTaskCounts(taskService.listNonCompletedTasksByProjectId(projectDTO.getId()).size());
                    projectDTO.setCompleteTaskCounts(taskService.listCompletedTasksByProjectId(projectDTO.getId()).size());
                    return projectDTO;
                })
                .collect(Collectors.toList());
    }


}
