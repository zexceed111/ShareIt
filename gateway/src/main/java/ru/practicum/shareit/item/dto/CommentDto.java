package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;

@Data
@Builder
public class CommentDto {
    long id;
    String text;
    Item item;
    String authorName;
    boolean created;
}
