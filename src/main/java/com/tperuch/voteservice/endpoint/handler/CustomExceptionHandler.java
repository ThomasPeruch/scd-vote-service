package com.tperuch.voteservice.endpoint.handler;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

import static com.tperuch.voteservice.util.DateUtil.formatDate;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> methodArgumentNotValidException(MethodArgumentNotValidException exception){
        return new ResponseEntity<>(new ErrorDto(
                formatDate(LocalDateTime.now()),
                exception.getClass().getName(),
                HttpStatus.BAD_REQUEST.value(),
                List.of(exception.getMessage())),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> methodEntityNotFoundException(EntityNotFoundException exception){
        return new ResponseEntity<>(new ErrorDto(
                formatDate(LocalDateTime.now()),
                exception.getClass().getName(),
                HttpStatus.BAD_REQUEST.value(),
                List.of(exception.getMessage())),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> methodIllegalArgumentException(IllegalArgumentException exception){
        return new ResponseEntity<>(new ErrorDto(
                formatDate(LocalDateTime.now()),
                exception.getClass().getName(),
                HttpStatus.BAD_REQUEST.value(),
                List.of(exception.getMessage())),
                HttpStatus.BAD_REQUEST);
    }
}
