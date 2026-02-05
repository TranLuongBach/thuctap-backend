package com.example.demo.repository;

import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    // Check trùng title trong cùng project
    boolean existsByTitleAndProject_Id(String title, int projectId);

    List<Task> findByAssignee_Id(int userId);
    List<Task> findByAssignee(User user);
    List<Task> findByProject_Id(int projectId);
}
