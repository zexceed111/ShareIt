package ru.practicum.shareit.request;

import jakarta.persistence.*;
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
@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String description;
    @OneToOne
    @JoinColumn(name = "requestor_id")
    User requestor;
    LocalDateTime created;
    @OneToMany(mappedBy = "request", fetch = FetchType.EAGER)
    @Builder.Default
    List<Item> responsesList = new ArrayList<>();
}
