package com.example.demo;

import com.example.demo.entity.Project;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;

import com.example.demo.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
 //mock
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTask_success() {

        // mock project
        Project project = mock(Project.class);

        // mock user
        User user = mock(User.class);
        when(user.getStatus()).thenReturn(User.Status.ACTIVE);

        // mock repository behavior
        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        when(taskRepository.existsByTitleAndProject_Id("Test Task", 1)).thenReturn(false);
        when(userRepository.findById(2)).thenReturn(Optional.of(user));

        // task sau khi save
        Task savedTask = mock(Task.class);
        when(savedTask.getTitle()).thenReturn("Test Task");

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        // gọi service
        Task result = taskService.createTask("Test Task", 1, 2);

        // kiểm tra kết quả
        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());

        verify(taskRepository).save(any(Task.class));
    }


    @Test
    void assignTask_success() {

        // mock entities
        Task task = mock(Task.class);
        Project project = mock(Project.class);
        User owner = mock(User.class);
        User user = mock(User.class);

        when(task.getProject()).thenReturn(project);
        when(project.getOwner()).thenReturn(owner);
        when(owner.getId()).thenReturn(2);

        when(user.getId()).thenReturn(2);
        when(user.getStatus()).thenReturn(User.Status.ACTIVE);

        // mock repository
        when(taskRepository.findById(1)).thenReturn(Optional.of(task));
        when(userRepository.findById(2)).thenReturn(Optional.of(user));
        when(taskRepository.save(task)).thenReturn(task);

        // run service
        Task result = taskService.assignTask(1, 2);

        // verify
        assertNotNull(result);
        verify(task).assign(user);
        verify(taskRepository).save(task);
    }


    @Test
    void assignTask_userInactive() {

        Task task = mock(Task.class);
        User user = mock(User.class);

        when(taskRepository.findById(1)).thenReturn(Optional.of(task));
        when(userRepository.findById(2)).thenReturn(Optional.of(user));

        when(user.getStatus()).thenReturn(User.Status.INACTIVE);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> taskService.assignTask(1, 2));

        assertEquals("User is inactive", ex.getMessage());
    }

}