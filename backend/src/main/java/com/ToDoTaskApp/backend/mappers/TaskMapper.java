package com.ToDoTaskApp.backend.mappers;

import com.ToDoTaskApp.backend.dto.request.AddTaskDTO;
import com.ToDoTaskApp.backend.dto.response.TaskDetailsDTO;
import com.ToDoTaskApp.backend.entity.Task;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task addTaskDtoToTask(AddTaskDTO addTaskDTO);
    TaskDetailsDTO taskToTaskDetailsDTO(Task task);
    List<TaskDetailsDTO> taskListToTaskDetailsDTOList(List<Task> tasks);
}
