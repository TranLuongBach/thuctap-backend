package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.UserCreateRequest;
import com.example.demo.entity.User;
import com.example.demo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = AuthController.TAG_NAME,
        description = "API xử lý đăng ký và đăng nhập người dùng"
)
@RestController
@RequestMapping(AuthController.BASE_URL)
public class AuthController {

    // ===== CONSTANTS =====
    public static final String BASE_URL = "/api/auth";
    public static final String TAG_NAME = "Authentication";

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Register account",
            description = "Tạo mới tài khoản người dùng trong hệ thống"
    )
    @PostMapping("/register")
    public User registerUser(@Valid @RequestBody UserCreateRequest payload) {
        return authService.register(payload);
    }

    @Operation(
            summary = "Login",
            description = "Xác thực thông tin đăng nhập và trả về JWT token"
    )
    @PostMapping("/login")
    public String authenticate(@Valid @RequestBody LoginRequest payload) {
        return authService.login(payload);
    }
}