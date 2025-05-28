package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // старые методы для pageable...
    Page<Booking> findByBooker_Id(long bookerId, Pageable pageable);
    Page<Booking> findByItemOwner_Id(long ownerId, Pageable pageable);
    @Query("SELECT b FROM Booking b " +
            " WHERE b.booker.id = :bookerId " +
            "   AND b.start <= :now " +
            "   AND b.end >= :now " +
            "   AND b.status = ru.practicum.shareit.booking.model.Status.APPROVED")
    Page<Booking> findCurrentByBooker(@Param("bookerId") long bookerId,
                                      @Param("now") LocalDateTime now,
                                      Pageable pageable);
    @Query("SELECT b FROM Booking b " +
            " WHERE b.item.owner.id = :ownerId " +
            "   AND b.start <= :now " +
            "   AND b.end >= :now " +
            "   AND b.status = ru.practicum.shareit.booking.model.Status.APPROVED")
    Page<Booking> findCurrentByOwner(@Param("ownerId") long ownerId,
                                     @Param("now") LocalDateTime now,
                                     Pageable pageable);
    Page<Booking> findByBooker_IdAndEndBefore(long bookerId, LocalDateTime now, Pageable pageable);
    Page<Booking> findByItemOwner_IdAndEndBefore(long ownerId, LocalDateTime now, Pageable pageable);
    Page<Booking> findByBooker_IdAndStartAfter(long bookerId, LocalDateTime now, Pageable pageable);
    Page<Booking> findByItemOwner_IdAndStartAfter(long ownerId, LocalDateTime now, Pageable pageable);
    Page<Booking> findByBooker_IdAndStatus(long bookerId, Status status, Pageable pageable);
    Page<Booking> findByItemOwner_IdAndStatus(long ownerId, Status status, Pageable pageable);

    // для getById() — список по item и статусу
    List<Booking> findByItem_idAndStatus(long item_id, Status status);

    // для addComment() — одно бронирование по item и booker
    Booking    findByItem_idAndBooker_id(long item_id, long booker_id);

    // ↓↓↓ НОВЫЕ МЕТОДЫ ↓↓↓

    // возвращает список бронирований для item + статус
    List<Booking> findByItem_IdAndStatus(long itemId, Status status);

    // находит одно бронирование по item и booker
    Booking findByItem_IdAndBooker_Id(long itemId, long bookerId);

    // (опционально) для «последнего» и «следующего»:
    Booking findTopByItem_IdAndStatusAndEndBeforeOrderByEndDesc(long itemId,
                                                                Status status,
                                                                LocalDateTime now);
    Booking findTopByItem_IdAndStatusAndStartAfterOrderByStartAsc(long itemId,
                                                                  Status status,
                                                                  LocalDateTime now);
}