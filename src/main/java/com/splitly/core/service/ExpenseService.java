package com.splitly.core.service;

import com.splitly.core.dto.Expense;
import com.splitly.core.repository.ExpenseRepository;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenseService {

  private final ExpenseRepository expenseRepository;

  public Expense createExpense(Expense expenseDto) {
    return expenseRepository.save(expenseDto);
  }

  public List<Expense> findExpensesByPayerId(UUID payerId) {
    return expenseRepository.findByPayerId(payerId);
  }

  public Expense getByPayerIdAndGroupId(UUID groupId, UUID expenseId) {
    return expenseRepository.findByGroupIdAndId(groupId, expenseId)
      .orElseThrow(() -> new NotFoundException("Expense not found"));
  }

  public Set<Expense> findByGroupId(UUID groupId) {
    return expenseRepository.findByGroupId(groupId);
  }
}
