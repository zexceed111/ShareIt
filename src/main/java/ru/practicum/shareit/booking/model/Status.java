package ru.practicum.shareit.booking.model;

public enum Status {
    // новое бронирование, ожидает одобрения
    WAITING,

    //бронирование подтверждено владельцем
    APPROVED,

    // бронирование отклонено владельцем
    REJECTED,

    //бронирование отменено создателем
    CANCELED
}
