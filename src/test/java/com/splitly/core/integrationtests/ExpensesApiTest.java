package com.splitly.core.integrationtests;

import static com.splitly.core.integrationtests.GroupsApiTest.doPostGroups;
import static com.splitly.core.integrationtests.UsersApiTest.doPostUsersByName;
import static com.splitly.core.utils.TestUtils.asJson;
import static com.splitly.core.utils.TestUtils.asObject;
import static org.apache.commons.lang3.math.NumberUtils.createBigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.splitly.core.model.Expense;
import com.splitly.core.model.Group;
import com.splitly.core.utils.IntegrationTest;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class ExpensesApiTest extends IntegrationTest {

  @Test
  void createExpense() throws Exception {
    var user1 = doPostUsersByName("jane");
    var user2 = doPostUsersByName("john");
    var user3 = doPostUsersByName("joe");
    var group = new Group().addUsersItem(user1.getId()).addUsersItem(user2.getId()).addUsersItem(user3.getId());
    group = doPostGroups(group);

    var expense = new Expense()
      .amount(createBigDecimal("102.30"))
      .currency("GEL")
      .participants(List.of(user1.getId(), user2.getId()))
      .payerId(user3.getId());

    var expenseFromResponse = doPostExpenses(group.getId(), expense);

    var expenseFromResponse1 = doGetExpenses(group.getId(), expenseFromResponse.getId());

    assertThat(expenseFromResponse1.getAmount().compareTo(createBigDecimal("102.30"))).isEqualTo(0);
    assertThat(expenseFromResponse1.getCurrency()).isEqualTo("GEL");
    assertThat(expenseFromResponse1.getParticipants()).containsExactlyInAnyOrder(user1.getId(), user2.getId());
  }

  @Test
  void createMoreThanOneExpenses() throws Exception {
    var user1 = doPostUsersByName("jane");
    var user2 = doPostUsersByName("john");
    var user3 = doPostUsersByName("joe");
    var group = new Group().addUsersItem(user1.getId()).addUsersItem(user2.getId()).addUsersItem(user3.getId());
    group = doPostGroups(group);

    var expense1 = new Expense()
      .amount(createBigDecimal("102.30"))
      .currency("GEL")
      .participants(List.of(user1.getId(), user2.getId()))
      .payerId(user3.getId());

    var expense2 = new Expense()
      .amount(createBigDecimal("202.30"))
      .currency("GEL")
      .participants(List.of(user2.getId(), user3.getId()))
      .payerId(user2.getId());

    var expense3 = new Expense()
      .amount(createBigDecimal("302.30"))
      .currency("GEL")
      .participants(List.of(user1.getId(), user2.getId(), user3.getId()))
      .payerId(user1.getId());

    doPostExpenses(group.getId(), expense1);
    doPostExpenses(group.getId(), expense2);
    doPostExpenses(group.getId(), expense3);

    var response = mockMvc.perform(get("/groups/" + group.getId() + "/expenses"))
      .andExpect(status().isOk())
      .andReturn()
      .getResponse().getContentAsString();
    var expenses = asObject(response, new TypeReference<List<Expense>>() {});

    assertThat(expenses.size()).isEqualTo(3);
  }

  public static Expense doGetExpenses(UUID groupId, UUID expenseId) throws Exception {
    var getExpense = mockMvc.perform(get("/groups/" + groupId + "/expenses/" + expenseId))
      .andExpect(status().isOk())
      .andReturn();
    var contentAsString1 = getExpense.getResponse().getContentAsString();
    return asObject(contentAsString1, Expense.class);
  }

  public static Expense doPostExpenses(UUID groupId, Expense expense) throws Exception {
    var mvcResult = mockMvc.perform(post("/groups/" + groupId + "/expenses")
        .contentType(APPLICATION_JSON)
        .content(asJson(expense)))
      .andExpect(status().isCreated())
      .andReturn();
    var contentAsString = mvcResult.getResponse().getContentAsString();
    return asObject(contentAsString, Expense.class);
  }

  public static Expense expense(UUID payerId, BigDecimal amount, List<UUID> participants) {
    return new Expense()
      .payerId(payerId)
      .amount(amount)
      .participants(participants);
  }
}
