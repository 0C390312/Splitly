package com.splitly.core.service;

import com.splitly.core.dto.Group;
import com.splitly.core.repository.GroupRepository;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupService {

  private final GroupRepository groupRepository;
  private final UserService userService;

  public Group createGroup(Group groupDto) {
    return groupRepository.save(groupDto);
  }

  public Group addUsersToGroup(UUID groupId, List<UUID> usersIds) {
    var group = groupRepository.findById(groupId).orElseThrow();
    var users = userService.findUsersByIds(usersIds);
    group.setUsers(users);
    groupRepository.save(group);
    return group;
  }

  public Group getById(UUID groupId) {
    return groupRepository.findById(groupId).orElseThrow(() -> new NotFoundException("Group not found"));
  }
}
