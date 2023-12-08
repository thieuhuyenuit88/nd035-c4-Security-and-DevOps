
package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final CartRepository cartRepository = mock(CartRepository.class);

    private final BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void create_user_happy_path() {
        when(bCryptPasswordEncoder.encode("password")).thenReturn("hashedPassword");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("password");
        request.setConfirmPassword("password");
        ResponseEntity<User> response = userController.createUser(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("hashedPassword", user.getPassword());
    }

    @Test
    public void create_user_unhappy_path() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("password");
        request.setConfirmPassword("different_password");
        ResponseEntity<User> response = userController.createUser(request);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void test_get_invalid_username() {
        when(userRepository.findByUsername("user2")).thenReturn(null);
        ResponseEntity<User> user = userController.findByUserName("user2");
        assertEquals(404, user.getStatusCodeValue());
    }

    @Test
    public void test_get_valid_username() {
        when(userRepository.findByUsername("test")).thenReturn(CartControllerTest.getTestUser());
        ResponseEntity<User> user = userController.findByUserName("test");
        assertEquals(200, user.getStatusCodeValue());
    }

    @Test
    public void test_get_invalid_user_id() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        ResponseEntity<User> user = userController.findById(2L);
        assertEquals(404, user.getStatusCodeValue());
    }

    @Test
    public void test_get_valid_user_id() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(CartControllerTest.getTestUser()));
        ResponseEntity<User> user = userController.findById(1L);
        assertEquals(200, user.getStatusCodeValue());
    }
}