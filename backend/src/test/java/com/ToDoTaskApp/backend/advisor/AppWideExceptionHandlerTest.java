package com.ToDoTaskApp.backend.advisor;

import com.ToDoTaskApp.backend.dto.StandardResponse;
import com.ToDoTaskApp.backend.exception.AlreadyExistException;
import com.ToDoTaskApp.backend.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AppWideExceptionHandlerTest {

    private AppWideExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new AppWideExceptionHandler();
    }

    @Test
    void testHandleAlreadyExistException() {
        AlreadyExistException exception = new AlreadyExistException("User already exists");

        ResponseEntity<StandardResponse> response = exceptionHandler.handleAlreadyExistException(exception);

        assertThat(response).isNotNull();
        assertEquals(409, response.getStatusCodeValue());
        assertEquals("ERROR", response.getBody().getMessage());
        assertEquals("User already exists", response.getBody().getData());
    }

    @Test
    void testHandleNotFoundException() {
        NotFoundException exception = new NotFoundException("Task not found");

        ResponseEntity<StandardResponse> response = exceptionHandler.handleNotFoundException(exception);

        assertThat(response).isNotNull();
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("ERROR", response.getBody().getMessage());
        assertEquals("Task not found", response.getBody().getData());
    }
}

