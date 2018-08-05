package com.github.krystiankowalik.splitme.api.usersservice.service.impl;

import com.github.krystiankowalik.splitme.api.usersservice.exception.GroupAlreadyExistsException;
import com.github.krystiankowalik.splitme.api.usersservice.exception.GroupNotFoundException;
import com.github.krystiankowalik.splitme.api.usersservice.model.User;
import com.github.krystiankowalik.splitme.api.usersservice.service.GroupService;
import com.github.krystiankowalik.splitme.api.usersservice.service.UserService;
import com.github.krystiankowalik.splitme.api.usersservice.exception.NotGroupMemberException;
import com.github.krystiankowalik.splitme.api.usersservice.exception.UserNotFoundException;
import com.github.krystiankowalik.splitme.api.usersservice.model.Group;
import com.github.krystiankowalik.splitme.api.usersservice.util.ApiUtil;
import com.github.krystiankowalik.splitme.api.usersservice.util.GroupMapper;
import com.github.krystiankowalik.splitme.api.usersservice.util.UserMapper;
import lombok.AllArgsConstructor;
import lombok.val;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GroupServiceImpl implements GroupService {


    private final RealmResource realm;
    private final UserService userService;

    @Override
    public List<Group> getAllGroups(String search, boolean detail, Integer first, Integer max) throws GroupNotFoundException {
        if (Objects.isNull(max)) {
            max = 1000;
        }
        if (Objects.isNull(first)) {
            first = 0;
        }
        List<Group> groups = getGroups(search, first, max);

        if (detail) {
            groups = addGroupsUsers(groups);
        }

        return groups;
    }

    private List<Group> getGroups(String search, int first, int max) {
        val groupRepresentations = realm.groups().groups(search, first, max);
        return GroupMapper.from(groupRepresentations);
    }

    private List<Group> addGroupsUsers(List<Group> groups) throws GroupNotFoundException {
        for (Group group : groups) {
            group.setUsers(getGroupsUsers(group.getId()));
        }
        return groups;
    }


    @Override
    public Group getGroup(String id) throws GroupNotFoundException {
        groupExists(id);
        val groupRepresentation = realm.groups().group(id).toRepresentation();
        val group = GroupMapper.from(groupRepresentation);
        group.setUsers(getGroupsUsers(group.getId()));
        return group;
    }

    @Override
    public List<User> getGroupsUsers(String groupId) throws GroupNotFoundException {
        groupExists(groupId);
        return UserMapper.from(realm.groups().group(groupId).members());
    }

    @Override
    public Group addGroup(Group group, Principal principal) throws GroupNotFoundException, GroupAlreadyExistsException, UserNotFoundException {
        group.setId(null);
        group.setUsers(null);
        val newGroup = GroupMapper.from(group);

        val response = realm
                .groups()
                .add(newGroup);

        ensureUniqueGroupName(group.getName(), response);

        val groupId = ApiUtil.getIdFromUri(response.getLocation().toString());
        addUserToGroup(principal.getName(), groupId);
        return getGroup(groupId);
    }

    private void ensureUniqueGroupName(String groupName, Response response) throws GroupAlreadyExistsException {
        if (response.getStatus() == HttpStatus.CONFLICT.value()) {
            throw new GroupAlreadyExistsException("There is already a group by name: " + groupName);
        }

    }

    private void addUserToGroup(String userId, String groupId) throws UserNotFoundException, GroupNotFoundException {
        userService.userExists(userId);
        groupExists(groupId);
        realm.users().get(userId).joinGroup(groupId);
    }


    @Override
    public void deleteGroup(String id, Principal principal) throws UserNotFoundException, GroupNotFoundException, NotGroupMemberException {

        if (!groupContainsUser(id, principal.getName())) {
            throw new NotGroupMemberException("User: " + principal.getName() + " is not a member of group: " + id);
        }
        realm.groups().group(id).remove();
    }

    @Override
    public boolean groupContainsUser(String groupId, String userId) throws UserNotFoundException, GroupNotFoundException {
        userService.userExists(userId);
        groupExists(groupId);
        return getGroupsUsers(groupId).stream()
                .map(User::getId)
                .collect(Collectors.toList())
                .contains(userId);

    }

    @Override
    public Group addUser(String groupId, String userId) throws GroupNotFoundException, UserNotFoundException {
        userService.userExists(userId);
        groupExists(groupId);

        realm.users().get(userId).joinGroup(groupId);
        return getGroup(groupId);
    }

    @Override
    public Group updateGroup(Group group) throws GroupNotFoundException {
        groupExists(group.getId());
        val groupRepresentation = GroupMapper.from(group);
        realm.groups().group(group.getId()).update(groupRepresentation);
        return getGroup(group.getId());
    }

    @Override
    public boolean groupExists(String id) throws GroupNotFoundException {
        boolean exists;
        try {
            exists = Objects.nonNull(realm.groups().group(id).toRepresentation().getId());

        } catch (NotFoundException e) {
            throw new GroupNotFoundException("No group with id: " + id + " has been found.", e);
        }

        return exists;

    }

    @Override
    public void checkGroupMembership(String groupId, Principal principal) throws UserNotFoundException, GroupNotFoundException, NotGroupMemberException {
        if (!groupContainsUser(groupId, principal.getName())) {
            throw new NotGroupMemberException("Not Group Member");
        }
    }
}
