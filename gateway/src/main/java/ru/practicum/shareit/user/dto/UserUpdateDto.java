package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserUpdateDto {
    String name;
    @Email(message = "incorrect email input format")
    String email;
}
