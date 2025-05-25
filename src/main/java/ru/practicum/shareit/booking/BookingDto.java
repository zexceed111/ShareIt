package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.item.ItemDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ItemDto item;
    private UserDto booker;
    private BookingStatus status;
}
