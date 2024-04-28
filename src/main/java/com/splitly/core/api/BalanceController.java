package com.splitly.core.api;

import com.splitly.core.converter.BalanceMapper;
import com.splitly.core.model.Debt;
import com.splitly.core.service.BalanceService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequiredArgsConstructor
public class BalanceController implements BalanceApi {

  private final BalanceService balanceService;
  private final BalanceMapper mapper;

  @Override
  public ResponseEntity<List<Debt>>getGroupBalances(UUID groupId) {
    var debts = balanceService.calculateDebts(groupId);
    return ResponseEntity.ok(mapper.toModel(debts));
  }
}
