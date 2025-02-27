package com.ToDoTaskApp.backend.controller.integration;

import com.ToDoTaskApp.backend.dto.StandardResponse;
import com.ToDoTaskApp.backend.dto.request.AddTaskDTO;
import com.ToDoTaskApp.backend.dto.response.TaskDetailsDTO;
import com.ToDoTaskApp.backend.mappers.TaskMapper;
import com.ToDoTaskApp.backend.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TaskControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    public void addTask_ShouldSaveTask() {
        AddTaskDTO addTaskDTO = new AddTaskDTO("Test Task", LocalDate.now(), LocalTime.now(),"Description");

        ResponseEntity<StandardResponse> response = restTemplate.postForEntity(
                "/api/v1/task/add-task",
                addTaskDTO,
                StandardResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        LinkedHashMap dataMap = (LinkedHashMap) response.getBody().getData();
        TaskDetailsDTO savedTask = objectMapper.convertValue(dataMap, TaskDetailsDTO.class);
        assertNotNull(savedTask.getTaskId());
        assertEquals("Test Task", savedTask.getTaskTitle());
    }

    @Test
    public void getTasks_ShouldReturnAllTasks() {
        restTemplate.postForEntity("/api/v1/task/add-task", new AddTaskDTO("Task 1", LocalDate.now(), LocalTime.now(),"Description 1"), StandardResponse.class);
        restTemplate.postForEntity("/api/v1/task/add-task", new AddTaskDTO("Task 2", LocalDate.now(), LocalTime.now(),"Description 2"), StandardResponse.class);

        ResponseEntity<StandardResponse> response = restTemplate.getForEntity("/api/v1/task/get-tasks", StandardResponse.class);

        List<TaskDetailsDTO> tasks = (List<TaskDetailsDTO>) response.getBody().getData();
        assertEquals(2, tasks.size());
    }

    @Test
    public void deleteTask_ShouldRemoveTask() {
        ResponseEntity<StandardResponse> addResponse = restTemplate.postForEntity(
                "/api/v1/task/add-task",
                new AddTaskDTO("Task 3", LocalDate.now(), LocalTime.now(),"Description 3"),
                StandardResponse.class
        );
        LinkedHashMap dataMap = (LinkedHashMap) addResponse.getBody().getData();
        TaskDetailsDTO addedTask = objectMapper.convertValue(dataMap, TaskDetailsDTO.class);
        int taskId = addedTask.getTaskId();

        restTemplate.delete("/api/v1/task/delete-task?id=" + taskId);

        ResponseEntity<StandardResponse> getResponse = restTemplate.getForEntity("/api/v1/task/get-task?id=" + taskId, StandardResponse.class);
        assertThat(getResponse.getBody().getData()).isNull();
    }

    @Test
    public void updateTask_ShouldModifyTask() {
        ResponseEntity<StandardResponse> addResponse = restTemplate.postForEntity(
                "/api/v1/task/add-task",
                new AddTaskDTO("Task 4", LocalDate.now(), LocalTime.now(),"Description 4"),
                StandardResponse.class
        );
        LinkedHashMap dataMap = (LinkedHashMap) addResponse.getBody().getData();
        TaskDetailsDTO addedTask = objectMapper.convertValue(dataMap, TaskDetailsDTO.class);
        int taskId = addedTask.getTaskId();

        AddTaskDTO updateDTO = new AddTaskDTO("Task 4 Updated", LocalDate.now(), LocalTime.now(),"Description 4");
        restTemplate.put("/api/v1/task/update-task?id=" + taskId, updateDTO);

        ResponseEntity<StandardResponse> getResponse = restTemplate.getForEntity("/api/v1/task/get-task?id=" + taskId, StandardResponse.class);
        LinkedHashMap dataMap1 = (LinkedHashMap) getResponse.getBody().getData();
        TaskDetailsDTO updatedTask = objectMapper.convertValue(dataMap1, TaskDetailsDTO.class);

        assertEquals("Task 4 Updated", updatedTask.getTaskTitle());
    }
}