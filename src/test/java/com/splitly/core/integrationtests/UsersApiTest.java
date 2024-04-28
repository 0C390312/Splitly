package com.splitly.core.integrationtests;

import static com.splitly.core.utils.TestUtils.asJson;
import static com.splitly.core.utils.TestUtils.asObject;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.splitly.core.model.User;
import com.splitly.core.utils.IntegrationTest;
import java.util.UUID;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class UsersApiTest extends IntegrationTest {

  @Test
  void createUser() throws Exception {
    var mvcResult = mockMvc.perform(post("/users")
        .contentType("application/json")
        .content(asJson("json/create-user.json")))
      .andExpect(status().isCreated())
      .andReturn();

    var response = mvcResult.getResponse().getContentAsString();
    var user = asObject(response, User.class);
    assertThat(user.getId()).isNotNull();
  }

  @Test
  void createUser_thenGetUserById() throws Exception {
    var user = createUser("json/create-user.json");

    var mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users/" + user.getId()))
      .andExpect(status().isOk())
      .andReturn();

    var response = mvcResult.getResponse().getContentAsString();
    var userFromResponse = asObject(response, User.class);
    Assertions.assertThat(userFromResponse).isEqualTo(user);
  }

  @SneakyThrows
  public static MvcResult doPostUsersByJson(String pathToUserJson) {
    return mockMvc.perform(post("/users")
        .contentType(APPLICATION_JSON)
        .content(asJson("json/create-user.json")))
      .andExpect(status().isCreated())
      .andReturn();
  }

  @SneakyThrows
  public static User doPostUsersByName(String name) {
    var mvcResult = mockMvc.perform(post("/users")
        .contentType(APPLICATION_JSON)
        .content(asJson(user(name))))
      .andExpect(status().isCreated())
      .andReturn();
    return asObject(mvcResult.getResponse().getContentAsString(), User.class);
  }

  @SneakyThrows
  public static MvcResult doPostUsers(User user) {
    return mockMvc.perform(post("/users")
        .contentType(APPLICATION_JSON)
        .content(asJson(user)))
      .andExpect(status().isCreated())
      .andReturn();
  }

  @SneakyThrows
  public static User createUser(String pathToUserJson) {
    var mvcResult = doPostUsersByJson(pathToUserJson);
    var response = mvcResult.getResponse().getContentAsString();
    return asObject(response, User.class);
  }

  @SneakyThrows
  public static MvcResult getUserById(UUID userId) {
    return mockMvc.perform(MockMvcRequestBuilders.get("/users/" + userId))
      .andExpect(status().isOk())
      .andReturn();
  }

  public static User user(String name) {
    return new User().name(name);
  }
}
