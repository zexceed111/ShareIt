package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoTest {

    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    void testSerializeUser() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("Ilona")
                .email("ilona@example.com")
                .build();

        assertThat(json.write(userDto)).hasJsonPathNumberValue("$.id");
        assertThat(json.write(userDto)).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(json.write(userDto)).extractingJsonPathStringValue("$.name").isEqualTo("Ilona");
        assertThat(json.write(userDto)).extractingJsonPathStringValue("$.email").isEqualTo("ilona@example.com");
    }

    @Test
    void testDeserializeUser() throws Exception {
        String jsonContent = "{"
                + "\"id\": 1,"
                + "\"name\": \"Ilona\","
                + "\"email\": \"ilona@example.com\""
                + "}";

        UserDto userDto = json.parseObject(jsonContent);

        assertThat(userDto.getId()).isEqualTo(1L);
        assertThat(userDto.getName()).isEqualTo("Ilona");
        assertThat(userDto.getEmail()).isEqualTo("ilona@example.com");
    }
}

