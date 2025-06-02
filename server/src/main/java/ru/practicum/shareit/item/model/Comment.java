package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.user.model.User;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String text;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    User author;
}
