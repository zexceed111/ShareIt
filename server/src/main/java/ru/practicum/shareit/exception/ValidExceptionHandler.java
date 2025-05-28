package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class ValidExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalException(final Exception e) {
        log.error("An exception has occurred.");
        return new ErrorResponse("Internal Server Exception: ", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NoSuchElementException e) {
        log.error("An exception has occurred.");
        return new ErrorResponse("NotFound Exception: ", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleIllegalArgumentException(final IllegalArgumentException e) {
        log.error("An exception has occurred.");
        return new ErrorResponse("Illegal Argument Exception: ", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleRuntimeException(final RuntimeException e) {
        log.error("An exception has occurred.");
        return new ErrorResponse("Runtime Exception: ", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleConflict(final DataIntegrityViolationException e) {
        log.error("An exception has occurred.");
        return new ErrorResponse("Data Integrity Violation Exception: ", e.getMessage());
    }
}
