package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RequestShortDto {
    long id;
    String description;
    LocalDateTime created;
}
