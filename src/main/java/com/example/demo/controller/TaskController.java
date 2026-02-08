package com.example.demo.controller;

import com.example.demo.config.OpenApiConfig;
import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.TaskCreateRequest;
import com.example.demo.entity.Task;
import com.example.demo.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = TaskController.TAG_NAME,
        description = "API phục vụ quản lý công việc trong hệ thống"
)
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME)
@RestController
@RequestMapping(TaskController.BASE_URL)
public class TaskController {

    public static final String BASE_URL = "/api/tasks";
    public static final String TAG_NAME = "Task Management";
    private static final String DELETE_SUCCESS_MESSAGE = "Xóa công việc thành công";

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Lấy toàn bộ danh sách công việc")
    @GetMapping
    public ApiResponse<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ApiResponse.success(tasks);
    }

    @Operation(summary = "Lấy chi tiết công việc theo id")
    @GetMapping("/{id}")
    public ApiResponse<Task> getTaskDetail(@PathVariable int id) {
        Task task = taskService.getTaskById(id);
        return ApiResponse.success(task);
    }

    @Operation(summary = "Tạo mới công việc")
    @PostMapping
    public ApiResponse<Task> createTask(@Valid @RequestBody TaskCreateRequest request) {
        Task newTask = taskService.createTask(
                request.getTitle(),
                request.getProjectId(),
                request.getAssigneeId()
        );
        return ApiResponse.success(newTask);
    }

    @Operation(summary = "Cập nhật thông tin công việc")
    @PutMapping("/{id}")
    public ApiResponse<Task> updateTask(@PathVariable int id,
                                        @Valid @RequestBody Task taskRequest) {

        Task updatedTask = taskService.updateTask(
                id,
                taskRequest.getTitle(),
                taskRequest.getStatus()
        );

        return ApiResponse.success(updatedTask);
    }

    @Operation(summary = "Xóa công việc theo id")
    @DeleteMapping("/{id}")
    public ApiResponse<String> removeTask(@PathVariable int id) {
        taskService.deleteTask(id);
        return ApiResponse.success(DELETE_SUCCESS_MESSAGE);
    }

    @Operation(summary = "Lấy danh sách công việc theo người dùng")
    @GetMapping("/user/{userId}")
    public ApiResponse<List<Task>> getTasksByUser(@PathVariable int userId) {
        List<Task> tasks = taskService.getTasksByUser(userId);
        return ApiResponse.success(tasks);
    }

    @Operation(summary = "Lấy danh sách công việc theo dự án")
    @GetMapping("/project/{projectId}")
    public ApiResponse<List<Task>> getTasksByProject(@PathVariable int projectId) {
        List<Task> tasks = taskService.getTasksByProject(projectId);
        return ApiResponse.success(tasks);
    }

    @Operation(summary = "Gán công việc cho người dùng")
    @PutMapping("/{id}/assign")
    public ApiResponse<Task> assignTask(@PathVariable int id,
                                        @RequestParam int userId) {

        Task assignedTask = taskService.assignTask(id, userId);
        return ApiResponse.success(assignedTask);
    }

    @Operation(summary = "Cập nhật trạng thái công việc")
    @PutMapping("/{id}/status")
    public ApiResponse<Task> changeTaskStatus(@PathVariable int id,
                                              @RequestParam Task.Status status) {

        Task updatedTask = taskService.updateTask(id, null, status);
        return ApiResponse.success(updatedTask);
    }
}