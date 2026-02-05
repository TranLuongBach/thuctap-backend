package com.example.demo.service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.UserCreateRequest;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserRole;
import com.example.demo.entity.UserRoleId;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;
import com.example.demo.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private static final String ROLE_NOT_FOUND_MESSAGE = "Role not found";
    private static final String USER_NOT_FOUND_MESSAGE = "User not found";
    private static final String INVALID_PASSWORD_MESSAGE = "Invalid password";
    private static final String USER_HAS_NO_ROLE_MESSAGE = "User has no role";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       UserRoleRepository userRoleRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public User register(UserCreateRequest request) {
        User newUser = createUserFromRequest(request);
        User savedUser = userRepository.save(newUser);

        Role assignedRole = findRoleByName(request.getRole());
        UserRole userRole = buildUserRole(savedUser, assignedRole);

        userRoleRepository.save(userRole);

        return savedUser;
    }

    public String login(LoginRequest request) {
        User user = findUserByEmail(request.getEmail());
        validatePassword(request.getPassword(), user.getPassword());

        UserRole userRole = findUserRole(user);
        String roleName = userRole.getRole().getName();

        return jwtUtil.generateToken(user.getEmail(), roleName);
    }

    private User createUserFromRequest(UserCreateRequest request) {
        return new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())
        );
    }

    private Role findRoleByName(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND_MESSAGE));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND_MESSAGE));
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new RuntimeException(INVALID_PASSWORD_MESSAGE);
        }
    }

    private UserRole findUserRole(User user) {
        return userRoleRepository.findFirstByUser(user)
                .orElseThrow(() -> new RuntimeException(USER_HAS_NO_ROLE_MESSAGE));
    }

    private UserRole buildUserRole(User user, Role role) {
        UserRoleId userRoleId = new UserRoleId();
        userRoleId.setUserId(user.getId());
        userRoleId.setRoleId(role.getId());

        UserRole userRole = new UserRole();
        userRole.setId(userRoleId);
        userRole.setUser(user);
        userRole.setRole(role);
        userRole.setAssignedAt(LocalDateTime.now());

        return userRole;
    }
}