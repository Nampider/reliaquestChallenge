package com.reliaquest.api.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleRateLimitException() {
        RateLimitException ex = new RateLimitException("Something went wrong");
        ResponseEntity<Object> response = globalExceptionHandler.handleRateLimitException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
    }

    @Test
    void handleEmployeeNotFoundException() {
        EmployeeNotFoundException ex = new EmployeeNotFoundException("Employee Not Found");
        ResponseEntity<Object> response = globalExceptionHandler.handleEmployeeNotFoundException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void handleInternalServerException() {
        InternalServerException ex = new InternalServerException("Internal Error");
        ResponseEntity<Object> response = globalExceptionHandler.handleInternalServerException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
