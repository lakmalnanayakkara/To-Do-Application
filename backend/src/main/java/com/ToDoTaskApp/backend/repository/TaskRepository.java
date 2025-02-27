package com.ToDoTaskApp.backend.repository;

import com.ToDoTaskApp.backend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface TaskRepository extends JpaRepository<Task,Integer> {

    Task findByTaskTitle(String taskTitle);

    List<Task> findTop5ByOrderByTaskDateAsc();
}
