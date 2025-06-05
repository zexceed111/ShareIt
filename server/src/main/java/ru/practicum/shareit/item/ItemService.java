package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final RequestRepository requestRepository;

    public List<ItemDto> getItemsList(Long userId) {
        checkUserAndGetUserById(userId);

        List<Item> itemsList = itemRepository.findAllByOwnerId(userId);
        List<Booking> bookingsList =  bookingRepository.findByItemIdInAndStatusOrderByStartDesc(itemsList,
                BookingStatus.APPROVED);

        Map<Long, List<Booking>> bookingsMap = bookingsList.stream()
                .collect(Collectors.groupingBy(
                        booking -> booking.getItem().getId()));

        bookingsMap.forEach((id, list) ->
                list.sort(Comparator.comparing(Booking::getStart))
        );

        for (Item item : itemsList) {
            List<Booking> bookings = bookingsMap.get(item.getId());

            if (bookings == null) {
                continue;
            }
            Booking nextBooking = bookings.stream()
                    .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                    .findFirst()
                    .orElse(null);
            item.setNextBooking(nextBooking);
            item.setLastBooking(bookings.getLast());
        }

        List<Comment> commentList = commentRepository.findAllByItemList(itemsList);
        Map<Long, List<Comment>> commentMap = commentList.stream()
                .collect(Collectors.groupingBy(comment -> comment.getItem().getId()));

        for (Item item : itemsList) {
            item.setItemComments(commentMap.getOrDefault(item.getId(), new ArrayList<>()));
        }

       return itemsList.stream()
                .map(ItemMapper::toDto)
                .toList();
    }

    public ItemDto getItemById(long id, long userId) {
        checkUserAndGetUserById(userId);
        List<CommentShortDto> itemComments = commentRepository.findAllByItemId(id).stream()
                .map(CommentMapper::toShortDto)
                .toList();
        Item item = checkAndGetItemById(id);
        ItemDto itemDto = ItemMapper.toDto(item);
        itemDto.setComments(itemComments);
        return itemDto;
    }

    @Transactional
    public ItemDto addNewItem(ItemCreateDto itemCreateDto, long userId) {
        User user = checkUserAndGetUserById(userId);
        itemCreateDto.setOwner(user);
        if (itemCreateDto.getRequestId() != null) {
            checkAndGetRequestById(itemCreateDto.getRequestId());
        }
        Item item = ItemMapper.toItem(itemCreateDto);
        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Transactional
    public ItemDto updateItem(ItemUpdateDto itemUpdateDto, long itemId, long userId) {
        User user = checkUserAndGetUserById(userId);
        Item updatedItem = checkAndGetItemById(itemId);
        checkIsOwner(userId, itemId);

        Item updateItem = ItemMapper.toItem(itemUpdateDto);
        updateItem.setOwner(user);
        updateItem.setId(itemId);

        if (itemUpdateDto.getAvailable() == null) {
            updateItem.setAvailable(updatedItem.getAvailable());
        }
        if (itemUpdateDto.getDescription() == null) {
            updateItem.setDescription(updatedItem.getDescription());
        }
        if (itemUpdateDto.getName() == null) {
            updateItem.setName(updatedItem.getName());
        }
        return ItemMapper.toDto(itemRepository.save(updateItem));
    }

    public List<ItemDto> searchItems(String text, long userId) {
        checkUserAndGetUserById(userId);
        return itemRepository.findAllByOwnerIdAndText(userId, text).stream()
                .map(ItemMapper::toDto)
                .toList();
    }

    @Transactional
    public CommentDto addNewComment(long itemId, long userId, CommentCreateDto commentCreateDto) {
        Item item = checkAndGetItemById(itemId);
        User user = checkAndGetUserById(userId);
        checkWasBooker(itemId, userId);
        Comment comment = CommentMapper.toComment(commentCreateDto);
        comment.setItem(item);
        comment.setAuthor(user);
        itemRepository.save(item);

        return CommentMapper.toDto(commentRepository.save(comment));
    }

    private User checkUserAndGetUserById(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NoSuchElementException("User with id = " + userId + " not found.");
        }
        return userOptional.get();
        }

    private Item checkAndGetItemById(Long itemId) {
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (itemOptional.isEmpty()) {
            throw new NoSuchElementException("Item with id = " + itemId + " not found.");
        }
        return itemOptional.get();
    }

    private void checkIsOwner(long userId, long itemId) {
        if (checkAndGetItemById(itemId).getOwner().getId() != userId) {
                throw new NoSuchElementException("User with id = " + userId + " isn't owner for item with id = " + itemId + ".");
        }
    }

    private User checkAndGetUserById(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NoSuchElementException("User with id = " + userId + " not found.");
        }
        return userOptional.get();
    }

    private void checkWasBooker(long itemId, long userId) {
        Optional<Booking> bookingOptional = bookingRepository.findByItemIdAndBookerIdAndStatusAndEndingBefore(itemId, userId, BookingStatus.APPROVED, LocalDateTime.now());
        if (bookingOptional.isEmpty()) {
            throw new RuntimeException("User with id " + userId + " сannot leave a comment on the item with id " + itemId + ".");
        }
    }

    private Request checkAndGetRequestById(long requestId) {
        Optional<Request> requestOptional = requestRepository.findById(requestId);
        if (requestOptional.isEmpty()) {
            throw new NoSuchElementException("Request with id = " + requestId + " not found.");
        }
        return requestOptional.get();
    }
}
