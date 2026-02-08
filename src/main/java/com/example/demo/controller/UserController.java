package com.example.demo.controller;

import com.example.demo.config.OpenApiConfig;
import com.example.demo.dto.UserCreateRequest;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(
        name = UserController.TAG_NAME,
        description = "API phục vụ quản lý thông tin người dùng trong hệ thống"
)
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME)
@RestController
@RequestMapping(UserController.BASE_URL)
public class UserController {

    public static final String BASE_URL = "/api/users";
    public static final String TAG_NAME = "User Management";
    private static final String STATUS_FIELD = "status";

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Lấy toàn bộ danh sách người dùng",
            description = "Trả về danh sách tất cả người dùng hiện có trong hệ thống"
    )
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(
            summary = "Lấy thông tin người dùng theo id",
            description = "Truy vấn chi tiết người dùng dựa trên mã định danh"
    )
    @GetMapping("/{id}")
    public User getUserDetail(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @Operation(
            summary = "Tạo mới người dùng",
            description = "Thêm một người dùng mới vào hệ thống"
    )
    @PostMapping
    public User createUser(@Valid @RequestBody UserCreateRequest payload) {
        return userService.createUser(payload);
    }

    @Operation(
            summary = "Cập nhật trạng thái người dùng",
            description = "Thay đổi trạng thái hoạt động của người dùng theo id"
    )
    @PutMapping("/{id}/status")
    public User changeUserStatus(@PathVariable int id,
                                 @RequestBody Map<String, String> requestBody) {

        User.Status status = parseStatus(requestBody);
        return userService.updateStatus(id, status);
    }

    @Operation(
            summary = "Xóa người dùng",
            description = "Xóa người dùng khỏi hệ thống theo id"
    )
    @DeleteMapping("/{id}")
    public void removeUser(@PathVariable int id) {
        userService.deleteUser(id);
    }

    private User.Status parseStatus(Map<String, String> requestBody) {
        try {
            String rawStatus = requestBody.get(STATUS_FIELD);
            return User.Status.valueOf(rawStatus);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Giá trị trạng thái không hợp lệ");
        }
    }
}