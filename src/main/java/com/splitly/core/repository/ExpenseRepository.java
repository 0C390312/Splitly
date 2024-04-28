package com.splitly.core.repository;

import com.splitly.core.dto.Expense;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

  List<Expense> findByPayerId(UUID payerId);

  List<Expense> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

  Optional<Expense> findByGroupIdAndId(UUID groupId, UUID expenseId);

  Set<Expense> findByGroupId(UUID groupId);
}
