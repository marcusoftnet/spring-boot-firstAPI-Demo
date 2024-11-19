package com.marcusoftnet.firstAPI.repository;

import com.marcusoftnet.firstAPI.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
  List<User> findAll();
  Optional<User> findById(String id);
  void save(User user);
  void update(User user);
  void deleteById(String id);
}
