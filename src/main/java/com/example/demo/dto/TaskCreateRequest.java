package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCreateRequest {

    private static final String TITLE_REQUIRED_MESSAGE = "Title must not be blank";
    private static final String PROJECT_ID_REQUIRED_MESSAGE = "Project ID is required";

    @NotBlank(message = TITLE_REQUIRED_MESSAGE)
    private String title;

    @NotNull(message = PROJECT_ID_REQUIRED_MESSAGE)
    private Integer projectId;

    private Integer assigneeId;
}