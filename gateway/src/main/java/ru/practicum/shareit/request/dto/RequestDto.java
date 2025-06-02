package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemForRequestDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class RequestDto {
    long id;
    String description;
    User requestor;
    LocalDateTime created;
    List<ItemForRequestDto> items;
}
