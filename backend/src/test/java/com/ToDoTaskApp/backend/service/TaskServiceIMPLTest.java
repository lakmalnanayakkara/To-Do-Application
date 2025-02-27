package com.ToDoTaskApp.backend.service;

import com.ToDoTaskApp.backend.dto.request.AddTaskDTO;
import com.ToDoTaskApp.backend.dto.response.TaskDetailsDTO;
import com.ToDoTaskApp.backend.entity.Task;
import com.ToDoTaskApp.backend.exception.AlreadyExistException;
import com.ToDoTaskApp.backend.exception.NotFoundException;
import com.ToDoTaskApp.backend.mappers.TaskMapper;
import com.ToDoTaskApp.backend.repository.TaskRepository;
import com.ToDoTaskApp.backend.service.implementation.TaskServiceIMPL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceIMPLTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceIMPL taskService;

    private final int VALID_TASK_ID = 1;
    private final int INVALID_TASK_ID = 999;
    private final AddTaskDTO SAMPLE_DTO = new AddTaskDTO(
            "Updated Task",
            LocalDate.of(2025, 3, 1),
            LocalTime.of(15, 0),
            "Updated Description"
    );

    private Task mockTask;

    @BeforeEach
    void setUp() {
        mockTask = new Task(
                VALID_TASK_ID,
                "Sample Task",
                LocalDate.now(),
                LocalTime.now(),
                "Sample Description"
        );
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveTask_Success() {
        AddTaskDTO addTaskDTO = new AddTaskDTO("New Task", LocalDate.now().plusDays(1),LocalTime.of(14, 30), "Description");
        Task task = new Task(1, "New Task", LocalDate.now().plusDays(1),LocalTime.of(14, 30), "Description");

        when(taskRepository.findByTaskTitle("New Task")).thenReturn(null);
        when(taskMapper.addTaskDtoToTask(addTaskDTO)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.taskToTaskDetailsDTO(task)).thenReturn(new TaskDetailsDTO(1, "New Task", LocalDate.now().plusDays(1),LocalTime.of(14, 30), "Description"));

        TaskDetailsDTO result = taskService.saveTask(addTaskDTO);

        assertEquals("New Task", result.getTaskTitle());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testSaveTask_TaskAlreadyExists() {
        AddTaskDTO addTaskDTO = new AddTaskDTO("Existing Task", LocalDate.now().plusDays(1), LocalTime.of(14, 30), "Description");
        when(taskRepository.findByTaskTitle("Existing Task")).thenReturn(new Task());
        assertThrows(AlreadyExistException.class, () -> taskService.saveTask(addTaskDTO));
    }

    @Test
    void testGetTasks_Success() {
        List<Task> tasks = List.of(
                new Task(1, "Task 1", LocalDate.now(), LocalTime.now(), "Desc 1"),
                new Task(2, "Task 2", LocalDate.now().plusDays(1), LocalTime.now(), "Desc 2")
        );

        List<TaskDetailsDTO> expectedDtos = List.of(
                new TaskDetailsDTO(1, "Task 1", LocalDate.now(), LocalTime.now(), "Desc 1"),
                new TaskDetailsDTO(2, "Task 2", LocalDate.now().plusDays(1), LocalTime.now(), "Desc 2")
        );

        when(taskRepository.findTop5ByOrderByTaskDateAsc()).thenReturn(tasks);
        when(taskMapper.taskListToTaskDetailsDTOList(tasks)).thenReturn(expectedDtos);

        List<TaskDetailsDTO> result = taskService.getTasks();

        assertEquals(2, result.size());
        assertEquals(expectedDtos, result);
        verify(taskRepository).findTop5ByOrderByTaskDateAsc();
        verify(taskMapper).taskListToTaskDetailsDTOList(tasks);
    }

    @Test
    void testGetTasks_NotFound() {
        when(taskRepository.findTop5ByOrderByTaskDateAsc()).thenReturn(Collections.emptyList());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> taskService.getTasks());

        assertEquals("No Tasks found.", exception.getMessage());
        verify(taskRepository).findTop5ByOrderByTaskDateAsc();
        verifyNoInteractions(taskMapper);
    }

    @Test
    void testDeleteTask_NotFound() {
        when(taskRepository.getReferenceById(1)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> taskService.deleteTask(1));
    }

    @Test
    void testDeleteTask_Success() {
        TaskDetailsDTO expectedDto = new TaskDetailsDTO(VALID_TASK_ID, "Sample Task", LocalDate.now(),LocalTime.now(),"Sample Description");

        when(taskRepository.getReferenceById(VALID_TASK_ID)).thenReturn(mockTask);
        when(taskMapper.taskToTaskDetailsDTO(mockTask)).thenReturn(expectedDto);

        TaskDetailsDTO result = taskService.deleteTask(VALID_TASK_ID);

        verify(taskRepository).delete(mockTask);
        assertThat(result).isEqualTo(expectedDto);
    }

    @Test
    void testUpdateTask_NotFound() {
        when(taskRepository.getReferenceById(1)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> taskService.updateTask(new AddTaskDTO(), 1));
    }

    @Test
    void testUpdateTask_Success() {
        TaskDetailsDTO expectedDto = new TaskDetailsDTO(VALID_TASK_ID, "Sample Task", LocalDate.now(),LocalTime.now(),"Sample Description");

        when(taskRepository.getReferenceById(VALID_TASK_ID)).thenReturn(mockTask);
        when(taskMapper.taskToTaskDetailsDTO(mockTask)).thenReturn(expectedDto);

        TaskDetailsDTO result = taskService.updateTask(SAMPLE_DTO, VALID_TASK_ID);

        assertThat(mockTask.getTaskTitle()).isEqualTo(SAMPLE_DTO.getTaskTitle());
        assertThat(mockTask.getDescription()).isEqualTo(SAMPLE_DTO.getDescription());
        verify(taskRepository).save(mockTask);
        assertThat(result).isEqualTo(expectedDto);
    }

    @Test
    void getTask_WhenTaskExists_ShouldReturnDto() {
        TaskDetailsDTO expectedDto = new TaskDetailsDTO(VALID_TASK_ID, "Sample Task", LocalDate.now(),LocalTime.now(),"Sample Description");

        when(taskRepository.getReferenceById(VALID_TASK_ID)).thenReturn(mockTask);
        when(taskMapper.taskToTaskDetailsDTO(mockTask)).thenReturn(expectedDto);

        TaskDetailsDTO result = taskService.getTask(VALID_TASK_ID);

        verify(taskMapper).taskToTaskDetailsDTO(mockTask);
        assertThat(result).isEqualTo(expectedDto);
    }

    @Test
    void getTask_WhenTaskNotFound_ShouldThrowException() {
        when(taskRepository.getReferenceById(INVALID_TASK_ID)).thenReturn(null);

        assertThrows(NotFoundException.class,
                () -> taskService.getTask(INVALID_TASK_ID)
        );
    }
}
