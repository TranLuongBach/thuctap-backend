package com.example.demo;

import org.junit.jupiter.api.Test;
import com.example.demo.entity.Project;
import com.example.demo.entity.User;
import static org.junit.jupiter.api.Assertions.*;

class ProjectMappingTest {

    @Test
    void shouldMapProjectToOwner() {

        User owner = new User("admin", "admin@test.com", "123");

        Project project = new Project(
                "Demo Project",
                "Description",
                owner
        );

        assertNotNull(project.getOwner());
        assertEquals(owner, project.getOwner());
    }
}
