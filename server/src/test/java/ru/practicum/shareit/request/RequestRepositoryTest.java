package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@DataJpaTest
public class RequestRepositoryTest {
    @Autowired
    private RequestRepository requestRepository;

    @BeforeEach
    void beforeEachTest() {

        User user = new User();
        Request request = new Request();
        List<Request> requestList = new ArrayList<>();

        user.setId(1);
        user.setName("new_user");
        user.setEmail("new_user@example.com");

        request.setId(1);
        request.setRequestor(user);
        request.setDescription("request_description");
        request.setCreated(LocalDateTime.of(2025, 2, 7, 0, 0, 0));

        requestList.add(request);

    }

    @Test
    void getAllByRequestorIdTest() {
        List<Request> requesDatatList = requestRepository.getAllByRequestorId(1);

        Assertions.assertNotNull(requesDatatList);
        Assertions.assertEquals(1, requesDatatList.size());
    }
}
