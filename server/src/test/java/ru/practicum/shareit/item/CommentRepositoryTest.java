package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;


@DataJpaTest
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    private List<Item> itemList;
    private List<Comment> commentList;

    @BeforeEach
    void beforeEachTest() {
        User user = new User();
        User booker = new User();
        Item item = new Item();
        Comment comment = new Comment();

        user.setId(1);
        user.setName("new_user");
        user.setEmail("new_user@example.com");

        booker.setId(2);
        booker.setName("booker");
        booker.setEmail("booker@example.com");

        item.setId(1);
        item.setOwner(user);
        item.setName("new_item");
        item.setDescription("description_new_item");

        comment.setId(1);
        comment.setText("comment text");
        comment.setAuthor(booker);
        comment.setItem(item);

        itemList = new ArrayList<>();
        itemList.add(item);

        commentList = new ArrayList<>();
        commentList.add(comment);
    }

    @Test
    void findAllByItemIdTest() {
        List<Comment> commentDataList = commentRepository.findAllByItemId(1);

        Assertions.assertNotNull(commentDataList);
        Assertions.assertEquals(1, commentDataList.size());
        Assertions.assertEquals(commentList.get(0).getId(), commentDataList.get(0).getId());
    }

    @Test
    void findAllByItemListTest() {
        List<Comment> commentDataList = commentRepository.findAllByItemList(itemList);

        Assertions.assertNotNull(commentDataList);
        Assertions.assertEquals(1, commentDataList.size());
    }
}
