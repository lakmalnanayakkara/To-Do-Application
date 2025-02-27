package com.ToDoTaskApp.backend.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AlreadyExistExceptionTest {
    @Test
    void testAlreadyExistException() {
        String errorMessage = "Task already exists";
        AlreadyExistException exception = new AlreadyExistException(errorMessage);

        assertThat(exception)
                .isInstanceOf(RuntimeException.class)
                .hasMessage(errorMessage);
    }
}
