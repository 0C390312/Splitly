package com.splitly.core.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "expenses")
public class Expense {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "payer_id")
  private User payer;

  private BigDecimal amount;

  private String currency;

  @ManyToMany
  @JoinTable(
    name = "expense_users",
    joinColumns = @JoinColumn(name = "expense_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id")
  )
  private Set<User> participants;

  @ManyToOne
  @JoinColumn(name = "group_id")
  private Group group;

  private LocalDateTime createdAt;
}
