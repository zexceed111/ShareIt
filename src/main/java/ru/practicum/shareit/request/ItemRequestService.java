package ru.practicum.shareit.request;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(ItemRequestDto requestDto);

    ItemRequestDto getRequestById(Long requestId);

    List<ItemRequestDto> getRequestsByUserId(Long userId);

    List<ItemRequestDto> searchRequests(String text);

    ItemRequestDto update(Long requestId, ItemRequestDto updatedRequest);

    void delete(Long requestId);
}
