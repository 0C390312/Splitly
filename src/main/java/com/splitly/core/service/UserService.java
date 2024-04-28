package com.splitly.core.service;

import com.splitly.core.dto.User;
import com.splitly.core.repository.UserRepository;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public User createUser(User userDto) {
    return userRepository.save(userDto);
  }

  public List<User> findUsersByIds(List<UUID> ids) {
    return userRepository.findByIdIn(ids);
  }

  public User findUserByName(String name) {
    return userRepository.findByName(name).stream().findFirst().orElse(null);
  }

  public User findUserById(UUID id) {
    return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
  }
}
