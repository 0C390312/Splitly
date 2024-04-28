package com.splitly.core.converter;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

import com.splitly.core.dto.Group;
import com.splitly.core.dto.User;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface GroupMapper {

  Group toDto(com.splitly.core.model.Group model);

  com.splitly.core.model.Group toModel(Group dto);

  default java.util.UUID map(com.splitly.core.model.User user) {
    return user.getId();
  }

  default com.splitly.core.model.User map(java.util.UUID id) {
    return new com.splitly.core.model.User().id(id);
  }

  default List<UUID> map(List<User> value) {
    return value.stream().map(User::getId).collect(Collectors.toList());
  }

  default List<User> mapUser(List<UUID> ids) {
    return emptyIfNull(ids).stream().map(id -> User.builder().id(id).build()).collect(Collectors.toList());
  }
}
