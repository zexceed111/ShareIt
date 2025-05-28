package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;



@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getAllItemsFromUserList(@RequestHeader("X-Sharer-User-Id")
                                                          @Positive(message = "user id must be positive")
                                                          @NotNull(message = "the field cannot be empty")
                                                          Long userId) {
        log.info("A request has been received to receive all the user's item, user id {}.", userId);
        return itemClient.getItemsList(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemInfo(@PathVariable
                                              @Positive(message = "item id must be positive")
                                              @NotNull(message = "the field cannot be empty")
                                              Long itemId,
                                              @RequestHeader("X-Sharer-User-Id")
                                              @Positive(message = "user id must be positive")
                                              @NotNull(message = "the field cannot be empty")
                                              Long userId) {
        log.info("A request has been received to receive item's info about item with id {}.", itemId);
        return itemClient.getItemById(itemId, userId);
    }

    @PostMapping
    public ResponseEntity<Object> addNewItem(@Valid
                                              @RequestBody
                                              ItemCreateDto itemCreateDto,
                                              @RequestHeader("X-Sharer-User-Id")
                                              @Positive(message = "user id must be positive")
                                              @NotNull(message = "the field cannot be empty")
                                              Long userId) {
            log.info("Received a request to add new item.");
            return itemClient.postItem(userId, itemCreateDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@Valid
                                              @RequestBody
                                              ItemUpdateDto itemUpdateDto,
                                              @PathVariable
                                              @Positive(message = "item id must be positive")
                                              @NotNull(message = "the field cannot be empty")
                                              Long itemId,
                                              @RequestHeader("X-Sharer-User-Id")
                                              @Positive(message = "user id must be positive")
                                              @NotNull(message = "the field cannot be empty")
                                              Long userId) {
        log.info("Received a request to update an item with id {}.", itemId);
        return itemClient.updateItem(userId, itemId, itemUpdateDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam
                                              @NotNull(message = "the field cannot be empty")
                                              @NotBlank(message = "search query cannot be empty")
                                              String text,
                                              @RequestHeader("X-Sharer-User-Id")
                                              @Positive(message = "user id must be positive")
                                              @NotNull(message = "the field cannot be empty")
                                              Long userId) {
        log.info("A search request has been received.");
        return itemClient.search(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable
                                             @NotNull(message = "the field cannot be empty")
                                             @Positive(message = "user id must be positive")
                                             Long itemId,
                                             @RequestHeader("X-Sharer-User-Id")
                                             @NotNull
                                             @Positive(message = "user id must be positive")
                                             Long userId,
                                             @Valid
                                             @RequestBody
                                             CommentCreateDto comment) {
        log.info("Received a request to post comment from user with id {} on item with id {}.", userId, itemId);
        return itemClient.addComment(itemId, userId, comment);
    }
}
