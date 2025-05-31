package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.*;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {
    private final ItemService itemService;

    @Test
    void addNewItemTest() {
        ItemCreateDto itemCreateDto = ItemCreateDto.builder()
                .name("new_item_5")
                .description("new_item_5_description")
                .available(true)
                .build();

        ItemDto itemDto = itemService.addNewItem(itemCreateDto, 1);

        Assertions.assertNotNull(itemDto);
        Assertions.assertEquals(5, itemDto.getId());
        Assertions.assertEquals("new_item_5", itemDto.getName());
        Assertions.assertEquals("new_item_5_description", itemDto.getDescription());
        Assertions.assertEquals(1, itemDto.getOwner().getId());

    }

    @Test
    void addNewItemWithWrongOwnerTest() {
        ItemCreateDto itemCreateDto = ItemCreateDto.builder()
                .name("new_item_5")
                .description("new_item_5_description")
                .available(true)
                .build();

        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> itemService.addNewItem(itemCreateDto, 1000)
        );

        Assertions.assertEquals(e.getMessage(), "User with id = 1000 not found.");
    }

    @Test
    void updateItemTest() {
        ItemUpdateDto itemUpdateDto = ItemUpdateDto.builder()
                .name("new_item_test")
                .build();

        ItemDto itemDto = itemService.updateItem(itemUpdateDto, 1, 1);

        Assertions.assertNotNull(itemDto);
        Assertions.assertEquals("new_item_test", itemDto.getName());
    }

    @Test
    void updateItemWithWrongIdTest() {
        ItemUpdateDto itemUpdateDto = ItemUpdateDto.builder()
                .name("new_item_test")
                .build();

        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> itemService.updateItem(itemUpdateDto, 1000, 2)
        );

        Assertions.assertEquals(e.getMessage(), "Item with id = 1000 not found.");
    }

    @Test
    void updateItemWithUserIsNotOwnerTest() {
        ItemUpdateDto itemUpdateDto = ItemUpdateDto.builder()
                .name("new_item_test")
                .build();

        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> itemService.updateItem(itemUpdateDto, 1, 3)
        );

        Assertions.assertEquals(e.getMessage(), "User with id = 3 isn't owner for item with id = 1.");
    }

    @Test
    void getItemByIdTest() {
        ItemDto itemDto = itemService.getItemById(1, 1);

        Assertions.assertNotNull(itemDto);
        Assertions.assertEquals("new_item", itemDto.getName());
        Assertions.assertEquals("description_new_item", itemDto.getDescription());
    }

    @Test
    void getItemsListTest() {
        List<ItemDto> itemDtoList = itemService.getItemsList(3L);

        Assertions.assertNotNull(itemDtoList);
        Assertions.assertEquals(3, itemDtoList.size());
    }

    @Test
    void searchItemsTest() {
        List<ItemDto> itemDtoList = itemService.searchItems("item", 3);

        Assertions.assertNotNull(itemDtoList);
        Assertions.assertEquals(2, itemDtoList.size());
    }

    @Test
    void addNewCommentTest() {
        CommentCreateDto commentCreateDto = CommentCreateDto.builder()
                .text("comment_test_text").build();

        CommentDto commentDto = itemService.addNewComment(3, 2, commentCreateDto);

        Assertions.assertNotNull(commentDto);
        Assertions.assertEquals(2, commentDto.getId());
        Assertions.assertEquals("comment_test_text", commentDto.getText());
        Assertions.assertEquals("booker", commentDto.getAuthorName());
    }

    @Test
    void addNewCommentWithWrongBookerIdTest() {
        CommentCreateDto commentCreateDto = CommentCreateDto.builder()
                .text("comment_test_text").build();

        RuntimeException e = assertThrows(RuntimeException.class,
                () -> itemService.addNewComment(3, 3, commentCreateDto)
        );

        Assertions.assertEquals(e.getMessage(), "User with id 3 сannot leave a comment on the item with id 3.");

    }
}
