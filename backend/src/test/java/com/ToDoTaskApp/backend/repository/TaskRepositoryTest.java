package com.ToDoTaskApp.backend.repository;

import com.ToDoTaskApp.backend.entity.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
@Transactional
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    private Task task;


    @BeforeEach
    void setUp() {
        task = new Task();
        task.setTaskTitle("Test Task");
        task.setTaskDate(LocalDate.now().plusDays(1));
        task.setTaskTime(LocalTime.of(14, 30));
        task.setDescription("This is a test task.");
        task = taskRepository.save(task); // ID generated here
    }

    @Test
    void testSaveTask() {
        assertNotNull(task.getTaskId());
        assertEquals("Test Task", task.getTaskTitle());
    }
    @Test
    void testFindByTaskTitle_Found() {
        Task foundTask = taskRepository.findByTaskTitle("Test Task");
        assertNotNull(foundTask);
        assertEquals("Test Task", foundTask.getTaskTitle());
    }

    @Test
    void testFindByTaskTitle_NotFound() {
        Task foundTask = taskRepository.findByTaskTitle("Nonexistent Task");
        assertNull(foundTask);
    }

    @Test
    void testFindTop5ByOrderByTaskDateAsc() {
        List<Task> tasks = taskRepository.findTop5ByOrderByTaskDateAsc();
        assertFalse(tasks.isEmpty());
        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getTaskTitle());
    }

    @Test
    void testDeleteTask() {
        taskRepository.delete(task);
        Optional<Task> deletedTask = taskRepository.findById(task.getTaskId());
        assertTrue(deletedTask.isEmpty());
    }
}
