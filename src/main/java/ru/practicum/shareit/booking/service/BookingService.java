package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.model.State;

public interface BookingService {
    BookingDto add(NewBookingRequest request, long bookerId);
    BookingDto updateStatus(long userId, long id, boolean approved);
    BookingDto findById(long userId, long bookingId);
    Page<BookingDto> findByBooker(long bookerId, State state, Pageable pageable);
    Page<BookingDto> findByOwner(long ownerId, State state, Pageable pageable);
}