package com.splitly.core.integrationtests;

import static com.splitly.core.integrationtests.UsersApiTest.doPostUsersByName;
import static com.splitly.core.utils.TestUtils.asJson;
import static com.splitly.core.utils.TestUtils.asObject;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.splitly.core.model.Group;
import com.splitly.core.utils.IntegrationTest;
import java.util.List;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class GroupsApiTest extends IntegrationTest {

  @Test
  void createGroup() throws Exception {
    var user1 = UsersApiTest.doPostUsersByName("John");
    var user2 = UsersApiTest.doPostUsersByName("Jane");
    var group = new Group().addUsersItem(user1.getId()).addUsersItem(user2.getId());

    var mvcResult = mockMvc.perform(post("/groups")
        .contentType(APPLICATION_JSON)
        .content(asJson(group)))
      .andExpect(status().isCreated())
      .andReturn();

    var contentAsString = mvcResult.getResponse().getContentAsString();
    var groupFromResponse = asObject(contentAsString, Group.class);

    var getGroupResult = mockMvc.perform(get("/groups/" + groupFromResponse.getId()))
      .andExpect(status().isOk())
      .andReturn();
    var contentAsString1 = getGroupResult.getResponse().getContentAsString();
    var groupFromResponse1 = asObject(contentAsString1, Group.class);

    assertThat(groupFromResponse).isEqualTo(groupFromResponse1);
    assertThat(groupFromResponse.getId()).isNotNull();
    assertThat(groupFromResponse.getUsers()).containsExactlyInAnyOrder(user1.getId(), user2.getId());
  }

  @Test
  void createGroup_thenAddUserToGroup() throws Exception {
    var group = doPostGroups("test-group");

    var user = doPostUsersByName("John");

    mockMvc.perform(post("/groups/" + group.getId() + "/users")
        .contentType(APPLICATION_JSON)
        .content(asJson(List.of(user.getId()))))
      .andExpect(status().isOk());

    var result = doGetGroup(group.getId());

    assertThat(result.getUsers().get(0)).isEqualTo(user.getId());
  }

  @SneakyThrows
  public static Group doGetGroup(UUID id) {
    var mvcResult = mockMvc.perform(get("/groups/" + id))
      .andExpect(status().isOk())
      .andReturn();
    return asObject(mvcResult.getResponse().getContentAsString(), Group.class);
  }

  @SneakyThrows
  public static Group doPostGroups(String name) {
    var group = new Group().name(name);
    var mvcResult = mockMvc.perform(post("/groups")
        .contentType(APPLICATION_JSON)
        .content(asJson(group)))
      .andExpect(status().isCreated())
      .andReturn();
    return asObject(mvcResult.getResponse().getContentAsString(), Group.class);
  }

  @SneakyThrows
  public static Group doPostGroups(Group group) {
    var mvcResult = mockMvc.perform(post("/groups")
        .contentType(APPLICATION_JSON)
        .content(asJson(group)))
      .andExpect(status().isCreated())
      .andReturn();
    return asObject(mvcResult.getResponse().getContentAsString(), Group.class);
  }
}
