package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.user.model.User;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemCreateDto {
    String name;
    String description;
    Boolean available;
    User owner;
    Long requestId;
}
