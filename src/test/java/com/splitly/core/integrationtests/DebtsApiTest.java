package com.splitly.core.integrationtests;

import static com.splitly.core.integrationtests.ExpensesApiTest.doPostExpenses;
import static com.splitly.core.integrationtests.ExpensesApiTest.expense;
import static com.splitly.core.integrationtests.GroupsApiTest.doPostGroups;
import static com.splitly.core.integrationtests.UsersApiTest.doPostUsersByName;
import static com.splitly.core.utils.TestUtils.asObject;
import static org.apache.commons.lang3.math.NumberUtils.createBigDecimal;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.splitly.core.model.Debt;
import com.splitly.core.utils.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Test;

public class DebtsApiTest extends IntegrationTest {

  @Test
  void calculateDebts() throws Exception {
    var user1 = doPostUsersByName("Alice").getId();
    var user2 = doPostUsersByName("Bob").getId();
    var user3 = doPostUsersByName("Charlie").getId();

    var group = doPostGroups("group").getId();

    doPostExpenses(group, expense(user1, createBigDecimal("90"), List.of(user1, user2, user3)));
    doPostExpenses(group, expense(user1, createBigDecimal("90"), List.of(user1, user2, user3)));
    doPostExpenses(group, expense(user1, createBigDecimal("90"), List.of(user1, user2, user3)));
    doPostExpenses(group, expense(user1, createBigDecimal("90"), List.of(user1, user2, user3)));

    var contentAsString = mockMvc.perform(get("/groups/" + group + "/balances"))
      .andExpect(status().isOk())
      .andReturn()
      .getResponse().getContentAsString();

    var debts = asObject(contentAsString, new TypeReference<List<Debt>>() {});
    assertThat(debts.size()).isEqualTo(2);
    assertThat(debts.get(0).getAmount().compareTo(createBigDecimal("120"))).isEqualTo(0);
    assertThat(debts.get(1).getAmount().compareTo(createBigDecimal("120"))).isEqualTo(0);
  }
}
