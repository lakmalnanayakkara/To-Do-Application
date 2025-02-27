package com.ToDoTaskApp.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "tasks")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Task {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id", nullable = false, unique = true)
    private int taskId;

    @Column(name = "task_title", nullable = false)
    private String taskTitle;

    @Column(name = "task_date", nullable = false)
    private LocalDate taskDate;

    @Column(name = "starting_time", nullable = false)
    private LocalTime taskTime;

    @Column(name = "task_description", nullable = false)
    private String description;
}
