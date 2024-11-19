package com.marcusoftnet.firstAPI.service;

import com.marcusoftnet.firstAPI.model.User;
import com.marcusoftnet.firstAPI.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  public Optional<User> getUserById(String id) {
    return userRepository.findById(id);
  }

  public void createUser(User user) {
    userRepository.save(user);
  }

  public void updateUser(User user) {
    userRepository.update(user);
  }

  public void deleteUserById(String id) {
    userRepository.deleteById(id);
  }
}
