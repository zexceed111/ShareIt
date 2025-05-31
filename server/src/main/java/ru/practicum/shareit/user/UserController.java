package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;


@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsersList() {
        return userService.getAllUsersList();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable
                                               Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody
                                              UserCreateDto userCreateDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(userCreateDto));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable
                                        Long userId,
                                        @RequestBody
                                        UserUpdateDto userUpdateDto) {
        return ResponseEntity.status(200).body(userService.updateUser(userId, userUpdateDto));
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable
                               Long userId) {
        userService.deleteUserById(userId);
    }
}
