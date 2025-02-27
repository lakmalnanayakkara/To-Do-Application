package com.ToDoTaskApp.backend.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class NotFoundExceptionTest {
    @Test
    void testNotFoundException() {
        String errorMessage = "Task not found";
        NotFoundException exception = new NotFoundException(errorMessage);

        assertThat(exception)
                .isInstanceOf(RuntimeException.class)
                .hasMessage(errorMessage);
    }

}
