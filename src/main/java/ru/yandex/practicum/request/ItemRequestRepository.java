package ru.yandex.practicum.request;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.user.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ItemRequestRepository {

    private final Map<Long, ItemRequest> requests = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    public ItemRequest save(ItemRequest request) {
        if (request.getId() == null) {
            request.setId(idCounter.incrementAndGet());
        }
        requests.put(request.getId(), request);
        return request;
    }

    public ItemRequest findById(Long id) {
        return requests.get(id);
    }

    public Iterable<ItemRequest> findAll() {
        return requests.values();
    }

    public void delete(Long id) {
        requests.remove(id);
    }

    public List<ItemRequest> findByRequestor(User requestor) {
        return requests.values().stream().filter(r -> r.getRequestor().equals(requestor)).toList();
    }

    public List<ItemRequest> findByDescriptionContaining(String text) {
        return requests.values().stream().filter(r -> r.getDescription().contains(text)).toList();
    }
}
