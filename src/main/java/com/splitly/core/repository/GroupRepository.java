package com.splitly.core.repository;

import com.splitly.core.dto.Expense;
import com.splitly.core.dto.Group;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, UUID> {

  List<Expense> findByUsersId(UUID userId);
}
