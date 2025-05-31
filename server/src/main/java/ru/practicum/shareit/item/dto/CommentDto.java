package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    long id;
    String text;
    Item item;
    String authorName;
    boolean created;
}
