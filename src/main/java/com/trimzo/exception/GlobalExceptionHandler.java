package com.trimzo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 — Validation failed
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fe.getField(), fe.getDefaultMessage());
        }

        return buildError(HttpStatus.BAD_REQUEST,
                "Validation failed", fieldErrors, request);
    }

    // 401 — Wrong email or password
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredentials(
            InvalidCredentialsException ex,
            HttpServletRequest request) {

        return buildError(HttpStatus.UNAUTHORIZED,
                ex.getMessage(), null, request);
    }

    // 403 — URL inactive or access denied
    @ExceptionHandler({UrlInactiveException.class,
            AccessDeniedException.class})
    public ResponseEntity<Map<String, Object>> handleForbidden(
            RuntimeException ex,
            HttpServletRequest request) {

        return buildError(HttpStatus.FORBIDDEN,
                ex.getMessage(), null, request);
    }

    // 404 — URL or user not found
    @ExceptionHandler({UrlNotFoundException.class,
            UserNotFoundException.class})
    public ResponseEntity<Map<String, Object>> handleNotFound(
            RuntimeException ex,
            HttpServletRequest request) {

        return buildError(HttpStatus.NOT_FOUND,
                ex.getMessage(), null, request);
    }

    // 409 — Duplicate email or conflict
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleConflict(
            RuntimeException ex,
            HttpServletRequest request) {

        return buildError(HttpStatus.CONFLICT,
                ex.getMessage(), null, request);
    }

    // 410 — URL expired
    @ExceptionHandler(UrlExpiredException.class)
    public ResponseEntity<Map<String, Object>> handleExpired(
            UrlExpiredException ex,
            HttpServletRequest request) {

        return buildError(HttpStatus.GONE,
                ex.getMessage(), null, request);
    }

    private ResponseEntity<Map<String, Object>> buildError(
            HttpStatus status,
            String message,
            Object details,
            HttpServletRequest request) {

        Map<String, Object> error = new HashMap<>();
        error.put("status", status.value());
        error.put("error", status.getReasonPhrase());
        error.put("message", message);
        error.put("timestamp", LocalDateTime.now());
        error.put("path", request.getRequestURI());

        if (details != null) {
            error.put("details", details);
        }

        return ResponseEntity.status(status).body(error);
    }
}