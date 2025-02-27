package com.ToDoTaskApp.backend.mappers;

import com.ToDoTaskApp.backend.dto.request.AddTaskDTO;
import com.ToDoTaskApp.backend.dto.response.TaskDetailsDTO;
import com.ToDoTaskApp.backend.entity.Task;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TaskMapperTest {

    private final TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);

    @Test
    void testAddTaskDtoToTask() {
        AddTaskDTO addTaskDTO = new AddTaskDTO();
        addTaskDTO.setTaskTitle("Test Task");
        addTaskDTO.setDescription("Task Description");
        addTaskDTO.setTaskDate(LocalDate.of(2025, 2, 27));
        addTaskDTO.setTaskTime(LocalTime.of(10, 30));

        Task task = taskMapper.addTaskDtoToTask(addTaskDTO);

        assertThat(task).isNotNull();
        assertThat(task.getTaskTitle()).isEqualTo(addTaskDTO.getTaskTitle());
        assertThat(task.getDescription()).isEqualTo(addTaskDTO.getDescription());
        assertThat(task.getTaskDate()).isEqualTo(addTaskDTO.getTaskDate());
        assertThat(task.getTaskTime()).isEqualTo(addTaskDTO.getTaskTime());
    }

    @Test
    void testAddTaskDtoToTask_WhenAddTaskDtoIsNull() {
        Task task = taskMapper.addTaskDtoToTask(null);
        assertThat(task).isNull();
    }

    @Test
    void testTaskToTaskDetailsDTO() {
        Task task = new Task();
        task.setTaskId(1);
        task.setTaskTitle("Sample Task");
        task.setDescription("Sample Description");
        task.setTaskDate(LocalDate.of(2025, 2, 28));
        task.setTaskTime(LocalTime.of(14, 0));

        TaskDetailsDTO taskDetailsDTO = taskMapper.taskToTaskDetailsDTO(task);

        assertThat(taskDetailsDTO).isNotNull();
        assertThat(taskDetailsDTO.getTaskId()).isEqualTo(task.getTaskId());
        assertThat(taskDetailsDTO.getTaskTitle()).isEqualTo(task.getTaskTitle());
        assertThat(taskDetailsDTO.getDescription()).isEqualTo(task.getDescription());
        assertThat(taskDetailsDTO.getTaskDate()).isEqualTo(task.getTaskDate());
        assertThat(taskDetailsDTO.getTaskTime()).isEqualTo(task.getTaskTime());
    }

    @Test
    void testTaskToTaskDetailsDTO_WhenTaskIsNull() {
        TaskDetailsDTO taskDetailsDTO = taskMapper.taskToTaskDetailsDTO(null);
        assertThat(taskDetailsDTO).isNull();
    }

    @Test
    void testTaskListToTaskDetailsDTOList() {
        Task task1 = new Task();
        task1.setTaskId(1);
        task1.setTaskTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setTaskDate(LocalDate.of(2025, 3, 1));
        task1.setTaskTime(LocalTime.of(9, 0));

        Task task2 = new Task();
        task2.setTaskId(2);
        task2.setTaskTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setTaskDate(LocalDate.of(2025, 3, 2));
        task2.setTaskTime(LocalTime.of(10, 0));

        List<Task> tasks = List.of(task1, task2);
        List<TaskDetailsDTO> taskDetailsDTOList = taskMapper.taskListToTaskDetailsDTOList(tasks);

        assertThat(taskDetailsDTOList).isNotNull();
        assertThat(taskDetailsDTOList).hasSize(2);

        assertThat(taskDetailsDTOList.get(0).getTaskId()).isEqualTo(task1.getTaskId());
        assertThat(taskDetailsDTOList.get(0).getTaskTitle()).isEqualTo(task1.getTaskTitle());

        assertThat(taskDetailsDTOList.get(1).getTaskId()).isEqualTo(task2.getTaskId());
        assertThat(taskDetailsDTOList.get(1).getTaskTitle()).isEqualTo(task2.getTaskTitle());
    }

    @Test
    void testTaskListToTaskDetailsDTOList_WhenTaskListIsNull() {
        List<TaskDetailsDTO> taskDetailsDTOList = taskMapper.taskListToTaskDetailsDTOList(null);
        assertThat(taskDetailsDTOList).isNull();
    }
}
