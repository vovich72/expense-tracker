package com.expensetracker.service.impl;

import com.expensetracker.model.User;
import com.expensetracker.repository.UserRepository;
import com.expensetracker.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserServiceImplTest {

    private UserService userService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    UserServiceImplTest(UserRepository userRepository) {
        this.userService = new UserServiceImpl(userRepository);
    }

    @BeforeEach
    void setupThis(){
        entityManager.createNativeQuery("CREATE SEQUENCE  user_seq").executeUpdate();
    }

    @AfterEach
    void finishThis(){
        entityManager.createNativeQuery("DROP SEQUENCE user_seq").executeUpdate();
    }


    @Test
    void saveEntityInDatabaseAndReadIt() {
        //given
        var user = new User();
        user.setUsername("username");
        user.setEmail("email");
        user.setPassword("password");

        //when
        var savedUser = userService.save(user);
        User getingUser = userService.findByUsername(user.getUsername());

        //then
        assertEquals(savedUser.getId(), getingUser.getId());
        assertEquals(user.getUsername(), getingUser.getUsername());
        assertEquals(user.getEmail(), getingUser.getEmail());
        assertEquals(user.getPassword(), getingUser.getPassword());
        assertEquals(savedUser.getCreatedAt(), getingUser.getCreatedAt());
    }

    @Test
    void findUserIfExists() {
        var user = userService.findByUsername("username");
        assertNull(user);

        //if method with throw then
//        Exception exception = assertThrows(RuntimeException.class, () -> userService.findByUsername("username"));
//        assertEquals("User not found", exception.getMessage());
    }
}