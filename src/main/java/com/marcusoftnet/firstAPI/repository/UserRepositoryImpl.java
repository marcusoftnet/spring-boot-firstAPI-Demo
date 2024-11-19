package com.marcusoftnet.firstAPI.repository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcusoftnet.firstAPI.model.User;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final String filePath = "src/main/resources/users.json";

  @Override
  public List<User> findAll() {
    try {
      return objectMapper.readValue(new File(filePath), new TypeReference<>() {});
    } catch (IOException e) {
      return new ArrayList<>();
    }
  }

  @Override
  public Optional<User> findById(String id) {
    return findAll().stream().filter(user -> user.getId().equals(id)).findFirst();
  }

  @Override
  public void save(User user) {
    List<User> users = findAll();
    users.add(user);
    writeToFile(users);
  }

  @Override
  public void update(User user) {
    List<User> users = findAll();
    users.removeIf(u -> u.getId().equals(user.getId()));
    users.add(user);
    writeToFile(users);
  }

  @Override
  public void deleteById(String id) {
    List<User> users = findAll();
    users.removeIf(user -> user.getId().equals(id));
    writeToFile(users);
  }

  private void writeToFile(List<User> users) {
    try {
      objectMapper.writeValue(new File(filePath), users);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
