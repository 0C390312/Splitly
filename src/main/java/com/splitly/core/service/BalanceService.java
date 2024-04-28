package com.splitly.core.service;

import com.splitly.core.service.DebtUtils.Debt;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BalanceService {

  private final ExpenseService expenseService;

  public List<Debt> calculateDebts(UUID groupId) {
    var expenses = expenseService.findByGroupId(groupId);
    return DebtUtils.settleDebts(expenses);
  }
}
