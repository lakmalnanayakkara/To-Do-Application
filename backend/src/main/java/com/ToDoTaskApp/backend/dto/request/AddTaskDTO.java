package com.ToDoTaskApp.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddTaskDTO {
    private String taskTitle;
    private LocalDate taskDate;
    private LocalTime taskTime;
    private String description;
}
