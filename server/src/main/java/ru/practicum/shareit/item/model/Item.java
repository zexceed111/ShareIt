package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "items")
public class Item {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   long id;
   String name;
   String description;
   @Column(name = "is_available")
   Boolean available;
   @ManyToOne
   @JoinColumn(name = "owner_id")
   User owner;
   @OneToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "last_booking_id")
   @Builder.Default
   Booking lastBooking = null;
   @OneToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "next_booking_id")
   @Builder.Default
   Booking nextBooking = null;
   @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
   @Builder.Default
   List<Comment> itemComments = new ArrayList<>();
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "request_id")
   Request request;
}
