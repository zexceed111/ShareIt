package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.Request;

public class ItemMapper {
    public static Item toItem(ItemCreateDto itemCreateDto) {
        Request request = itemCreateDto.getRequestId() == null ? null : Request.builder().id(itemCreateDto.getRequestId()).build();
        return Item.builder()
                .name(itemCreateDto.getName())
                .owner(itemCreateDto.getOwner())
                .description(itemCreateDto.getDescription())
                .available(itemCreateDto.getAvailable())
                .request(request)
                .build();
    }


    public static Item toItem(ItemUpdateDto itemUpdateDto) {
        return Item.builder()
                .name(itemUpdateDto.getName())
                .description(itemUpdateDto.getDescription())
                .available(itemUpdateDto.getAvailable())
                .build();
    }

    public static ItemDto toDto(Item item) {
    Long requestId = item.getRequest() == null ? null : item.getRequest().getId();
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .owner(item.getOwner())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(item.getLastBooking())
                .nextBooking(item.getNextBooking())
                .requestId(requestId)
                .comments(item.getItemComments().stream().map(CommentMapper::toShortDto).toList())
                .build();
    }


    public static ItemForRequestDto itemForRequestDto(Item item) {
        return ItemForRequestDto.builder()
                .id(item.getId())
                .name(item.getName())
                .ownerId(item.getOwner().getId())
                .build();
    }
}
