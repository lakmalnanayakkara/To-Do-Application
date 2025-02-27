package com.ToDoTaskApp.backend.service.implementation;

import com.ToDoTaskApp.backend.dto.request.AddTaskDTO;
import com.ToDoTaskApp.backend.dto.response.TaskDetailsDTO;
import com.ToDoTaskApp.backend.entity.Task;
import com.ToDoTaskApp.backend.exception.AlreadyExistException;
import com.ToDoTaskApp.backend.exception.NotFoundException;
import com.ToDoTaskApp.backend.mappers.TaskMapper;
import com.ToDoTaskApp.backend.repository.TaskRepository;
import com.ToDoTaskApp.backend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceIMPL implements TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public TaskDetailsDTO saveTask(AddTaskDTO addTaskDTO) {
        Task taskExist = taskRepository.findByTaskTitle(addTaskDTO.getTaskTitle());
        if( taskExist != null){
            throw new AlreadyExistException("Task Already Exist");
        }else {
            Task task = taskMapper.addTaskDtoToTask(addTaskDTO);
            Task savedTask = taskRepository.save(task);
            TaskDetailsDTO taskDetailsDTO = taskMapper.taskToTaskDetailsDTO(savedTask);
            return taskDetailsDTO;
        }
    }

    @Override
    public List<TaskDetailsDTO> getTasks() {
        List<Task> taskList = taskRepository.findTop5ByOrderByTaskDateAsc();
        if(taskList.size() == 0){
            throw new NotFoundException("No Tasks found.");
        }else {
            List<TaskDetailsDTO> taskDetailsDTOList = taskMapper.taskListToTaskDetailsDTOList(taskList);
            return taskDetailsDTOList;
        }
    }

    @Override
    public TaskDetailsDTO deleteTask(int taskId) {
        Task task = taskRepository.getReferenceById(taskId);
        if(task == null){
            throw new NotFoundException("Task doesn't exist");
        }else {
            TaskDetailsDTO taskDetailsDTO = taskMapper.taskToTaskDetailsDTO(task);
            taskRepository.delete(task);
            return taskDetailsDTO;
        }
    }

    @Override
    public TaskDetailsDTO updateTask(AddTaskDTO addTaskDTO, int taskId) {
        Task task = taskRepository.getReferenceById(taskId);
        if(task == null){
            throw new NotFoundException("Task doesn't exist");
        }else {
            task.setTaskTitle(addTaskDTO.getTaskTitle());
            task.setTaskDate(addTaskDTO.getTaskDate());
            task.setTaskTime(addTaskDTO.getTaskTime());
            task.setDescription(addTaskDTO.getDescription());

            TaskDetailsDTO taskDetailsDTO = taskMapper.taskToTaskDetailsDTO(task);
            taskRepository.save(task);
            return taskDetailsDTO;
        }
    }

    @Override
    public TaskDetailsDTO getTask(int taskId) {
        Task task = taskRepository.getReferenceById(taskId);
        if(task == null){
            throw new NotFoundException("Task doesn't exist");
        }else {
            TaskDetailsDTO taskDetailsDTO = taskMapper.taskToTaskDetailsDTO(task);
            return taskDetailsDTO;
        }
    }
}
