package com.pragma.bootcamp.infrastructure.exceptionhandler;

import com.pragma.bootcamp.domain.exception.CapacityNotFoundException;
import com.pragma.bootcamp.domain.exception.CapacityServiceUnavailableException;
import com.pragma.bootcamp.domain.exception.DuplicateCapacityException;
import com.pragma.bootcamp.domain.exception.InvalidCapacityCountException;
import com.pragma.bootcamp.domain.exception.InvalidPaginationParameterException;
import com.pragma.bootcamp.infrastructure.exception.NoDataFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerAdvisor {

    private static final String MESSAGE = "message";

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoDataFoundException(
            NoDataFoundException ignoredNoDataFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.NO_DATA_FOUND.getMessage()));
    }

    @ExceptionHandler(InvalidCapacityCountException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCapacityCountException(
            InvalidCapacityCountException ignore) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.INVALID_NUMBER_OF_CAPACITIES_ASSOCIATED.getMessage()));
    }

    @ExceptionHandler(DuplicateCapacityException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateCapacityException(
            DuplicateCapacityException ignore) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.DUPLICATE_CAPACITY_ID.getMessage()));
    }

    @ExceptionHandler(CapacityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCapacityNotFoundException(
            CapacityNotFoundException ignore) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.CAPACITY_NOT_FOUND.getMessage()));
    }

    @ExceptionHandler(CapacityServiceUnavailableException.class)
    public ResponseEntity<Map<String, String>> handleCapacityServiceUnavailableException(
            CapacityServiceUnavailableException ignore) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.CAPACITY_SERVICE_UNAVAILABLE.getMessage()));
    }

    @ExceptionHandler(InvalidPaginationParameterException.class)
    public ResponseEntity<Map<String, String>> handleInvalidPaginationParameterException(
            InvalidPaginationParameterException ignore) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.INVALID_PAGINATION_PARAMETER.getMessage()));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(WebExchangeBindException exception) {
        Map<String, String> errors = exception.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() != null
                                ? fieldError.getDefaultMessage()
                                : ExceptionResponse.INVALID_REQUEST.getMessage(),
                        (existing, _) -> existing
                ));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
