package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    private static final String EMAIL_REQUIRED_MESSAGE = "Email must not be blank";
    private static final String EMAIL_INVALID_MESSAGE = "Invalid email format";
    private static final String PASSWORD_REQUIRED_MESSAGE = "Password must not be blank";

    @NotBlank(message = EMAIL_REQUIRED_MESSAGE)
    @Email(message = EMAIL_INVALID_MESSAGE)
    private String email;

    @NotBlank(message = PASSWORD_REQUIRED_MESSAGE)
    private String password;
}