package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserDto> getAllUsersList() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    public UserDto getUserById(long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new NoSuchElementException("User with id = " + id + " not found.");
        }
        return UserMapper.toUserDto(userOptional.get());
    }

    public UserDto addUser(UserCreateDto userCreateDto) {
        validateUserEmail(userCreateDto.getEmail());
        User user = UserMapper.toUser(userCreateDto);
        userRepository.save(user);
        return UserMapper.toUserDto(user);
    }

    public UserDto updateUser(Long userId, UserUpdateDto userUpdateDto) {
        validateUserEmail(userUpdateDto.getEmail());
        checkUserId(userId);
        User dbUser = userRepository.findById(userId).get();
        User updateUser = UserMapper.toUser(userUpdateDto);
        updateUser.setId(userId);
        if (updateUser.getName() != null && updateUser.getEmail() != null) {
            userRepository.save(updateUser);
            return UserMapper.toUserDto(updateUser);
        }
        if (updateUser.getName() == null) {
            updateUser.setName(dbUser.getName());
        }
        if (updateUser.getEmail() == null) {
            updateUser.setEmail(dbUser.getEmail());
        }
        userRepository.save(updateUser);
        return UserMapper.toUserDto(updateUser);
    }

    public void deleteUserById(long id) {
        checkUserId(id);
        userRepository.deleteById(id);
    }


    private void validateUserEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            throw new IllegalArgumentException("User with the email "  + email + " already exist");
        }
    }

    private void checkUserId(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User with id = " + userId + " not found.");
        }
    }
}
