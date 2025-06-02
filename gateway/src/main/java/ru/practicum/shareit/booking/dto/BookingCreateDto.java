package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.*;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingCreateDto {
    @NotNull(message = "the field cannot be empty")
    LocalDateTime start;
    @NotNull(message = "the field cannot be empty")
    LocalDateTime end;
    @NotNull(message = "the field cannot be empty")
    Long itemId;
    Long bookerId;


    @AssertTrue(message = "the start of the booking must be earlier than the end")
    boolean isStartBeforeEnd() {
        return start.isBefore(end);
    }

    @AssertTrue(message = "start and end must be either in the future or on the current day")
    boolean isStartAndEndInFutureOrInThisDay() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        if ((start.isAfter(now) && end.isAfter(now))
                || (start.toLocalDate().equals(today) && end.toLocalDate().equals(today))) {
            return true;
        } else return false;
    }
}
