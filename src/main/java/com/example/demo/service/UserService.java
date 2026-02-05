package com.example.demo.service;

import com.example.demo.dto.UserCreateRequest;
import com.example.demo.entity.User;
import com.example.demo.expection.ApiException1;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private static final String USER_NOT_FOUND_MESSAGE = "User not found";
    private static final String USER_NOT_FOUND_CODE = "USER_NOT_FOUND";

    private static final String USERNAME_EXISTS_MESSAGE = "Username already exists";
    private static final String USERNAME_EXISTS_CODE = "USERNAME_EXISTS";

    private static final String EMAIL_EXISTS_MESSAGE = "Email already exists";
    private static final String EMAIL_EXISTS_CODE = "EMAIL_EXISTS";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> buildUserNotFoundException());
    }

    public User createUser(UserCreateRequest request) {
        validateUsernameNotExists(request.getUsername());
        validateEmailNotExists(request.getEmail());

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User newUser = buildUser(request, encodedPassword);

        return userRepository.save(newUser);
    }

    public User updateStatus(int userId, User.Status status) {
        User existingUser = getUserById(userId);
        existingUser.setStatus(status);
        existingUser.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(existingUser);
    }

    public void deleteUser(int userId) {
        User existingUser = getUserById(userId);
        userRepository.delete(existingUser);
    }

    private void validateUsernameNotExists(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new ApiException1(USERNAME_EXISTS_MESSAGE, USERNAME_EXISTS_CODE);
        }
    }

    private void validateEmailNotExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ApiException1(EMAIL_EXISTS_MESSAGE, EMAIL_EXISTS_CODE);
        }
    }

    private User buildUser(UserCreateRequest request, String encodedPassword) {
        return new User(
                request.getUsername(),
                request.getEmail(),
                encodedPassword
        );
    }

    private ApiException1 buildUserNotFoundException() {
        return new ApiException1(USER_NOT_FOUND_MESSAGE, USER_NOT_FOUND_CODE);
    }
}