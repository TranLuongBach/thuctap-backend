package com.example.demo.expection;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // ===== CONSTANTS =====
    private static final String STATUS_KEY = "status";
    private static final String CODE_KEY = "code";
    private static final String MESSAGE_KEY = "message";
    private static final String ERRORS_KEY = "errors";

    private static final String STATUS_FAILED = "FAILED";
    private static final String VALIDATION_CODE = "VALIDATION_ERROR";
    private static final String INTERNAL_CODE = "INTERNAL_ERROR";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex
    ) {

        Map<String, String> validationErrors = extractValidationErrors(ex);

        Map<String, Object> response = buildResponse(
                VALIDATION_CODE,
                null,
                validationErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ApiException1.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(ApiException1 ex) {

        Map<String, Object> response = buildResponse(
                ex.getCode(),
                ex.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleUnexpectedException(Exception ex) {

        Map<String, Object> response = buildResponse(
                INTERNAL_CODE,
                ex.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    // ===== HELPER METHODS =====

    private Map<String, String> extractValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                );

        return errors;
    }

    private Map<String, Object> buildResponse(
            String code,
            String message,
            Map<String, ?> errors
    ) {
        Map<String, Object> response = new HashMap<>();
        response.put(STATUS_KEY, STATUS_FAILED);
        response.put(CODE_KEY, code);

        if (message != null) {
            response.put(MESSAGE_KEY, message);
        }

        if (errors != null) {
            response.put(ERRORS_KEY, errors);
        }

        return response;
    }
}
