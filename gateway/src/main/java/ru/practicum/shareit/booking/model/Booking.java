package ru.practicum.shareit.booking.model;
import lombok.*;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    long id;
    LocalDateTime start;
    LocalDateTime ending;
    Item item;
    User booker;
    BookingStatus status;

}
