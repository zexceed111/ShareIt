package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCreateDto {
    @NotNull(message = "the field cannot be empty")
    @NotBlank(message = "the field cannot be blank")
    String name;
    @NotNull(message = "the field cannot be empty")
    @Email(message = "incorrect email input format")
    String email;
}
