package com.example.demo;

import com.example.demo.entity.Project;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TaskMappingTest {

    @Test
    void shouldMapTaskToProject() {

        User owner = new User("owner", "o@test.com", "123");
        Project project = new Project("Project A", "Desc", owner);

        Task task = new Task("Task 1", project);

        assertNotNull(task.getProject());
        assertEquals(project, task.getProject());
    }

    @Test
    void shouldMapTaskToAssignee() {

        User owner = new User("owner", "o@test.com", "123");
        Project project = new Project("Project A", "Desc", owner);

        Task task = new Task("Task 1", project);

        User assignee = new User("dev", "dev@test.com", "123");
        task.assign(assignee);

        assertNotNull(task.getAssignee());
        assertEquals(assignee, task.getAssignee());
    }


}
