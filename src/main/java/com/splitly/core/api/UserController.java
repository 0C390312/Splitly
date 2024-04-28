package com.splitly.core.api;

import com.splitly.core.converter.UserMapper;
import com.splitly.core.model.User;
import com.splitly.core.service.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

  private final UserService userService;
  private final UserMapper mapper;

  @Override
  public ResponseEntity<User> createUser(User user) {
    var response = userService.createUser(mapper.toDto(user));
    return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toModel(response));
  }

  @Override
  public ResponseEntity<User> getUser(UUID id) {
    var response = userService.findUserById(id);
    return ResponseEntity.ok(mapper.toModel(response));
  }
}
