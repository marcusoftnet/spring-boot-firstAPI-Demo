package com.marcusoftnet.firstAPI.service;

import com.marcusoftnet.firstAPI.model.User;
import com.marcusoftnet.firstAPI.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetAllUsers() {
    when(userRepository.findAll()).thenReturn(Arrays.asList(new User("1", "John Doe", "john@example.com")));

    var users = userService.getAllUsers();

    assertEquals(1, users.size());
    verify(userRepository).findAll();
  }

  @Test
  void testGetUserById() {
    User user = new User("1", "John Doe", "john@example.com");
    when(userRepository.findById("1")).thenReturn(Optional.of(user));

    var result = userService.getUserById("1");

    assertTrue(result.isPresent());
    assertEquals("John Doe", result.get().getName());
    verify(userRepository).findById("1");
  }

  @Test
  void testCreateUser() {
    User user = new User("1", "John Doe", "john@example.com");
    userService.createUser(user);

    verify(userRepository).save(user);
  }

  @Test
  void testUpdateUser() {
    User user = new User("1", "John Doe", "john@example.com");
    when(userRepository.findById("1")).thenReturn(Optional.of(user));
    userService.updateUser(user);

    verify(userRepository).update(user);
  }

  @Test
  void testDeleteUserById() {
    User user = new User("1", "John Doe", "john@example.com");
    when(userRepository.findById("1")).thenReturn(Optional.of(user));

    userService.deleteUserById("1");

    verify(userRepository).deleteById("1");
  }
}
