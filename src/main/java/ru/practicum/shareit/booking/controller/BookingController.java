package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto add(@RequestBody @Valid NewBookingRequest request, @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.add(request, userId);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto updateStatus(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long bookingId, @RequestParam boolean approved) {
        return bookingService.updateStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto findById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long bookingId) {
        return bookingService.findById(userId, bookingId);
    }

    /**
     * GET /bookings?state=ALL&page=0&size=20&sort=start,desc
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> findByBooker(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(defaultValue = "ALL") State state, @PageableDefault(size = 20, sort = "start", direction = Sort.Direction.DESC) Pageable pageable) {
        return bookingService.findByBooker(userId, state, pageable).getContent();
    }

    /**
     * GET /bookings/owner?state=ALL&page=0&size=20&sort=start,desc
     */
    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> findByOwner(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(defaultValue = "ALL") State state, @PageableDefault(size = 20, sort = "start", direction = Sort.Direction.DESC) Pageable pageable) {
        return bookingService.findByOwner(userId, state, pageable).getContent();
    }
}