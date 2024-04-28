package com.splitly.core.service;

import static com.splitly.core.service.CalculatorTest.expense;
import static com.splitly.core.service.CalculatorTest.user;
import static com.splitly.core.service.DebtUtils.settleDebts;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.math.BigDecimal;
import java.util.Set;
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
  void equalAmmount() {
    var user1 = user("u1");
    var user2 = user("u2");

    var groupId = randomUUID();

    var expense1 = expense(groupId, user1, Set.of(user2, user1), new BigDecimal("100"), "GEL");
    var expense2 = expense(groupId, user2, Set.of(user2, user1), new BigDecimal("100"), "GEL");

    var expenses = Set.of(expense1, expense2);
    var debts = settleDebts(expenses);
    assertThat(debts).hasSize(0);
  }
}