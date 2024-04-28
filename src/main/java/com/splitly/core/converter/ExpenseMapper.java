package com.splitly.core.converter;

import com.splitly.core.dto.Expense;
import com.splitly.core.dto.Group;
import com.splitly.core.dto.User;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

  @Mapping(target = "payer", source = "model.payerId")
  @Mapping(source = "groupId", target = "group")
  com.splitly.core.dto.Expense toDto(com.splitly.core.model.Expense model, UUID groupId);

  @Mapping(target = "payerId", source = "payer")
  com.splitly.core.model.Expense toModel(com.splitly.core.dto.Expense dto);

  List<com.splitly.core.model.Expense> toModel(Set<Expense> dto);

  default java.util.UUID map(com.splitly.core.model.User user) {
    return user.getId();
  }

  default User map(UUID payerId) {
    return User.builder().id(payerId).build();
  }

  default Group mapGr(UUID groupId) {
    return Group.builder().id(groupId).build();
  }

  default UUID map(User payer) {
    return payer.getId();
  }

  default List<UUID> map(List<User> value) {
    return value.stream().map(User::getId).collect(Collectors.toList());
  }

  default UUID map(Group value) {
    return value.getId();
  }
}
