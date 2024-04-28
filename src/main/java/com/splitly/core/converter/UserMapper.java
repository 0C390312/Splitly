package com.splitly.core.converter;

import com.splitly.core.dto.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

  User toDto(com.splitly.core.model.User model);

  com.splitly.core.model.User toModel(User dto);
}
