package com.cydeo.service.impl;

import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProjectService projectService;
    private final TaskService taskService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper,@Lazy ProjectService projectService,@Lazy TaskService taskService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @Override
    public List<UserDTO> listAllUsers() {

        return userRepository.findAll(Sort.by("firstName")).stream().map(user -> userMapper.convertToDto(user)).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) {
        User user = userRepository.findByUserName(username);

        return userMapper.convertToDto(user);
    }

    @Override
    public void save(UserDTO userDTO) {
        User user = userMapper.convertToEntity(userDTO);

        userRepository.save(user);
    }

    @Override
    public void deleteByUserName(String username) {
        userRepository.deleteByUserName(username);
    }

    @Override
    public void delete(String username) {
        User user = userRepository.findByUserName(username);
        if(user.getRole().getDescription().equalsIgnoreCase("manager")){
            if(projectService.listProjectsByManager().stream().allMatch(projectDTO -> projectDTO.getProjectStatus()==Status.COMPLETE)){
                user.setIsDeleted(true);
            }
        }
        else if (user.getRole().getDescription().equalsIgnoreCase("employee")){
            if (taskService.listAllTasksByAssignedEmployeeUsername(username).stream().allMatch(taskDTO -> taskDTO.getTaskStatus()==Status.COMPLETE)){
                user.setIsDeleted(true);
            }
        } else user.setIsDeleted(true); 
        userRepository.save(user);
    }

    @Override
    public List<UserDTO> listAllByRole(String role) {
        List<User> users = userRepository.findByRole_DescriptionIgnoreCase(role);
        return users.stream().map(userMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        User user = userMapper.convertToEntity(userDTO);

        user.setId(userRepository.findByUserName(user.getUserName()).getId());

        userRepository.save(user);

        return findByUserName(user.getUserName());
    }

}
