package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestShortDto;

import java.time.LocalDateTime;

public class RequestMapper {
    public static Request toModel(RequestCreateDto requestCreateDto) {
        return Request.builder()
                .description(requestCreateDto.getDescription())
                .created(LocalDateTime.now())
                .build();
    }

    public static RequestShortDto toShortDto(Request request) {
        return RequestShortDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .build();
    }

    public static RequestDto toDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .items(request.getResponsesList().stream().map(ItemMapper::itemForRequestDto).toList())
                .build();
    }
}
