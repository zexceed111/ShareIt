package ru.practicum.shareit.user.model;

import lombok.*;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    long id;
    String name;
    String email;
}
