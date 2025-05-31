package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.dto.ItemForRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class RequestDtoTest {

    @Autowired
    private JacksonTester<RequestDto> json;

    @Test
    void testSerializeRequest() throws Exception {
        User requestor = new User();
        requestor.setId(1L);
        requestor.setName("Ilona");
        requestor.setEmail("ilona@example.com");

        LocalDateTime created = LocalDateTime.of(2025, 5, 4, 10, 30);

        RequestDto requestDto = RequestDto.builder()
                .id(100L)
                .description("description")
                .created(created)
                .build();

        User user = new User();
        user.setId(1L);
        user.setName("Ilona");
        user.setEmail("ilona@example.com");

        ItemForRequestDto itemForRequestDto = new ItemForRequestDto();
        itemForRequestDto.setId(10L);
        itemForRequestDto.setName("item");

        requestDto.setItems(List.of(itemForRequestDto));

        var result = json.write(requestDto);

        assertThat(result).hasJsonPathNumberValue("$.id");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(100);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2025-05-04T10:30:00");
        assertThat(result).extractingJsonPathArrayValue("$.items").hasSize(1);
    }

    @Test
    void testDeserializeRequest() throws Exception {
        String jsonContent =
                "{\n" +
                "  \"id\": 100,\n" +
                "  \"description\": \"description\",\n" +
                "  \"created\": \"2025-05-04T10:30:00\",\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"id\": 10,\n" +
                "      \"name\": \"item\",\n" +
                "      \"ownerId\": 1\n" +
                "    }\n" +
                "  ]\n" +
                "}";



        RequestDto requestDto = json.parseObject(jsonContent);

        assertThat(requestDto.getId()).isEqualTo(100L);
        assertThat(requestDto.getDescription()).isEqualTo("description");
        assertThat(requestDto.getCreated()).isEqualTo(LocalDateTime.of(2025, 5, 4, 10, 30));

        assertThat(requestDto.getItems()).hasSize(1);
        ItemForRequestDto item = requestDto.getItems().get(0);
        assertThat(item.getId()).isEqualTo(10L);
        assertThat(item.getName()).isEqualTo("item");
        assertThat(item.getOwnerId()).isEqualTo(1L);
    }

}

