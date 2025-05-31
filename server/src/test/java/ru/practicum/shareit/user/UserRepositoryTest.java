package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmailTest() {
        User user = new User();
        user.setName("new_user");
        user.setEmail("new_user@example.com");
        Optional<User> userData = userRepository.findByEmail(user.getEmail());

        Assertions.assertNotNull(Optional.ofNullable(userData));
        Assertions.assertEquals(userData.get().getName(), "new_user");
    }
}
