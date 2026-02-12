package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequest {

    private static final String USERNAME_REQUIRED_MESSAGE = "Username must not be blank";
    private static final String USERNAME_LENGTH_MESSAGE = "Username length must be between 3 and 50";

    private static final String EMAIL_REQUIRED_MESSAGE = "Email must not be blank";
    private static final String EMAIL_INVALID_MESSAGE = "Invalid email format";

    private static final String PASSWORD_REQUIRED_MESSAGE = "Password must not be blank";
    private static final String PASSWORD_LENGTH_MESSAGE = "Password must be at least 6 characters";

    private static final String ROLE_REQUIRED_MESSAGE = "Role must not be blank";

    @NotBlank(message = USERNAME_REQUIRED_MESSAGE)
    @Size(min = 3, max = 50, message = USERNAME_LENGTH_MESSAGE)
    private String username;

    @NotBlank(message = EMAIL_REQUIRED_MESSAGE)
    @Email(message = EMAIL_INVALID_MESSAGE)
    private String email;

    @NotBlank(message = PASSWORD_REQUIRED_MESSAGE)
    @Size(min = 6, message = PASSWORD_LENGTH_MESSAGE)
    private String password;

    @NotBlank(message = ROLE_REQUIRED_MESSAGE)
    private String role;
}