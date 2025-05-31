package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Long userId = 1L;

    @Test
    void getAllUsersList() throws Exception {
        UserDto user = new UserDto();
        user.setId(userId);
        user.setName("John");
        user.setEmail("john@example.com");

        Mockito.when(userService.getAllUsersList()).thenReturn(List.of(user));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(userId))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[0].email").value("john@example.com"));
    }

    @Test
    void getUserById() throws Exception {
        UserDto user = new UserDto();
        user.setId(userId);
        user.setName("Jane");
        user.setEmail("jane@example.com");

        Mockito.when(userService.getUserById(userId)).thenReturn(user);

        mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("Jane"))
                .andExpect(jsonPath("$.email").value("jane@example.com"));
    }

    @Test
    void createUser() throws Exception {
        UserCreateDto createDto = new UserCreateDto();
        createDto.setName("Alice");
        createDto.setEmail("alice@example.com");

        UserDto responseDto = new UserDto();
        responseDto.setId(userId);
        responseDto.setName("Alice");
        responseDto.setEmail("alice@example.com");

        Mockito.when(userService.addUser(any())).thenReturn(responseDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void updateUser() throws Exception {
        UserUpdateDto updateDto = new UserUpdateDto();
        updateDto.setName("Updated Name");
        updateDto.setEmail("updated@example.com");

        UserDto updatedUser = new UserDto();
        updatedUser.setId(userId);
        updatedUser.setName("Updated Name");
        updatedUser.setEmail("updated@example.com");

        Mockito.when(userService.updateUser(eq(userId), any())).thenReturn(updatedUser);

        mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    void deleteUserById() throws Exception {
        Mockito.doNothing().when(userService).deleteUserById(userId);

        mockMvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isOk());
    }
}

