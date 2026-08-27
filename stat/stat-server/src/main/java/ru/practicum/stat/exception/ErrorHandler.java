package ru.practicum.stat.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    private ApiError buildApiError(String error, String message, HttpStatus status, HttpServletRequest request) {
        return new ApiError(
                error,
                message,
                status.name(),
                LocalDateTime.now(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValid(final MethodArgumentNotValidException e, HttpServletRequest request) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining(", "));

        log.error("Ошибка валидации: {}", errorMessage);
        return buildApiError("Ошибка валидации", errorMessage, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleIllegalArgument(final IllegalArgumentException e, HttpServletRequest request) {
        log.error("Ошибка валидации: {}", e.getMessage());
        return buildApiError("Ошибка валидации", e.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingParams(final MissingServletRequestParameterException e, HttpServletRequest request) {
        log.error("Пропущен обязательный параметр: {}", e.getMessage());
        return buildApiError("Пропущен обязательный параметр", e.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMismatch(final MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        log.error("Несовпадение типов: {}", e.getMessage());
        return buildApiError("Неверный формат параметра", e.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleConstraintViolation(final ConstraintViolationException e, HttpServletRequest request) {
        String errorMessage = e.getConstraintViolations().stream()
                .map(violation -> String.format("%s: %s",
                        violation.getPropertyPath().toString(),
                        violation.getMessage()))
                .collect(Collectors.joining(", "));

        log.error("Ошибка валидации: {}", errorMessage);
        return buildApiError("Ошибка валидации", errorMessage, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleAllExceptions(final Exception e, HttpServletRequest request) {
        log.error("Непредвиденная ошибка: {}", e.getMessage(), e);
        return buildApiError("Внутренняя ошибка сервера", "Произошла непредвиденная ошибка",
                HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}