package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {
   long id;
   String name;
   String description;
   Boolean available;
   User owner;
   @Builder.Default
   Booking lastBooking = null;
   @Builder.Default
   Booking nextBooking = null;
   @Builder.Default
   List<Comment> itemComments = new ArrayList<>();
   Request request;
}
