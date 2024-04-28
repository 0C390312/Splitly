package com.splitly.core.api;

import static org.springframework.http.HttpStatus.CREATED;

import com.splitly.core.converter.GroupMapper;
import com.splitly.core.model.Group;
import com.splitly.core.service.GroupService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class GroupController implements GroupApi {

  private final GroupService groupService;
  private final GroupMapper groupMapper;

  @Override
  public ResponseEntity<Group> addUserToGroup(UUID groupId, List<UUID> users) {
    var response = groupService.addUsersToGroup(groupId, users);
    return ResponseEntity.ok().body(groupMapper.toModel(response));
  }

  @Override
  public ResponseEntity<Group> createGroup(Group group) {
    var response = groupService.createGroup(groupMapper.toDto(group));
    return ResponseEntity.status(CREATED).body(groupMapper.toModel(response));
  }

  @Override
  public ResponseEntity<Group> getGroup(UUID groupId) {
    var groupDto = groupService.getById(groupId);
    var group = groupMapper.toModel(groupDto);
    return ResponseEntity.ok().body(group);
  }
}
