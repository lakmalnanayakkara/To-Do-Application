package com.ToDoTaskApp.backend.controller;

import com.ToDoTaskApp.backend.dto.StandardResponse;
import com.ToDoTaskApp.backend.dto.request.AddTaskDTO;
import com.ToDoTaskApp.backend.dto.response.TaskDetailsDTO;
import com.ToDoTaskApp.backend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/task")
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping(path = "/add-task")
    public ResponseEntity<StandardResponse> saveTask(@RequestBody AddTaskDTO addTaskDTO) {
        TaskDetailsDTO taskDetailsDTO = taskService.saveTask(addTaskDTO);
        ResponseEntity<StandardResponse> response = new ResponseEntity<>(
                new StandardResponse(200,"SUCCESS",taskDetailsDTO),
                HttpStatus.OK
        );
        return response;
    }

    @GetMapping(path = "/get-tasks")
    public ResponseEntity<StandardResponse> getTasks(){
        List<TaskDetailsDTO> taskDetailsDTOList = taskService.getTasks();
        ResponseEntity<StandardResponse> response = new ResponseEntity<>(
                new StandardResponse(200,"SUCCESS",taskDetailsDTOList),
                HttpStatus.OK
        );
        return response;
    }

    @DeleteMapping(path = "/delete-task",params = "id")
    public ResponseEntity<StandardResponse> deleteTask(@RequestParam(value = "id") int taskId){
        TaskDetailsDTO taskDetailsDTO = taskService.deleteTask(taskId);
        ResponseEntity<StandardResponse> response = new ResponseEntity<>(
                new StandardResponse(200,"SUCCESS",taskDetailsDTO),
                HttpStatus.OK
        );
        return response;
    }

    @PutMapping(path = "/update-task",params = "id")
    public ResponseEntity<StandardResponse> updateTask(@RequestBody AddTaskDTO addTaskDTO,@RequestParam(value = "id") int taskId){
        TaskDetailsDTO taskDetailsDTO = taskService.updateTask(addTaskDTO,taskId);
        ResponseEntity<StandardResponse> response = new ResponseEntity<>(
                new StandardResponse(200,"SUCCESS",taskDetailsDTO),
                HttpStatus.OK
        );
        return response;
    }

    @GetMapping(path = "/get-task",params = "id")
    public ResponseEntity<StandardResponse> getTask(@RequestParam(value = "id") int taskId){
        TaskDetailsDTO taskDetailsDTO = taskService.getTask(taskId);
        ResponseEntity<StandardResponse> response = new ResponseEntity<>(
                new StandardResponse(200,"SUCCESS",taskDetailsDTO),
                HttpStatus.OK
        );
        return response;
    }
}
