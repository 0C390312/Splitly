package com.splitly.core.service;

import static com.splitly.core.utils.DebtUtils.settleDebts;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.splitly.core.dto.Expense;
import com.splitly.core.dto.Group;
import com.splitly.core.dto.User;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class DebtUtilsTest {

  @Test
  void test() {
    var user1 = user("u1");
    var user2 = user("u2");
    var user3 = user("u3");

    var groupId = randomUUID();

    var expense1 = expense(groupId, user1, Set.of(user2, user3), new BigDecimal("100"), "GEL");
    var expense2 = expense(groupId, user2, Set.of(user2, user3), new BigDecimal("120"), "GEL");
    var expense3 = expense(groupId, user3, Set.of(user1, user2, user3), new BigDecimal("420"), "GEL");

    var expenses = Set.of(expense1, expense2, expense3);
    var debts = settleDebts(expenses);
    assertThat(debts).hasSize(2);
  }

  @Test
  void equalAmount() {
    var user1 = user("u1");
    var user2 = user("u2");

    var groupId = randomUUID();

    var expense1 = expense(groupId, user1, Set.of(user2, user1), new BigDecimal("100"), "GEL");
    var expense2 = expense(groupId, user2, Set.of(user2, user1), new BigDecimal("100"), "GEL");

    var expenses = Set.of(expense1, expense2);
    var debts = settleDebts(expenses);
    assertThat(debts).hasSize(0);
  }

  public static User user(String name) {
    return User.builder().name(name).id(randomUUID()).build();
  }

  public static Expense expense(UUID groupId, User payer, Set<User> participants, BigDecimal amount, String currency) {
    return Expense.builder()
      .id(randomUUID())
      .group(Group.builder().id(groupId).build())
      .payer(payer)
      .participants(participants)
      .amount(amount)
      .currency(currency)
      .build();
  }
}