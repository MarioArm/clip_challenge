package com.example.clip.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.PersistenceException;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({PersistenceException.class})
    public ResponseEntity<String> handlePersistenceException(Exception ex, WebRequest request) {
        log.error("Persistence error. ", ex);
        return new ResponseEntity<>(
                "Error handling your request", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.debug("Validation error for request", ex);
        String errorDescription = ex.getAllErrors().stream().map(error -> {
            DefaultMessageSourceResolvable firstArgument = (DefaultMessageSourceResolvable) Objects.requireNonNull(error.getArguments())[0];
            return String.format("Field: [%s], Error: [%s]%n", firstArgument.getDefaultMessage(), error.getDefaultMessage());
        }).collect(Collectors.joining());
        return new ResponseEntity<>(errorDescription, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}