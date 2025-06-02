package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDto {
    long id;
    String name;
    String description;
    boolean available;
    User owner;
    String request;
    Booking lastBooking;
    Booking nextBooking;
    Long requestId;
    @Builder.Default
    List<CommentShortDto> comments = new ArrayList<>();
}
