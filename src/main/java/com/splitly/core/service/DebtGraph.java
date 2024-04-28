package com.splitly.core.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DebtGraph {
  private Map<String, BigDecimal> balanceMap = new HashMap<>();

  public void addPayment(String payer, BigDecimal amount, String... beneficiaries) {
    int beneficiaryCount = beneficiaries.length;
    for (String beneficiary : beneficiaries) {
      if (payer.equals(beneficiary)) {
        beneficiaryCount--;
      }
    }
    BigDecimal individualAmount = amount.divide(BigDecimal.valueOf(beneficiaryCount), 2, RoundingMode.HALF_UP);
    balanceMap.put(payer, balanceMap.getOrDefault(payer, BigDecimal.ZERO).subtract(amount));
    for (String beneficiary : beneficiaries) {
      if (!payer.equals(beneficiary)) {
        balanceMap.put(beneficiary, balanceMap.getOrDefault(beneficiary, BigDecimal.ZERO).add(individualAmount));
      }
    }
  }

  public void simplifyDebts() {
    List<String> debtors = new ArrayList<>();
    List<String> creditors = new ArrayList<>();
    for (String person : balanceMap.keySet()) {
      if (balanceMap.get(person).compareTo(BigDecimal.ZERO) < 0) {
        debtors.add(person);
      } else if (balanceMap.get(person).compareTo(BigDecimal.ZERO) > 0) {
        creditors.add(person);
      }
    }

    for (String debtor : debtors) {
      for (String creditor : creditors) {
        BigDecimal debt = balanceMap.get(debtor).abs();
        BigDecimal credit = balanceMap.get(creditor);
        if (debt.compareTo(BigDecimal.ZERO) > 0 && credit.compareTo(BigDecimal.ZERO) > 0) {
          BigDecimal transaction = debt.min(credit);
          System.out.println(debtor + " pays " + creditor + ": " + transaction);
          balanceMap.put(debtor, balanceMap.get(debtor).add(transaction));
          balanceMap.put(creditor, balanceMap.get(creditor).subtract(transaction));
        }
      }
    }
  }
}