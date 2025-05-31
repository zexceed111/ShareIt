package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getAllItemsFromUserList(@RequestHeader("X-Sharer-User-Id")
                                           Long userId) {
        log.info("A request has been received to receive all the user's item, user id {}.", userId);
        return itemService.getItemsList(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemInfo(@PathVariable
                               Long itemId,
                               @RequestHeader("X-Sharer-User-Id")
                               Long userId) {
        log.info("A request has been received to receive item's info about item with id {}.", itemId);
        return itemService.getItemById(itemId, userId);
    }

    @PostMapping
    public ResponseEntity<ItemDto> addNewItem(@RequestBody
                                              ItemCreateDto itemCreateDto,
                                              @RequestHeader("X-Sharer-User-Id")
                                              Long userId) {
            log.info("Received a request to add new item.");
            return ResponseEntity.status(HttpStatus.CREATED).body(itemService.addNewItem(itemCreateDto,userId));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@RequestBody
                                              ItemUpdateDto itemUpdateDto,
                                              @PathVariable
                                              Long itemId,
                                              @RequestHeader("X-Sharer-User-Id")
                                              Long userId) {
        log.info("Received a request to update an item with id {}.", itemId);
        return ResponseEntity.ok(itemService.updateItem(itemUpdateDto, itemId, userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> searchItems(@RequestParam String text,
                                                     @RequestHeader("X-Sharer-User-Id")
                                                     Long userId) {
        log.info("A search request has been received.");
        return ResponseEntity.ok(itemService.searchItems(text, userId));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> addComment(@PathVariable
                                                 Long itemId,
                                                 @RequestHeader("X-Sharer-User-Id")
                                                 Long userId,
                                                 @RequestBody
                                                 CommentCreateDto comment) {
        log.info("Received a request to post comment from user with id {} on item with id {}.", userId, itemId);
        return ResponseEntity.ok(itemService.addNewComment(itemId, userId, comment));
    }
}
