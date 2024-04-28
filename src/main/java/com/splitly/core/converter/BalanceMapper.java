package com.splitly.core.converter;

import com.splitly.core.model.Debt;
import com.splitly.core.service.DebtUtils;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BalanceMapper {

//  Debt toDto(com.splitly.core.model. model);

  List<Debt> toModel(List<DebtUtils.Debt> dto);
}
