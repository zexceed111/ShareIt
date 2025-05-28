package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Request {
    long id;
    String description;
    User requestor;
    LocalDateTime created;
    @Builder.Default
    List<Item> responsesList = new ArrayList<>();
}
