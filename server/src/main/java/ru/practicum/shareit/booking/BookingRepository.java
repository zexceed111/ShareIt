package ru.practicum.shareit.booking;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerId(long userId);

    List<Booking> findAllByBookerIdAndEndingBefore(long userId, LocalDateTime now);

    List<Booking> findAllByBookerIdAndStartAfter(long userId, LocalDateTime now);

    @Query("SELECT b " +
            "FROM Booking AS b  " +
            "WHERE b.booker.id = :id " +
            "AND :now > b.start " +
            "AND :now < b.ending")
    List<Booking> findAllByBookerIdCurrentBooking(@Param("id") long userId, @Param("now") LocalDateTime now);

    List<Booking> findAllByBookerIdAndStatus(long userId, BookingStatus status);

    @Query("SELECT COUNT(*) " +
            "FROM Booking AS b " +
            "WHERE b.item.id = :itemId " +
            "AND b.status IN ('WAITING', 'APPROVED') " +
            "AND b.start < :end " +
            "AND b.ending > :start")
    long findCountBookingsForItem(long itemId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.item i " +
            "WHERE i.owner.id = :ownerId " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByItemsOwnerId(long ownerId);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.item i " +
            "WHERE i.owner.id = :userId " +
            "AND :now > b.ending " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByItemsOwnerIdAndEndingBefore(long userId, LocalDateTime now);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.item i " +
            "WHERE i.owner.id = :userId " +
            "AND :now < b.start " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByItemsOwnerIdAndStartAfter(long userId, LocalDateTime now);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.item i " +
            "WHERE i.owner.id = :userId " +
            "AND :now BETWEEN b.start AND b.ending " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByItemsOwnerIdCurrentBooking(long userId, LocalDateTime now);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.item i " +
            "WHERE i.owner.id = :userId " +
            "AND b.status = :status " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByItemsOwnerIdAndStatus(long userId, BookingStatus status);

    Optional<Booking> findByItemIdAndBookerIdAndStatusAndEndingBefore(long itemId, long bookerId, BookingStatus status, LocalDateTime now);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.item IN :itemsList " +
            "AND b.status = :status " +
            "ORDER BY b.start DESC")
    List<Booking> findByItemIdInAndStatusOrderByStartDesc(@Param("itemsList") List<Item> itemsList, @Param("status") BookingStatus status);

}
