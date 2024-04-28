package com.splitly.core.service;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class DebtGraphTest {


  @Test
  void debt() {
    DebtGraph debtGraph = new DebtGraph();
    debtGraph.addPayment("u1", new BigDecimal("100"), "u2", "u3");
    debtGraph.addPayment("u2", new BigDecimal("120"), "u2", "u3");
    debtGraph.addPayment("u3", new BigDecimal("420"), "u1", "u2", "u3");
    debtGraph.simplifyDebts();


  }
}