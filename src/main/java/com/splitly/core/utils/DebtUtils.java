package com.splitly.core.utils;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;

import com.splitly.core.dto.Expense;
import com.splitly.core.dto.User;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DebtUtils {

  public static List<Debt> settleDebts(Set<Expense> expenses) {
    Map<User, BigDecimal> balanceMap = new HashMap<>();
    for (Expense expense : expenses) {
      var payer = expense.getPayer();
      balanceMap.putIfAbsent(payer, ZERO);
      balanceMap.put(payer, balanceMap.get(payer).add(expense.getAmount()));
      var splitAmount = expense.getAmount().divide(BigDecimal.valueOf(expense.getParticipants().size()), 2, HALF_UP);
      for (var participant : expense.getParticipants()) {
        balanceMap.putIfAbsent(participant, ZERO);
        balanceMap.put(participant, balanceMap.get(participant).subtract(splitAmount));
      }
    }

    List<Map.Entry<User, BigDecimal>> list = new ArrayList<>(balanceMap.entrySet());
    list.sort(Map.Entry.comparingByValue());

    List<Debt> debts = new ArrayList<>();
    int i = 0, j = list.size() - 1;
    while (i < j) {
      BigDecimal diff = list.get(j).getValue().min(list.get(i).getValue().negate());
      debts.add(new Debt(list.get(i).getKey(), list.get(j).getKey(), diff));
      balanceMap.put(list.get(i).getKey(), list.get(i).getValue().add(diff));
      balanceMap.put(list.get(j).getKey(), list.get(j).getValue().subtract(diff));
      if (balanceMap.get(list.get(i).getKey()).compareTo(ZERO) == 0) i++;
      if (balanceMap.get(list.get(j).getKey()).compareTo(ZERO) == 0) j--;
    }
    return debts.stream().filter(d -> d.amount.compareTo(ZERO) != 0).toList();
  }

  public record Debt(User debtor, User creditor, BigDecimal amount) {

    @Override
    public String toString() {
      return debtor.getName() + " owes (должен) " + creditor.getName() + ": " + amount;
    }
  }
}