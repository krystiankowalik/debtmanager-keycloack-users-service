package com.github.krystiankowalik.splitme.api.usersservice.service.impl;

import com.github.krystiankowalik.splitme.api.usersservice.db.GroupRepositiory;
import com.github.krystiankowalik.splitme.api.usersservice.db.MapUsersGroupRepositiory;
import com.github.krystiankowalik.splitme.api.usersservice.exception.GroupAlreadyExistsException;
import com.github.krystiankowalik.splitme.api.usersservice.exception.GroupNotFoundException;
import com.github.krystiankowalik.splitme.api.usersservice.exception.NotGroupMemberException;
import com.github.krystiankowalik.splitme.api.usersservice.exception.UserNotFoundException;
import com.github.krystiankowalik.splitme.api.usersservice.model.Group;
import com.github.krystiankowalik.splitme.api.usersservice.model.MapUsersGroup;
import com.github.krystiankowalik.splitme.api.usersservice.model.User;
import com.github.krystiankowalik.splitme.api.usersservice.service.GroupService;
import com.github.krystiankowalik.splitme.api.usersservice.service.UserService;
import com.github.krystiankowalik.splitme.api.usersservice.util.GroupMapper;
import com.github.krystiankowalik.splitme.api.usersservice.util.UtilKt;
import com.sun.xml.internal.bind.v2.util.CollisionCheckStack;
import lombok.AllArgsConstructor;
import lombok.val;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.Response;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional("jpaTransactionManager")
public class GroupServiceMySqlImpl implements GroupService {


    private final RealmResource realm;
    private final UserService userService;
    private final MapUsersGroupRepositiory mapUsersGroupRepositiory;
    private final GroupRepositiory groupRepositiory;


    @Override
    public List<Group> getAllGroups(String search, boolean detail, Integer first, Integer max) throws GroupNotFoundException, UserNotFoundException {

        val groupsFromRepo = groupRepositiory.findAll();

        val groups = new ArrayList<Group>();

        if (groupsFromRepo.size() > 0) {

            val userIds = groupsFromRepo
                    .stream()
                    .map(g -> mapUsersGroupRepositiory.findAllByGroupId(g.getId()))
                    .flatMap(Collection::stream)
                    .map(MapUsersGroup::getUserId)
                    .collect(Collectors.toList());
            System.out.println(userIds);

            val users = userService.getUsers(userIds);

            System.out.println(users);

            groupsFromRepo.forEach(g -> {

                val currentGroupUsers = users.stream()
                        .filter(u ->
                                mapUsersGroupRepositiory.findAllByGroupId(g.getId()).stream()
                                        .map(MapUsersGroup::getUserId)
                                        .collect(Collectors.toList())
                                        .contains(u.getId()))
                        .collect(Collectors.toList());
                System.out.println(currentGroupUsers);

                g.setUsers(currentGroupUsers);

                groups.add(g);
            });
        }

        return groups;
    }

    @Override
    public Group getGroup(String id) throws GroupNotFoundException, UserNotFoundException {
        groupExists(id);
        val group = groupRepositiory.getOne(id);
        val users = userService.getUsers(mapUsersGroupRepositiory
                .findAllByGroupId(group.getId())
                .stream()
                .map(MapUsersGroup::getUserId)
                .collect(Collectors.toList()));
        group.setUsers(users);
        return group;
    }

    @Override
    public List<User> getGroupsUsers(String groupId) throws GroupNotFoundException, UserNotFoundException {
        groupExists(groupId);
        return getGroup(groupId).getUsers();
    }

    @Override
    public Group addGroup(Group group, Principal principal) throws GroupNotFoundException, GroupAlreadyExistsException, UserNotFoundException {

        val newGroupId = UtilKt.generateRandomId();

        group.setId(newGroupId);

        val mapUsersGroup = new MapUsersGroup(UtilKt.generateRandomId(), newGroupId, principal.getName());

        mapUsersGroupRepositiory.save(mapUsersGroup);
        groupRepositiory.save(group);
        return getGroup(newGroupId);
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

        val group = groupRepositiory.getOne(groupId);
//        val mapUsersGroup = mapUsersGroupRepositiory.findAllByGroupId(group.getId());

        mapUsersGroupRepositiory.save(new MapUsersGroup(UtilKt.generateRandomId(), group.getId(), userId));


        return getGroup(groupId);
    }

    @Override
    public Group updateGroup(Group group) throws GroupNotFoundException, UserNotFoundException {
        groupExists(group.getId());
        val groupRepresentation = GroupMapper.from(group);
        realm.groups().group(group.getId()).update(groupRepresentation);
        return getGroup(group.getId());
    }

    @Override
    public boolean groupExists(String id) throws GroupNotFoundException {

        boolean exists;

        exists = groupRepositiory.existsById(id);

        if (!exists) {
            throw new GroupNotFoundException("No group with id: " + id + " has been found.");
        }

        return exists;

    }

    @Override
    public void checkGroupMembership(String groupId, Principal principal) throws UserNotFoundException, GroupNotFoundException, NotGroupMemberException {
        if (!groupContainsUser(groupId, principal.getName())) {
            throw new NotGroupMemberException("Not Group Member");
        }
    }

    @Override
    public List<Group> getGroupsByUserId(String userId) {
        return mapUsersGroupRepositiory
                .findAllByUserId(userId).stream()
                .map(MapUsersGroup::getGroupId)
                .map(groupRepositiory::getOne)
                .collect(Collectors.toList());
    }
}
