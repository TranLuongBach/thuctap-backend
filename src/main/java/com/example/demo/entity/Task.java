package com.example.demo.entity;

import com.example.demo.validation.FutureOrTodayDate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Tasks")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Title must not be blank")
    @Size(max = 150, message = "Title must not exceed 150 characters")
    @Column(nullable = false, length = 150)
    private String title;

    @Size(max = 2000, message = "Description too long")
    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId", nullable = false)
    @JsonIgnore
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigneeId")
    @JsonIgnore
    private User assignee;

    @FutureOrTodayDate
    private LocalDate deadline;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public enum Status {
        TODO, IN_PROGRESS, DONE
    }

    protected Task() {}

    public Task(String title, Project project) {
        this.title = title;
        this.project = project;
        this.status = Status.TODO;
        this.createdAt = LocalDateTime.now();
    }

    public void assign(User user) {
        if (user == null) {
            throw new RuntimeException("Assignee cannot be null");
        }

        if (this.assignee != null && this.assignee.getId() == user.getId()) {
            throw new RuntimeException("Task already assigned to this user");
        }

        this.assignee = user;
        this.updatedAt = LocalDateTime.now();
    }

    public void start() {
        this.status = Status.IN_PROGRESS;
        this.updatedAt = LocalDateTime.now();
    }

    public void done() {
        this.status = Status.DONE;
        this.updatedAt = LocalDateTime.now();
    }
}