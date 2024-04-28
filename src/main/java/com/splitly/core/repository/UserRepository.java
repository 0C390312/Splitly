package com.splitly.core.repository;

import com.splitly.core.dto.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

  List<User> findByName(String name);

  List<User> findByIdIn(List<UUID> uuids);

  List<User> findByEmail(String email);

  List<User> findByExternalId(String externalId);
}
