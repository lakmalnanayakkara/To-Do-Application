package com.ToDoTaskApp.backend.advisor;

import com.ToDoTaskApp.backend.dto.StandardResponse;
import com.ToDoTaskApp.backend.exception.AlreadyExistException;
import com.ToDoTaskApp.backend.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppWideExceptionHandler {
    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<StandardResponse> handleAlreadyExistException(Exception e) {
        ResponseEntity<StandardResponse> response = new ResponseEntity<>(
                new StandardResponse(500,"ERROR",e.getMessage()),
                HttpStatus.CONFLICT
        );
        return response;
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<StandardResponse> handleNotFoundException(Exception e) {
        ResponseEntity<StandardResponse> response = new ResponseEntity<>(
                new StandardResponse(500,"ERROR",e.getMessage()),
                HttpStatus.NOT_FOUND
        );
        return response;
    }
}
