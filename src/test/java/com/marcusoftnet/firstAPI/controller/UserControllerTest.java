package com.marcusoftnet.firstAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcusoftnet.firstAPI.model.User;
import com.marcusoftnet.firstAPI.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean // Use MockBean to create a mock of UserService
  private UserService userService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void testGetAllUsers() throws Exception {
    mockMvc.perform(get("/api/users"))
      .andExpect(status().isOk());
  }

  @Test
  void testGetUserById() throws Exception {
    User user = new User("1", "John Doe", "john@example.com");
    Mockito.when(userService.getUserById("1")).thenReturn(Optional.of(user));

    mockMvc.perform(get("/api/users/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("John Doe"));
  }

  @Test
  void testCreateUser() throws Exception {
    User user = new User("1", "John Doe", "john@example.com");

    mockMvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(user)))
      .andExpect(status().isCreated());

    Mockito.verify(userService).createUser(Mockito.any(User.class));
  }

  @Test
  void testUpdateUser() throws Exception {
    User user = new User("1", "John Doe", "john@example.com");

    Mockito.when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));

    mockMvc.perform(put("/api/users/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(user)))
      .andExpect(status().isOk());

    Mockito.verify(userService).updateUser(Mockito.any(User.class));
  }

  @Test
  void testDeleteUserById() throws Exception {
    User user = new User("1", "John Doe", "john@example.com");
    Mockito.when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));

    mockMvc.perform(delete("/api/users/1"))
      .andExpect(status().isOk());

    Mockito.verify(userService).deleteUserById("1");
  }
}
