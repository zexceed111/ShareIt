package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;


@RestController
@RequestMapping(path = "/users")
@Slf4j
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        return userClient.getUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable
                                               @NotNull(message = "the field cannot be empty")
                                               @Positive(message = "user id must be positive")
                                               Long userId) {
        return userClient.getUserById(userId);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid
                                              @RequestBody
                                              @NotNull(message = "the field cannot be empty")
                                              UserCreateDto userCreateDto) {
        return userClient.postUser(userCreateDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable
                                        @NotNull(message = "the field cannot be empty")
                                        @Positive(message = "user id must be positive")
                                        Long userId,
                                        @Valid
                                        @RequestBody
                                        @NotNull(message = "the field cannot be empty")
                                        UserUpdateDto userUpdateDto) {
        return userClient.updateUser(userId, userUpdateDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable
                               @NotNull(message = "the field cannot be empty")
                               @Positive(message = "user id must be positive")
                               Long userId) {
        return userClient.deleteById(userId);
    }
}
