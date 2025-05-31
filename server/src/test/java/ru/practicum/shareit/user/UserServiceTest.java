package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    private final UserService userService;

    private UserCreateDto userCreateDto;
    private UserUpdateDto userUpdateDto;

    @Test
    void addUserTest() {
        userCreateDto = new UserCreateDto("Don", "don@example.com");
        UserDto userDto = userService.addUser(userCreateDto);

        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(4, userDto.getId());
        Assertions.assertEquals("Don", userDto.getName());
        Assertions.assertEquals("don@example.com", userDto.getEmail());
    }

    @Test
    void addUserWithWrongEmailTest() {
        userCreateDto = new UserCreateDto("Don", "don@example.com");
        userService.addUser(userCreateDto);
        UserCreateDto userCreateDtoWrong = new UserCreateDto("Don", "don@example.com");

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> userService.addUser(userCreateDtoWrong)
        );

        Assertions.assertEquals(e.getMessage(), "User with the email don@example.com already exist");
    }

    @Test
    void updateUserTest() {
        userUpdateDto = new UserUpdateDto("Tom", "tom@example.com");
        UserDto userDto = userService.updateUser(3L, userUpdateDto);

        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(3, userDto.getId());
        Assertions.assertEquals("Tom", userDto.getName());
        Assertions.assertEquals("tom@example.com", userDto.getEmail());
    }

    @Test
    void updateUserNameTest() {
        UserDto userDto = userService.updateUser(1L, UserUpdateDto.builder().name("new_user_name").build());

        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(1, userDto.getId());
        Assertions.assertEquals("new_user_name", userDto.getName());
        Assertions.assertEquals("new_user@example.com", userDto.getEmail());
    }

    @Test
    void updateUserEmailTest() {
        UserDto userDto = userService.updateUser(1L, UserUpdateDto.builder().email("new_user_email@example.com").build());

        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(1, userDto.getId());
        Assertions.assertEquals("new_user", userDto.getName());
        Assertions.assertEquals("new_user_email@example.com", userDto.getEmail());
    }

    @Test
    void updateUserWithWrongUserIdTest() {
        userUpdateDto = new UserUpdateDto("Tom", "tom@example.com");
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> userService.updateUser(1000L, userUpdateDto)
        );

        Assertions.assertEquals(e.getMessage(), "User with id = 1000 not found.");

    }



    @Test
    void getUserByIdTest() {
        UserDto userDto = userService.getUserById(1);

        Assertions.assertNotNull(userDto);
        Assertions.assertEquals("new_user", userDto.getName());
        Assertions.assertEquals("new_user@example.com", userDto.getEmail());

    }

    @Test
    void getUserByWrongId() {
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> userService.getUserById(1000L)
        );

        Assertions.assertEquals(e.getMessage(), "User with id = 1000 not found.");
    }

    @Test
    void getAllUsersListTest() {
        List<UserDto> userDtoList = userService.getAllUsersList();

        Assertions.assertNotNull(userDtoList);
        Assertions.assertEquals(3, userDtoList.size());
    }

    @Test
    void deleteUserById() {
        userService.deleteUserById(1);

        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> userService.getUserById(1)
        );

        Assertions.assertEquals(e.getMessage(), "User with id = 1 not found.");

    }
}
