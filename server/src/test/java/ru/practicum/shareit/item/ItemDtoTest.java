package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentShortDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void testSerialize() throws Exception {
        User owner = User.builder()
                .id(1L)
                .name("John")
                .email("john@example.com")
                .build();

        User booker = User.builder()
                .id(2L)
                .name("Mary")
                .email("mary@example.com")
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("new_item")
                .description("new_description")
                .available(true)
                .owner(owner).build();

        Booking lastBooking = Booking.builder()
                .id(100L)
                .start(LocalDateTime.of(2026, 6, 2, 0, 0, 0))
                .ending(LocalDateTime.of(2026, 6, 4, 0, 0, 0))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build();

        Booking nextBooking = Booking.builder()
                .id(101L)
                .start(LocalDateTime.of(2026, 6, 12, 0, 0, 0))
                .ending(LocalDateTime.of(2026, 6, 17, 0, 0, 0))
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();

        CommentShortDto comment = CommentShortDto.builder()
                .text("test_text")
                .authorName("Alice")
                .build();

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("new_item")
                .description("new_description")
                .available(true)
                .owner(owner)
                .request("new_request")
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(List.of(comment))
                .build();

        assertThat(json.write(itemDto)).hasJsonPathNumberValue("$.id");
        assertThat(json.write(itemDto)).hasJsonPathStringValue("$.name");
        assertThat(json.write(itemDto)).hasJsonPathStringValue("$.description");
        assertThat(json.write(itemDto)).hasJsonPathBooleanValue("$.available");
        assertThat(json.write(itemDto)).hasJsonPathMapValue("$.owner");
        assertThat(json.write(itemDto)).hasJsonPathStringValue("$.request");
        assertThat(json.write(itemDto)).hasJsonPathMapValue("$.lastBooking");
        assertThat(json.write(itemDto)).hasJsonPathMapValue("$.nextBooking");
        assertThat(json.write(itemDto)).hasJsonPathArrayValue("$.comments");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{"
                + "\"id\": 1,"
                + "\"name\": \"new_item\","
                + "\"description\": \"new_description\","
                + "\"available\": true,"
                + "\"owner\": {"
                + "  \"id\": 1,"
                + "  \"name\": \"John\","
                + "  \"email\": \"john@example.com\""
                + "},"
                + "\"request\": \"new_request\","
                + "\"lastBooking\": {"
                + "  \"id\": 100,"
                + "  \"start\": \"2025-06-02T00:00:00\","
                + "  \"end\": \"2025-06-04T00:00:00\","
                + "  \"item\": {"
                + "    \"id\": 1,"
                + "    \"name\": \"new_item\","
                + "    \"description\": \"new_description\","
                + "    \"available\": true,"
                + "    \"owner\": {"
                + "      \"id\": 1,"
                + "      \"name\": \"John\","
                + "      \"email\": \"john@example.com\""
                + "    }"
                + "  },"
                + "  \"booker\": {"
                + "    \"id\": 2,"
                + "    \"name\": \"Mary\","
                + "    \"email\": \"mary@example.com\""
                + "  },"
                + "  \"status\": \"APPROVED\""
                + "},"
                + "\"nextBooking\": {"
                + "  \"id\": 101,"
                + "  \"start\": \"2025-06-12T00:00:00\","
                + "  \"end\": \"2025-06-17T00:00:00\","
                + "  \"item\": {"
                + "    \"id\": 1,"
                + "    \"name\": \"new_item\","
                + "    \"description\": \"new_description\","
                + "    \"available\": true,"
                + "    \"owner\": {"
                + "      \"id\": 1,"
                + "      \"name\": \"John\","
                + "      \"email\": \"john@example.com\""
                + "    }"
                + "  },"
                + "  \"booker\": {"
                + "    \"id\": 2,"
                + "    \"name\": \"Mary\","
                + "    \"email\": \"mary@example.com\""
                + "  },"
                + "  \"status\": \"WAITING\""
                + "},"
                + "\"comments\": ["
                + "  {"
                + "    \"id\": 200,"
                + "    \"text\": \"test_text\","
                + "    \"authorName\": \"Alice\""
                + "  }"
                + "]"
                + "}";


        ItemDto itemDto = json.parseObject(content);

        assertThat(itemDto.getId()).isEqualTo(1L);
        assertThat(itemDto.getName()).isEqualTo("new_item");
        assertThat(itemDto.getDescription()).isEqualTo("new_description");
        assertThat(itemDto.isAvailable()).isTrue();
        assertThat(itemDto.getOwner().getId()).isEqualTo(1L);
        assertThat(itemDto.getOwner().getName()).isEqualTo("John");
        assertThat(itemDto.getOwner().getEmail()).isEqualTo("john@example.com");
        assertThat(itemDto.getRequest()).isEqualTo("new_request");
        assertThat(itemDto.getLastBooking().getId()).isEqualTo(100L);
        assertThat(itemDto.getLastBooking().getBooker().getId()).isEqualTo(2L);
        assertThat(itemDto.getNextBooking().getId()).isEqualTo(101L);
        assertThat(itemDto.getNextBooking().getBooker().getId()).isEqualTo(2L);
        assertThat(itemDto.getComments()).hasSize(1);
        assertThat(itemDto.getComments().get(0).getText()).isEqualTo("test_text");
        assertThat(itemDto.getComments().get(0).getAuthorName()).isEqualTo("Alice");
    }

}

