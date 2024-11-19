package com.marcusoftnet.firstAPI.controller;

import com.marcusoftnet.firstAPI.model.User;
import com.marcusoftnet.firstAPI.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @PostMapping
  public ResponseEntity<Void> createUser(@RequestBody User user) {
    userService.createUser(user);
    return  ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable String id) {
    Optional<User> user = userService.getUserById(id);
    return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    // which is the short version of the following
    //    if(user.isEmpty())
    //      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    //    else
    //      return ResponseEntity.ok(user.get())
//
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> updateUser(@PathVariable String id, @RequestBody User userFromRequest) {
    if(userService.getUserById(id).isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    userFromRequest.setId(id);
    userService.updateUser(userFromRequest);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUserById(@PathVariable String id) {
    if(userService.getUserById(id).isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    userService.deleteUserById(id);
    return ResponseEntity.ok().build();
  }
}
