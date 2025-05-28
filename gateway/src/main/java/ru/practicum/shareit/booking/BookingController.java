package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingState;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
@Slf4j
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> addBooking(@Valid
                                             @RequestBody
                                             BookingCreateDto booking,
                                             @RequestHeader("X-Sharer-User-Id")
                                             @NotNull
                                             Long userId) {
        return bookingClient.postBooking(userId, booking);
    }

    @PatchMapping("{bookingId}")
    public ResponseEntity<Object> bookingConfirmation(@PathVariable
                                                          @NotNull
                                                          Long bookingId,
                                                          @RequestHeader("X-Sharer-User-Id")
                                                          @NotNull
                                                          Long userId,
                                                          @RequestParam
                                                          @NotNull
                                                          Boolean approved) {
        return bookingClient.confirmation(bookingId, userId, approved);
    }

    @GetMapping("{bookingId}")
    public ResponseEntity<Object> getBookingById(@PathVariable
                                                 @NotNull
                                                 Long bookingId,
                                                 @RequestHeader("X-Sharer-User-Id")
                                                 @NotNull
                                                 Long userId) {
        return bookingClient.getBooking(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsersBookings(@RequestParam(defaultValue = "ALL")
                                                      BookingState state,
                                                      @RequestHeader("X-Sharer-User-Id")
                                                      @NotNull
                                                      Long userId) {
        return bookingClient.getBookings(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingsForAllUsersItems(@RequestParam(defaultValue = "ALL")
                                                                           String state,
                                                                           @RequestHeader("X-Sharer-User-Id")
                                                                           @NotNull
                                                                           Long userId) {
        return bookingClient.getBookingsForAllUsersItems(userId, state);
    }
}
