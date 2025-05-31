package ru.practicum.shareit.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private final String error;
    private final String message;
}
