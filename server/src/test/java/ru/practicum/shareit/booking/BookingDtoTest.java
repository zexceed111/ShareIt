package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoTest {

    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void testSerializeBookingDto() throws Exception {
        User user = new User();
        user.setId(200L);
        user.setName("Pol");
        user.setEmail("pol@example.com");

        User booker = new User();
        booker.setId(201L);
        booker.setName("Tom");
        booker.setEmail("tom@example.com");

        Item item = new Item();
        item.setId(100L);
        item.setName("new_item");
        item.setDescription("item_description");
        item.setAvailable(true);
        item.setOwner(user);

        BookingDto dto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2025, 5, 4, 10, 0))
                .end(LocalDateTime.of(2025, 5, 4, 12, 0))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build();

        var result = json.write(dto);

        assertThat(result).hasJsonPathNumberValue("$.id");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2025-05-04T10:00:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2025-05-04T12:00:00");
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
        assertThat(result).extractingJsonPathMapValue("$.item").containsEntry("id", 100);
        assertThat(result).extractingJsonPathMapValue("$.booker").containsEntry("id", 201);
    }

    @Test
    void testDeserializeBookingDto() throws Exception {
        String jsonContent = "{"
                + "\"id\": 1,"
                + "\"start\": \"2025-05-04T10:00:00\","
                + "\"end\": \"2025-05-04T12:00:00\","
                + "\"status\": \"APPROVED\","
                + "\"item\": {"
                + "  \"id\": 100,"
                + "  \"name\": \"new_item\","
                + "  \"description\": \"item_description\","
                + "  \"available\": true,"
                + "  \"owner\": {"
                + "    \"id\": 200,"
                + "    \"name\": \"Tom\","
                + "    \"email\": \"tom@example.com\""
                + "  }"
                + "},"
                + "\"booker\": {"
                + "  \"id\": 201,"
                + "  \"name\": \"Pol\","
                + "  \"email\": \"pol@example.com\""
                + "},"
                + "\"status\": \"APPROVED\""
                + "}";


        BookingDto dto = json.parseObject(jsonContent);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getStart()).isEqualTo(LocalDateTime.of(2025, 5, 4, 10, 0));
        assertThat(dto.getEnd()).isEqualTo(LocalDateTime.of(2025, 5, 4, 12, 0));
        assertThat(dto.getStatus()).isEqualTo(BookingStatus.APPROVED);
        assertThat(dto.getItem().getId()).isEqualTo(100L);
        assertThat(dto.getBooker().getId()).isEqualTo(201L);
    }
}


