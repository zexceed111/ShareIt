package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByItemId(long itemId);

    @Query("SELECT c " +
            "FROM Comment c " +
            "WHERE c.item IN :itemsList ")
    List<Comment> findAllByItemList(List<Item> itemsList);
}
