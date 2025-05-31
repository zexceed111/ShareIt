package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentShortDto;
import ru.practicum.shareit.item.model.Comment;

public class CommentMapper {

    public static Comment toComment(CommentCreateDto commentCreateDto) {
          return Comment.builder()
                  .text(commentCreateDto.getText())
                  .build();
    }

    public static CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .item(comment.getItem())
                .authorName(comment.getAuthor().getName())
                .created(true)
                .build();
    }

    public static CommentShortDto toShortDto(Comment comment) {
        return CommentShortDto.builder()
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .build();
    }
}
