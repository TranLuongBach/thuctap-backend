package com.example.demo.service;

import com.example.demo.entity.Project;
import com.example.demo.entity.User;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectService {

    private static final String PROJECT_NOT_FOUND_MESSAGE = "Project not found";
    private static final String PROJECT_NAME_EMPTY_MESSAGE = "Project name must not be empty";
    private static final String OWNER_NOT_FOUND_MESSAGE = "Owner not found";
    private static final String OWNER_INACTIVE_MESSAGE = "Owner is inactive";

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository,
                          UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(int projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException(PROJECT_NOT_FOUND_MESSAGE));
    }

    public Project createProject(String name, String description, int ownerId) {
        validateProjectName(name);

        User owner = findActiveOwner(ownerId);
        Project project = new Project(name, description, owner);

        return projectRepository.save(project);
    }

    public Project updateProject(int projectId,
                                 String name,
                                 String description,
                                 Project.Status status) {

        Project existingProject = getProjectById(projectId);

        applyProjectName(existingProject, name);
        applyProjectDescription(existingProject, description);
        applyProjectStatus(existingProject, status);

        existingProject.setUpdatedAt(LocalDateTime.now());

        return projectRepository.save(existingProject);
    }

    public void deleteProject(int projectId) {
        projectRepository.deleteById(projectId);
    }

    private void validateProjectName(String name) {
        if (name == null || name.isBlank()) {
            throw new RuntimeException(PROJECT_NAME_EMPTY_MESSAGE);
        }
    }

    private User findActiveOwner(int ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException(OWNER_NOT_FOUND_MESSAGE));

        if (owner.getStatus() != User.Status.ACTIVE) {
            throw new RuntimeException(OWNER_INACTIVE_MESSAGE);
        }

        return owner;
    }

    private void applyProjectName(Project project, String name) {
        if (name != null && !name.isBlank()) {
            project.setName(name);
        }
    }

    private void applyProjectDescription(Project project, String description) {
        if (description != null) {
            project.setDescription(description);
        }
    }

    private void applyProjectStatus(Project project, Project.Status status) {
        if (status != null) {
            project.setStatus(status);
        }
    }
}