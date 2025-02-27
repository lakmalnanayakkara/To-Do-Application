package com.ToDoTaskApp.backend.service;

import com.ToDoTaskApp.backend.dto.request.AddTaskDTO;
import com.ToDoTaskApp.backend.dto.response.TaskDetailsDTO;

import java.util.List;

public interface TaskService {
    TaskDetailsDTO saveTask(AddTaskDTO addTaskDTO);

    List<TaskDetailsDTO> getTasks();

    TaskDetailsDTO deleteTask(int taskId);

    TaskDetailsDTO updateTask(AddTaskDTO addTaskDTO, int taskId);

    TaskDetailsDTO getTask(int taskId);
}
