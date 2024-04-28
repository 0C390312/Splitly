package com.splitly.core.service;

import static java.math.BigDecimal.valueOf;

import com.splitly.core.dto.Expense;
import com.splitly.core.dto.User;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

@Log4j2
@UtilityClass
public class Calculator {

  public static Map<String, Map<String, BigDecimal>> calculateBalances(Set<Expense> expenses) {
    Map<User, Map<User, BigDecimal>> balances = new HashMap<>();

    for (Expense expense : expenses) {
      var payer = expense.getPayer();
      var amount = expense.getAmount();
      Set<User> participants = expense.getParticipants();

      // Рассчитать сумму, которую каждый участник должен платить плательщику
      var amountPerParticipant = amount.divide(valueOf(participants.size()), 2, RoundingMode.HALF_UP);

      // Обновить балансы
      for (User participant : participants) {
        if (!participant.equals(payer)) {
          balances.computeIfAbsent(payer, m -> new HashMap<>())
            .compute(participant,
              (k, v) -> (v == null ? amountPerParticipant.negate() : v.subtract(amountPerParticipant)));

          balances.computeIfAbsent(participant, m -> new HashMap<>())
            .compute(payer, (k, v) -> (v == null ? amountPerParticipant : v.add(amountPerParticipant)));
        }
      }
    }

//    // Удалить взаимоисключающие суммы
//    for (Map<User, BigDecimal> balance : balances.values()) {
//      for (User debtor : new ArrayList<>(balance.keySet())) {
//        BigDecimal debt = balance.get(debtor);
//        if (debt.compareTo(BigDecimal.ZERO) < 0) {
//          BigDecimal payerDebt = debt.negate();
//          for (User creditor : balance.keySet()) {
//            if (creditor != debtor && balance.get(creditor).compareTo(BigDecimal.ZERO) > 0) {
//              BigDecimal creditorDebt = payerDebt.min(balance.get(creditor));
//              balance.put(debtor, payerDebt.subtract(creditorDebt));
//              balance.put(creditor, balance.get(creditor).subtract(creditorDebt));
//              payerDebt = payerDebt.subtract(creditorDebt);
//              if (payerDebt.compareTo(BigDecimal.ZERO) == 0) {
//                balance.remove(debtor);
//                break;
//              }
//            }
//          }
//        }
//      }
//    }

    Map<User, BigDecimal> debtors = new HashMap<>();
    Map<User, BigDecimal> creditors = new HashMap<>();

    for (User payer : balances.keySet()) {
      for (Map.Entry<User, BigDecimal> entry : balances.get(payer).entrySet()) {
        User participant = entry.getKey();
        BigDecimal balance = entry.getValue();

        if (balance.compareTo(BigDecimal.ZERO) < 0) {
          debtors.put(participant, balance.negate());
        } else if (balance.compareTo(BigDecimal.ZERO) > 0) {
          creditors.put(participant, balance);
        }
      }
    }

    // Sort debtors and creditors
    List<User> sortedDebtors = new ArrayList<>(debtors.keySet());
    sortedDebtors.sort((u1, u2) -> debtors.get(u2).compareTo(debtors.get(u1)));

    List<User> sortedCreditors = new ArrayList<>(creditors.keySet());
    sortedCreditors.sort((u1, u2) -> creditors.get(u2).compareTo(creditors.get(u1)));

    // Perform debt settlement
    BigDecimal remainingDebt = BigDecimal.ZERO;
    for (User debtor : sortedDebtors) {
      BigDecimal debtorBalance = debtors.get(debtor);
      if (debtorBalance.compareTo(BigDecimal.ZERO) > 0) {
        continue; // Debtor already settled
      }

      remainingDebt = debtorBalance.abs();
      for (User creditor : sortedCreditors) {
        BigDecimal creditorBalance = creditors.get(creditor);
        if (creditorBalance.compareTo(BigDecimal.ZERO) <= 0) {
          continue; // Creditor already settled
        }

        BigDecimal amountToSettle = remainingDebt.min(creditorBalance);
        debtorBalance = debtorBalance.add(amountToSettle);
        creditorBalance = creditorBalance.subtract(amountToSettle);

        remainingDebt = remainingDebt.subtract(amountToSettle);

        if (debtorBalance.compareTo(BigDecimal.ZERO) == 0) {
          debtors.remove(debtor);
          break;
        }

        if (creditorBalance.compareTo(BigDecimal.ZERO) == 0) {
          creditors.remove(creditor);
        }
      }
    }

    // Handle remaining debt (if any)
    if (remainingDebt.compareTo(BigDecimal.ZERO) > 0) {
      // Implement logic to handle remaining debt.
      // This could involve identifying a "group leader" to cover the remaining debt,
      // splitting the remaining debt among participants, or notifying users
      // about the imbalance.
      log.warn("Remaining debt: {}", remainingDebt);
    }

    if (!debtors.isEmpty()) {
      BigDecimal totalDebt = debtors.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
      BigDecimal adjustmentAmount = totalDebt.divide(BigDecimal.valueOf(debtors.size()), 2, RoundingMode.HALF_UP);

      for (User debtor : debtors.keySet()) {
        BigDecimal newBalance = debtors.get(debtor).add(adjustmentAmount);
        debtors.put(debtor, newBalance);
      }
    }

    // Update balances
    for (User payer : balances.keySet()) {
      if (creditors.containsKey(payer)) {
        balances.get(payer).put(payer, creditors.remove(payer));
      }
      if (debtors.containsKey(payer)) {
        balances.get(payer).put(payer, debtors.remove(payer));
      }
    }

    var result = new HashMap<String, Map<String, BigDecimal>>();
    for (User payer : balances.keySet()) {
      var payerName = payer.getName();
      var payerBalances = balances.get(payer);
      var payerBalancesMap = new HashMap<String, BigDecimal>();
      for (User participant : payerBalances.keySet()) {
        payerBalancesMap.put(participant.getName(), payerBalances.get(participant));
      }
      result.put(payerName, payerBalancesMap);
    }

    return result;
  }

}
