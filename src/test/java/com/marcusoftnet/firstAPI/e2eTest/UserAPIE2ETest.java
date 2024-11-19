package com.marcusoftnet.firstAPI.e2eTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcusoftnet.firstAPI.model.User;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("e2e")  // Loads application-e2e.yml configuration
public class UserAPIE2ETest {

  @LocalServerPort
  private int port;

  private String baseUrl;
  private RestTemplate restTemplate;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() throws IOException {
    baseUrl = "http://localhost:" + port + "/api/users";
    restTemplate = new RestTemplate();

    resetUserDataFile();
  }

  @AfterEach
  void tearDown() throws IOException {
    resetUserDataFile();
  }

  private File getDataFile() throws IOException {
    File userFile = new File("src/main/resources/users.json");
    if (!userFile.exists()) {
      throw new IOException("users.json file does not exist");
    }

    return userFile;
  }


  private void resetUserDataFile() throws IOException {
    File userFile = getDataFile();
    Files.write(Paths.get(userFile.toURI()), "[]".getBytes()); // Reset file to an empty array
  }

  private void addUserToDataFile(User newUser) throws IOException {
    File userFile = getDataFile();
    ObjectMapper objectMapper = new ObjectMapper();

    // Check if the file exists, if not, create an empty list
    List<User> users;
    if (userFile.exists()) {
      // Read the existing users from the file
      users = objectMapper.readValue(userFile, new TypeReference<List<User>>() {
      });
    } else {
      users = new ArrayList<>();
    }

    // Add the new user to the list
    users.add(newUser);

    // Write the updated list back to the file
    objectMapper.writeValue(userFile, users);
  }


  @Test
  void testCreateAndRetrieveUser() throws IOException {
    // Step 1: Create a User in the "database"
    User testUser = new User("1", "Jane Doe", "jane@example.com");
    addUserToDataFile(testUser);

    // Step 2: Retrieve the User by ID
    String urlWithId = baseUrl + "/" + testUser.getId();
    ResponseEntity<User> getResponse = restTemplate.getForEntity(urlWithId, User.class);
    assertEquals(HttpStatus.OK, getResponse.getStatusCode());

    User retrievedUser = getResponse.getBody();
    assertNotNull(retrievedUser);
    assertEquals(testUser.getId(), retrievedUser.getId());
    assertEquals(testUser.getName(), retrievedUser.getName());
    assertEquals(testUser.getEmail(), retrievedUser.getEmail());
  }

}
