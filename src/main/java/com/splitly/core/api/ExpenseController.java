package com.splitly.core.api;

import static org.springframework.http.HttpStatus.CREATED;

import com.splitly.core.converter.ExpenseMapper;
import com.splitly.core.model.Expense;
import com.splitly.core.service.ExpenseService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExpenseController implements ExpenseApi{

  private final ExpenseService expenseService;
  private final ExpenseMapper mapper;

  @Override
  public ResponseEntity<Expense> createExpense(UUID groupId, Expense expense) {
    var expenseDto = expenseService.createExpense(mapper.toDto(expense, groupId));
    return ResponseEntity.status(CREATED).body(mapper.toModel(expenseDto));
  }

  @Override
  public ResponseEntity<Expense> getExpenseById(UUID groupId, UUID expenseId) {
    var expenseDto = expenseService.getByPayerIdAndGroupId(groupId, expenseId);
    return ResponseEntity.ok(mapper.toModel(expenseDto));
  }

  @Override
  public ResponseEntity<List<Expense>> getExpenses(UUID groupId) {
    var expenses = expenseService.findByGroupId(groupId);
    return ResponseEntity.ok(mapper.toModel(expenses));
  }
}
