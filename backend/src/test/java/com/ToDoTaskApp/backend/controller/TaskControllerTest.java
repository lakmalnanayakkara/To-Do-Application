package com.ToDoTaskApp.backend.controller;

import com.ToDoTaskApp.backend.dto.StandardResponse;
import com.ToDoTaskApp.backend.dto.request.AddTaskDTO;
import com.ToDoTaskApp.backend.dto.response.TaskDetailsDTO;
import com.ToDoTaskApp.backend.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    void testSaveTask() throws Exception {
        AddTaskDTO addTaskDTO = new AddTaskDTO("Task 1", LocalDate.now().plusDays(1),LocalTime.of(14, 30), "Description");
        TaskDetailsDTO taskDetailsDTO = new TaskDetailsDTO(1,"Task 1", LocalDate.now().plusDays(1),LocalTime.of(14, 30), "Description");

        when(taskService.saveTask(any(AddTaskDTO.class))).thenReturn(taskDetailsDTO);

        mockMvc.perform(post("/api/v1/task/add-task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"taskTitle\":\"Task 1\",\"taskDate\":\"2024-02-27\",\"taskTime\":\"12:00\",\"description\":\"Description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("SUCCESS"));

        verify(taskService, times(1)).saveTask(any(AddTaskDTO.class));
    }

    @Test
    void testGetTasks() throws Exception {
        when(taskService.getTasks()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/task/get-tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("SUCCESS"));

        verify(taskService, times(1)).getTasks();
    }

    @Test
    void testDeleteTask() throws Exception {
        TaskDetailsDTO taskDetailsDTO = new TaskDetailsDTO(1, "Task 1", LocalDate.now().plusDays(1),LocalTime.of(14, 30), "Description");

        when(taskService.deleteTask(1)).thenReturn(taskDetailsDTO);

        mockMvc.perform(delete("/api/v1/task/delete-task?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(taskService, times(1)).deleteTask(1);
    }

    @Test
    void testUpdateTask() throws Exception {
        AddTaskDTO addTaskDTO = new AddTaskDTO("Updated Task", LocalDate.now().plusDays(1),LocalTime.of(14, 30), "Updated Description");
        TaskDetailsDTO taskDetailsDTO = new TaskDetailsDTO(1, "Updated Task", LocalDate.now().plusDays(1),LocalTime.of(14, 30), "Updated Description");

        when(taskService.updateTask(any(AddTaskDTO.class), eq(1))).thenReturn(taskDetailsDTO);

        mockMvc.perform(put("/api/v1/task/update-task?id=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"taskTitle\":\"Updated Task\",\"taskDate\":\"2024-02-28\",\"taskTime\":\"14:00\",\"description\":\"Updated Description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(taskService, times(1)).updateTask(any(AddTaskDTO.class), eq(1));
    }

    @Test
    void testGetTask() throws Exception {
        TaskDetailsDTO taskDetailsDTO = new TaskDetailsDTO(1, "Task 1", LocalDate.now().plusDays(1),LocalTime.of(14, 30), "Description");

        when(taskService.getTask(1)).thenReturn(taskDetailsDTO);

        mockMvc.perform(get("/api/v1/task/get-task?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(taskService, times(1)).getTask(1);
    }
}
