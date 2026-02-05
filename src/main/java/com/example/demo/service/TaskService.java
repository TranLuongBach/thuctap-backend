package com.example.demo.service;

import com.example.demo.entity.Project;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    private static final String TASK_NOT_FOUND_MESSAGE = "Task not found";
    private static final String PROJECT_NOT_FOUND_MESSAGE = "Project not found";
    private static final String USER_NOT_FOUND_MESSAGE = "User not found";
    private static final String USER_INACTIVE_MESSAGE = "User is inactive";
    private static final String ACCESS_DENIED_MESSAGE = "Access denied";
    private static final String TITLE_EMPTY_MESSAGE = "Title must not be empty";
    private static final String TASK_TITLE_EXISTS_MESSAGE = "Task title already exists in this project";
    private static final String TASK_COMPLETED_UPDATE_MESSAGE = "Cannot update a completed task";
    private static final String USER_NOT_IN_PROJECT_MESSAGE = "User does not belong to this project";
    private static final String MANAGER_ROLE = "ROLE_MANAGER";

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository,
                       ProjectRepository projectRepository,
                       UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public List<Task> getAllTasks() {
        Authentication authentication = getAuthentication();
        User currentUser = getAuthenticatedUser();

        if (hasManagerRole(authentication)) {
            return taskRepository.findAll();
        }

        return taskRepository.findByAssignee(currentUser);
    }

    public Task getTaskById(int taskId) {
        Task task = findTaskById(taskId);
        Authentication authentication = getAuthentication();

        if (hasManagerRole(authentication)) {
            return task;
        }

        validateTaskAccess(task, authentication.getName());
        return task;
    }

    public Task createTask(String title, int projectId, Integer assigneeId) {
        validateTaskTitle(title);

        Project project = findProjectById(projectId);
        validateDuplicateTaskTitle(title, projectId);

        Task newTask = new Task(title, project);

        if (assigneeId != null) {
            User assignee = findUserById(assigneeId);
            validateUserActive(assignee);
            newTask.assign(assignee);
        }

        return taskRepository.save(newTask);
    }

    public Task updateTask(int taskId, String title, Task.Status status) {
        Task existingTask = getTaskById(taskId);

        validateTaskUpdatable(existingTask);
        applyTaskTitle(existingTask, title);
        applyTaskStatus(existingTask, status);

        return taskRepository.save(existingTask);
    }

    public void deleteTask(int taskId) {
        taskRepository.deleteById(taskId);
    }

    public List<Task> getTasksByUser(int userId) {
        User user = findUserById(userId);
        return taskRepository.findByAssignee_Id(user.getId());
    }

    public List<Task> getTasksByProject(int projectId) {
        findProjectById(projectId);
        return taskRepository.findByProject_Id(projectId);
    }

    public Task assignTask(int taskId, int userId) {
        Task task = findTaskById(taskId);
        User user = findUserById(userId);

        validateUserActive(user);

        if (task.getProject().getOwner().getId() != user.getId()) {
            throw new RuntimeException(USER_NOT_IN_PROJECT_MESSAGE);
        }

        task.assign(user);
        return taskRepository.save(task);
    }

    private Task findTaskById(int taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException(TASK_NOT_FOUND_MESSAGE));
    }

    private Project findProjectById(int projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException(PROJECT_NOT_FOUND_MESSAGE));
    }

    private User findUserById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND_MESSAGE));
    }

    private void validateUserActive(User user) {
        if (user.getStatus() != User.Status.ACTIVE) {
            throw new RuntimeException(USER_INACTIVE_MESSAGE);
        }
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private User getAuthenticatedUser() {
        String email = getAuthentication().getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND_MESSAGE));
    }

    private boolean hasManagerRole(Authentication authentication) {
        return authentication.getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getAuthority().equals(MANAGER_ROLE));
    }

    private void validateTaskAccess(Task task, String email) {
        if (task.getAssignee() == null || !task.getAssignee().getEmail().equals(email)) {
            throw new RuntimeException(ACCESS_DENIED_MESSAGE);
        }
    }

    private void validateTaskTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new RuntimeException(TITLE_EMPTY_MESSAGE);
        }
    }

    private void validateDuplicateTaskTitle(String title, int projectId) {
        if (taskRepository.existsByTitleAndProject_Id(title, projectId)) {
            throw new RuntimeException(TASK_TITLE_EXISTS_MESSAGE);
        }
    }

    private void validateTaskUpdatable(Task task) {
        if (task.getStatus() == Task.Status.DONE) {
            throw new RuntimeException(TASK_COMPLETED_UPDATE_MESSAGE);
        }
    }

    private void applyTaskTitle(Task task, String title) {
        if (title == null || title.isBlank()) {
            return;
        }

        boolean duplicatedTitle = !title.equals(task.getTitle())
                && taskRepository.existsByTitleAndProject_Id(title, task.getProject().getId());

        if (duplicatedTitle) {
            throw new RuntimeException(TASK_TITLE_EXISTS_MESSAGE);
        }

        task.setTitle(title);
    }

    private void applyTaskStatus(Task task, Task.Status status) {
        if (status != null) {
            task.setStatus(status);
            task.setUpdatedAt(LocalDateTime.now());
        }
    }
}