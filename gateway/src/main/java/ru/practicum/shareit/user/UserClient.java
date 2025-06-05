package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getUsers() {
        return get("");
    }

    public ResponseEntity<Object> getUserById(long userId) {
        return get("/" + userId);
    }

    public ResponseEntity<Object> postUser(UserCreateDto userCreateDto) {
        return post("", userCreateDto);
    }

    public ResponseEntity<Object> updateUser(long userId, UserUpdateDto userUpdateDto) {
        return patch("/" + userId, userId, userUpdateDto);
    }

    public ResponseEntity<Object> deleteById(long userId) {
        return delete("/" + userId, userId);
    }
}
