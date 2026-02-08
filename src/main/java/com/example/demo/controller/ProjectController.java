package com.example.demo.controller;

import com.example.demo.config.OpenApiConfig;
import com.example.demo.dto.ApiResponse;
import com.example.demo.entity.Project;
import com.example.demo.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = ProjectController.TAG_NAME,
        description = "API phục vụ quản lý thông tin dự án trong hệ thống"
)
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME)
@RestController
@RequestMapping(ProjectController.BASE_URL)
public class ProjectController {

    public static final String BASE_URL = "/api/projects";
    public static final String TAG_NAME = "Project Management";
    private static final String DELETE_SUCCESS_MESSAGE = "Xóa dự án thành công";

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "Lấy toàn bộ danh sách dự án")
    @GetMapping
    public ApiResponse<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return ApiResponse.success(projects);
    }

    @Operation(summary = "Lấy chi tiết dự án theo id")
    @GetMapping("/{id}")
    public ApiResponse<Project> getProjectDetail(@PathVariable int id) {
        Project project = projectService.getProjectById(id);
        return ApiResponse.success(project);
    }

    @Operation(summary = "Tạo mới một dự án")
    @PostMapping
    public ApiResponse<Project> createProject(@RequestParam String name,
                                              @RequestParam String description,
                                              @RequestParam int ownerId) {

        Project createdProject = projectService.createProject(name, description, ownerId);
        return ApiResponse.success(createdProject);
    }

    @Operation(summary = "Cập nhật thông tin dự án")
    @PutMapping("/{id}")
    public ApiResponse<Project> updateProject(@PathVariable int id,
                                              @RequestParam(required = false) String name,
                                              @RequestParam(required = false) String description,
                                              @RequestParam(required = false) Project.Status status) {

        Project updatedProject = projectService.updateProject(id, name, description, status);
        return ApiResponse.success(updatedProject);
    }

    @Operation(summary = "Xóa dự án theo id")
    @DeleteMapping("/{id}")
    public ApiResponse<String> removeProject(@PathVariable int id) {
        projectService.deleteProject(id);
        return ApiResponse.success(DELETE_SUCCESS_MESSAGE);
    }
}