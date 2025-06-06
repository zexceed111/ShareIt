package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemUpdateDto {
    String name;
    String description;
    Boolean available;
}
