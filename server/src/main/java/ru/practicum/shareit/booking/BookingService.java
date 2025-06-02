package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BookingDto createBooking(BookingCreateDto createBooking, long userId) {
        User user = checkAndGetUserById(userId);
        Item item = checkAndGetItemById(createBooking.getItemId());
        itemIsAvailable(item);
        Booking booking = BookingMapper.toBooking(createBooking);
        booking.setItem(item);
        booking.setBooker(user);
        checkBookingIntersection(booking);

        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    public BookingDto bookingConfirmation(long bookingId, long userId, boolean approved) {
        Booking booking = checkBookingAndGetById(bookingId);
        userIsOwner(booking.getItem(), userId);
        checkAndGetUserById(userId);

        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new IllegalArgumentException("The booking status must be WAITING.");
        }

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
            return BookingMapper.toDto(bookingRepository.save(booking));
        } else {
            booking.setStatus(BookingStatus.REJECTED);
            return BookingMapper.toDto(bookingRepository.save(booking));
        }
    }

    public BookingDto getBookingById(long bookingId, long userId) {
        Booking booking =  checkBookingAndGetById(bookingId);
        if (booking.getBooker().getId() == userId || booking.getItem().getOwner().getId() == userId) {
            return BookingMapper.toDto(booking);
        } else throw new IllegalArgumentException("A request can only be made by the owner of the item or the booking.");
    }


    public List<BookingDto> getAllUsersBookings(String state, long booker) {
        checkAndGetUserById(booker);
        switch (checkAndGetState(state)) {
            case ALL:
                return bookingRepository.findAllByBookerId(booker).stream()
                        .map(BookingMapper::toDto)
                        .toList();
            case PAST:
                return bookingRepository.findAllByBookerIdAndEndingBefore(booker,LocalDateTime.now()).stream()
                        .map(BookingMapper::toDto)
                        .toList();
            case FUTURE:
                return bookingRepository.findAllByBookerIdAndStartAfter(booker, LocalDateTime.now()).stream()
                        .map(BookingMapper::toDto)
                        .toList();
            case CURRENT:
                return bookingRepository.findAllByBookerIdCurrentBooking(booker, LocalDateTime.now()).stream()
                        .map(BookingMapper::toDto)
                        .toList();
            case WAITING:
                return bookingRepository.findAllByBookerIdAndStatus(booker, BookingStatus.WAITING).stream()
                        .map(BookingMapper::toDto)
                        .toList();
            case REJECTED:
                return bookingRepository.findAllByBookerIdAndStatus(booker, BookingStatus.REJECTED).stream()
                        .map(BookingMapper::toDto)
                        .toList();
            default:
                throw new IllegalArgumentException("This state does not exist.");
        }
    }

    public List<BookingDto> getAllBookingsForAllUsersItems(String state, long owner) {

        checkAndGetUserById(owner);
        if (itemRepository.findAllByOwnerId(owner).isEmpty()) {
            return List.of();
        }

        switch (checkAndGetState(state)) {
            case ALL:
                return bookingRepository.findAllByItemsOwnerId(owner).stream()
                        .map(BookingMapper::toDto)
                        .toList();
            case PAST:
                return bookingRepository.findAllByItemsOwnerIdAndEndingBefore(owner, LocalDateTime.now()).stream()
                        .map(BookingMapper::toDto)
                        .toList();
            case FUTURE:
                return bookingRepository.findAllByItemsOwnerIdAndStartAfter(owner, LocalDateTime.now()).stream()
                        .map(BookingMapper::toDto)
                        .toList();
            case CURRENT:
                return bookingRepository.findAllByItemsOwnerIdCurrentBooking(owner, LocalDateTime.now()).stream()
                        .map(BookingMapper::toDto)
                        .toList();
            case WAITING:
                return bookingRepository.findAllByItemsOwnerIdAndStatus(owner, BookingStatus.WAITING).stream()
                        .map(BookingMapper::toDto)
                        .toList();
            case REJECTED:
                return bookingRepository.findAllByItemsOwnerIdAndStatus(owner, BookingStatus.REJECTED).stream()
                        .map(BookingMapper::toDto)
                        .toList();
            default:
                throw new IllegalArgumentException("This state does not exist.");
        }
    }

    private Booking checkBookingAndGetById(long bookingId) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isEmpty()) {
            throw new NoSuchElementException("Booking with id = " + bookingId + " not found.");
        }
        return bookingOptional.get();
    }

    private BookingState checkAndGetState(String state) {
        for (BookingState bookingState : BookingState.values()) {
            if (bookingState.name().equalsIgnoreCase(state)) {
                return BookingState.valueOf(state);
            }
        }
          throw new IllegalArgumentException("This state does not exist.");
    }

    private User checkAndGetUserById(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NoSuchElementException("User with id = " + userId + " not found.");
        }
        return userOptional.get();
    }

    private void checkBookingIntersection(Booking booking) {
        LocalDateTime startTime = booking.getStart();
        LocalDateTime endTime = booking.getEnding();
        long itemId = booking.getItem().getId();

        if (bookingRepository.findCountBookingsForItem(itemId, startTime, endTime) > 0) {
            throw new IllegalArgumentException("The time of this booking overlaps with an existing booking.");
        }
    }

    private Item checkAndGetItemById(long itemId) {
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (itemOptional.isEmpty()) {
            throw new NoSuchElementException("Item with id = " + itemId + " not found.");
        }
        return itemOptional.get();
    }

    private void itemIsAvailable(Item item) {
        if (!item.getAvailable()) {
            throw new RuntimeException("This item is not available.");
        }
    }

    private void userIsOwner(Item item, long userId) {
        if (item.getOwner().getId() != userId) {
            throw new RuntimeException("User with id " + userId + " is not owner item with id " + item.getId());
        }
    }



}
