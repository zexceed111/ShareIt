package ru.yandex.practicum.item;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItemService {
    ItemDto create(ItemDto itemDto, Long ownerId);

    ItemDto getItem(Long itemId);

    ItemDto update(Long itemId, ItemDto itemDto, Long userId);

    void deleteItem(Long itemId);

    List<ItemDto> getAllItemsByOwner(Long ownerId);

    List<ItemDto> search(String text);
}
