package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@DataJpaTest
public class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;

    private List<Booking> bookingDataList;


    @Test
    void findAllByBookerIdTest() {
        bookingDataList = bookingRepository.findAllByBookerId(2);

        Assertions.assertNotNull(bookingDataList);
        Assertions.assertEquals(4, bookingDataList.size());
    }

    @Test
    void findAllByBookerIdAndEndingBeforeTest() {
        bookingDataList = bookingRepository.findAllByBookerIdAndEndingBefore(2, LocalDateTime.now());

        Assertions.assertNotNull(bookingDataList);
        Assertions.assertEquals(3, bookingDataList.size());
    }

    @Test
    void  findAllByBookerIdAndStartAfterTest() {
        bookingDataList = bookingRepository.findAllByBookerIdAndStartAfter(2, LocalDateTime.now());

        Assertions.assertNotNull(bookingDataList);
        Assertions.assertEquals(0, bookingDataList.size());
    }

    @Test
    void  findAllByBookerIdCurrentBookingTest() {
        bookingDataList = bookingRepository.findAllByBookerIdCurrentBooking(2, LocalDateTime.now());

        Assertions.assertNotNull(bookingDataList);
        Assertions.assertEquals(1, bookingDataList.size());
    }

    @Test
    void  findAllByBookerIdAndStatusTest() {
        bookingDataList = bookingRepository.findAllByBookerIdAndStatus(2, BookingStatus.WAITING);

        Assertions.assertTrue(bookingDataList.isEmpty());
    }

    @Test
    void  findCountBookingsForItemTest() {
        Long count = bookingRepository.findCountBookingsForItem(3,
                LocalDateTime.of(2025, 5, 7, 0, 0, 0),
                LocalDateTime.of(2025, 5, 8, 0, 0, 0));

        Assertions.assertNotNull(count);
        Assertions.assertEquals(1, count);
    }

    @Test
    void findAllByOwnerIdTest() {
        bookingDataList = bookingRepository.findAllByItemsOwnerId(2);

        Assertions.assertNotNull(bookingDataList);
        Assertions.assertEquals(0, bookingDataList.size());
    }

    @Test
    void findAllByItemsOwnerIdAndEndingBeforeTest() {
        bookingDataList = bookingRepository.findPastBookingsByOwner(3,
                LocalDateTime.of(2025, 6, 8, 0, 0, 0));

        Assertions.assertNotNull(bookingDataList);
        Assertions.assertEquals(3, bookingDataList.size());
    }

    @Test
    void findAllByItemsOwnerIdAndStartAfterTest() {
        bookingDataList = bookingRepository.findAllByItemsOwnerIdAndStartAfter(2,
                LocalDateTime.of(2025, 6, 8, 0, 0, 0));

        Assertions.assertTrue(bookingDataList.isEmpty());

        bookingDataList = bookingRepository.findAllByItemsOwnerIdAndStartAfter(1,
                LocalDateTime.of(2023, 3, 5, 0, 0, 0));

        Assertions.assertNotNull(bookingDataList);
        Assertions.assertEquals(1, bookingDataList.size());
    }

    @Test
    void findAllByItemsOwnerIdCurrentBookingTest() {
        bookingDataList = bookingRepository.findAllByItemsOwnerIdCurrentBooking(3, LocalDateTime.now());

        Assertions.assertNotNull(bookingDataList);
        Assertions.assertEquals(1, bookingDataList.size());
    }

    @Test
    void findAllByItemsOwnerIdAndStatusTest() {
        bookingDataList = bookingRepository.findAllByItemsOwnerIdAndStatus(2, BookingStatus.REJECTED);

        Assertions.assertNotNull(bookingDataList);
        Assertions.assertEquals(0, bookingDataList.size());
    }

    @Test
    void findByItemIdAndBookerIdAndStatusAndEndingBeforeTest() {
        Optional<Booking> booking = bookingRepository.findByItemIdAndBookerIdAndStatusAndEndingBefore(1, 2, BookingStatus.APPROVED,
                LocalDateTime.now());

        Assertions.assertNotNull(booking);
        Assertions.assertTrue(booking.isPresent());
    }

    @Test
    void findByItemIdInAndStatusOrderByStartDescTest() {
        List<Item> itemList = new ArrayList<>();
        Optional<Item> item1 = itemRepository.findById(2L);
        Optional<Item> item2 = itemRepository.findById(3L);
        itemList.add(item1.get());
        itemList.add(item2.get());
        Optional<Booking> booking = bookingRepository.findById(5L);

        bookingDataList = bookingRepository.findByItemIdInAndStatusOrderByStartDesc(itemList, BookingStatus.APPROVED);

        Assertions.assertNotNull(bookingDataList);
        Assertions.assertEquals(2, bookingDataList.size());
        Assertions.assertEquals(3, bookingDataList.get(1).getId());
    }
}
