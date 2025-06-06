package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {
    private final BookingService bookingService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private BookingCreateDto bookingCreateDto;

    @Test
    void createBookingTest() {
        bookingCreateDto = new BookingCreateDto(LocalDateTime.of(2026, 4, 5, 0, 0, 0),
                LocalDateTime.of(2026, 4, 7, 0, 0, 0), 1L);

        BookingDto bookingDto = bookingService.createBooking(bookingCreateDto, 2);

        Assertions.assertNotNull(bookingDto);
        Assertions.assertEquals(6, bookingDto.getId());
        Assertions.assertEquals(1, bookingDto.getItem().getId());
        Assertions.assertEquals(2, bookingDto.getBooker().getId());
        Assertions.assertEquals(BookingStatus.WAITING, bookingDto.getStatus());
    }

    @Test
    void createBookingWithWrongBookerIdTest() {
        bookingCreateDto = new BookingCreateDto(LocalDateTime.of(2026, 4, 5, 0, 0, 0),
                LocalDateTime.of(2026, 4, 7, 0, 0, 0), 1L);

        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> bookingService.createBooking(bookingCreateDto, 1000L)
        );

        Assertions.assertEquals(e.getMessage(), "User with id = 1000 not found.");
    }

    @Test
    void createBookingWithWrongItemIdTest() {
        bookingCreateDto = new BookingCreateDto(LocalDateTime.of(2026, 4, 5, 0, 0, 0),
                LocalDateTime.of(2026, 4, 7, 0, 0, 0), 1000L);

        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> bookingService.createBooking(bookingCreateDto, 1L)
        );

        Assertions.assertEquals(e.getMessage(), "Item with id = 1000 not found.");
    }

    @Test
    void createBookingWithIntersectionsTest() {
        bookingCreateDto = new BookingCreateDto(LocalDateTime.of(2024, 6, 5, 0, 0, 0),
                LocalDateTime.of(2024, 6, 7, 0, 0, 0), 1L);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> bookingService.createBooking(bookingCreateDto, 2L)
        );

        Assertions.assertEquals(e.getMessage(), "The time of this booking overlaps with an existing booking.");
    }

    @Test
    void createBookingWithNotAvailableItemTest() {
        bookingCreateDto = new BookingCreateDto(LocalDateTime.of(2026, 4, 5, 0, 0, 0),
                LocalDateTime.of(2026, 4, 7, 0, 0, 0), 4L);

        RuntimeException e = assertThrows(RuntimeException.class,
                () -> bookingService.createBooking(bookingCreateDto, 2)
        );

        Assertions.assertEquals(e.getMessage(), "This item is not available.");

    }

    @Test
    void bookingConfirmationTest() {
        BookingDto bookingDto = bookingService.bookingConfirmation(4, 3, true);

        Assertions.assertNotNull(bookingDto);
        Assertions.assertEquals(BookingStatus.APPROVED, bookingDto.getStatus());
    }

    @Test
    void bookingConfirmationWithWrongStatusTest() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> bookingService.bookingConfirmation(1, 1, true)
        );

        Assertions.assertEquals(e.getMessage(), "The booking status must be WAITING.");
    }

    @Test
    void bookingConfirmationWhereUserIsNotUserTest() {
        RuntimeException e = assertThrows(RuntimeException.class,
                () -> bookingService.bookingConfirmation(1, 3, true)
        );

        Assertions.assertEquals(e.getMessage(), "User with id 3 is not owner item with id 1");

    }

    @Test
    void getBookingByIdTest() {
        BookingDto bookingDto = bookingService.getBookingById(1, 2);
        BookingDto bookingDto1 = bookingService.getBookingById(3, 3);

        Assertions.assertNotNull(bookingDto);
        Assertions.assertEquals(BookingStatus.APPROVED, bookingDto.getStatus());
        Assertions.assertNotNull(bookingDto1);
        Assertions.assertEquals(BookingStatus.APPROVED, bookingDto.getStatus());
    }

    @Test
    void getBookingByWrongIdTest() {
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> bookingService.getBookingById(1000, 2)
        );

        Assertions.assertEquals(e.getMessage(), "Booking with id = 1000 not found.");
    }

    @Test
    void getBookingByIdWithWrongUserIdTest() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> bookingService.getBookingById(4, 1000)
        );

        Assertions.assertEquals(e.getMessage(), "A request can only be made by the owner of the item or the booking.");
    }

    @Test
    void getAllUsersBookingsTest() {
        List<BookingDto> bookingDtoList = bookingService.getAllUsersBookings("ALL", 2);

        Assertions.assertNotNull(bookingDtoList);
        Assertions.assertEquals(4, bookingDtoList.size());

        List<BookingDto> bookingDtoList1 = bookingService.getAllUsersBookings("PAST", 2);

        Assertions.assertNotNull(bookingDtoList1);
        Assertions.assertEquals(3, bookingDtoList1.size());

        List<BookingDto> bookingDtoList2 = bookingService.getAllUsersBookings("FUTURE", 2);

        Assertions.assertNotNull(bookingDtoList2);
        Assertions.assertEquals(0, bookingDtoList2.size());

        List<BookingDto> bookingDtoList3 = bookingService.getAllUsersBookings("CURRENT", 2);

        Assertions.assertNotNull(bookingDtoList3);
        Assertions.assertEquals(1, bookingDtoList3.size());

        List<BookingDto> bookingDtoList4 = bookingService.getAllUsersBookings("WAITING", 1);

        Assertions.assertNotNull(bookingDtoList3);
        Assertions.assertEquals(1, bookingDtoList4.size());

        List<BookingDto> bookingDtoList5 = bookingService.getAllUsersBookings("REJECTED", 2);

        Assertions.assertNotNull(bookingDtoList3);
        Assertions.assertEquals(1, bookingDtoList5.size());
    }

    @Test
    void getAllUsersBookingsWithWrongStateTest() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> bookingService.getAllUsersBookings("lkjlkj", 2)
        );

        Assertions.assertEquals(e.getMessage(), "This state does not exist.");
    }

    @Test
    void getAllBookingsForAllUsersItemsTest() {
        List<BookingDto> bookingDtoList = bookingService.getAllBookingsForAllUsersItems("ALL", 3);

        Assertions.assertNotNull(bookingDtoList);
        Assertions.assertEquals(4, bookingDtoList.size());

        List<BookingDto> bookingDtoList1 = bookingService.getAllBookingsForAllUsersItems("PAST", 3);

        Assertions.assertNotNull(bookingDtoList1);
        Assertions.assertEquals(2, bookingDtoList1.size());

        List<BookingDto> bookingDtoList2 = bookingService.getAllBookingsForAllUsersItems("FUTURE", 3);

        Assertions.assertNotNull(bookingDtoList2);
        Assertions.assertEquals(1, bookingDtoList2.size());

        List<BookingDto> bookingDtoList3 = bookingService.getAllBookingsForAllUsersItems("CURRENT", 3);

        Assertions.assertNotNull(bookingDtoList3);
        Assertions.assertEquals(1, bookingDtoList3.size());

        List<BookingDto> bookingDtoList4 = bookingService.getAllBookingsForAllUsersItems("WAITING", 3);

        Assertions.assertNotNull(bookingDtoList3);
        Assertions.assertEquals(1, bookingDtoList4.size());

        List<BookingDto> bookingDtoList5 = bookingService.getAllBookingsForAllUsersItems("REJECTED", 3);

        Assertions.assertNotNull(bookingDtoList3);
        Assertions.assertEquals(1, bookingDtoList5.size());
    }

    @Test
    void getAllBookingsForAllUsersItemsWithWrongStatusTest() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> bookingService.getAllBookingsForAllUsersItems("lkjlkj", 3)
        );

        Assertions.assertEquals(e.getMessage(), "This state does not exist.");
    }

}
