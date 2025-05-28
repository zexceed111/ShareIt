package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.mapper.BookingDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NoAccessException;
import ru.practicum.shareit.exception.NotAvailableItemException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ParameterNotValidException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, ItemRepository itemRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public BookingDto add(NewBookingRequest request, long bookerId) {
        log.info("Создаём бронирование, itemId={}, bookerId={}", request.getItemId(), bookerId);
        User booker = userRepository.findById(bookerId).orElseThrow(() -> new NotFoundException("User not found"));
        Item item = itemRepository.findById(request.getItemId()).orElseThrow(() -> new NotFoundException("Item not found"));
        if (!item.getAvailable()) {
            throw new NotAvailableItemException("Item is not available");
        }
        Booking booking = BookingDtoMapper.mapToBookingAdd(request, booker, item);
        bookingRepository.save(booking);
        return BookingDtoMapper.mapToBookingDto(booking);
    }

    @Override
    @Transactional
    public BookingDto updateStatus(long userId, long id, boolean approved) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new NotFoundException("Booking not found"));
        if (booking.getItem().getOwner().getId() != userId) {
            throw new NoAccessException("Only owner can change status");
        }
        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        return BookingDtoMapper.mapToBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto findById(long userId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Booking not found"));
        long ownerId = booking.getItem().getOwner().getId();
        if (booking.getBooker().getId() != userId && ownerId != userId) {
            throw new NoAccessException("No access to booking");
        }
        return BookingDtoMapper.mapToBookingDto(booking);
    }

    @Override
    public Page<BookingDto> findByBooker(long bookerId, State state, Pageable pageable) {
        // проверяем, что пользователь существует
        userRepository.findById(bookerId).orElseThrow(() -> new NotFoundException("User not found"));
        LocalDateTime now = LocalDateTime.now();
        Page<Booking> page = switch (state) {
            case CURRENT -> bookingRepository.findCurrentByBooker(bookerId, now, pageable);
            case PAST -> bookingRepository.findByBooker_IdAndEndBefore(bookerId, now, pageable);
            case FUTURE -> bookingRepository.findByBooker_IdAndStartAfter(bookerId, now, pageable);
            case WAITING -> bookingRepository.findByBooker_IdAndStatus(bookerId, Status.WAITING, pageable);
            case REJECTED -> bookingRepository.findByBooker_IdAndStatus(bookerId, Status.REJECTED, pageable);
            case ALL -> bookingRepository.findByBooker_Id(bookerId, pageable);
            default -> throw new ParameterNotValidException("Unknown state: " + state);
        };
        return page.map(BookingDtoMapper::mapToBookingDto);
    }

    @Override
    public Page<BookingDto> findByOwner(long ownerId, State state, Pageable pageable) {
        // проверяем, что пользователь существует
        userRepository.findById(ownerId).orElseThrow(() -> new NotFoundException("User not found"));
        LocalDateTime now = LocalDateTime.now();
        Page<Booking> page = switch (state) {
            case CURRENT -> bookingRepository.findCurrentByOwner(ownerId, now, pageable);
            case PAST -> bookingRepository.findByItemOwner_IdAndEndBefore(ownerId, now, pageable);
            case FUTURE -> bookingRepository.findByItemOwner_IdAndStartAfter(ownerId, now, pageable);
            case WAITING -> bookingRepository.findByItemOwner_IdAndStatus(ownerId, Status.WAITING, pageable);
            case REJECTED -> bookingRepository.findByItemOwner_IdAndStatus(ownerId, Status.REJECTED, pageable);
            case ALL -> bookingRepository.findByItemOwner_Id(ownerId, pageable);
            default -> throw new ParameterNotValidException("Unknown state: " + state);
        };
        return page.map(BookingDtoMapper::mapToBookingDto);
    }
}