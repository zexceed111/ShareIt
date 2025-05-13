package ru.yandex.practicum.booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    BookingDto create(BookingDto bookingDto, Long userId);

    BookingDto getBookingById(Long bookingId);

    List<BookingDto> getUserBookings(Long userId);

    List<BookingDto> getItemBookings(Long itemId);

    BookingDto updateBookingStatus(Long bookingId, BookingStatus newStatus);

    void cancelBooking(Long bookingId);

    boolean checkDateOverlap(LocalDateTime start, LocalDateTime end, Long itemId);
}
