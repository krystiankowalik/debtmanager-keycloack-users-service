package com.github.krystiankowalik.splitme.api.usersservice.service;

import com.github.krystiankowalik.splitme.api.usersservice.exception.*;
import com.github.krystiankowalik.splitme.api.usersservice.model.MapUsersGroup;
import com.github.krystiankowalik.splitme.api.usersservice.model.User;
import com.github.krystiankowalik.splitme.api.usersservice.model.Group;

import java.security.Principal;
import java.util.List;

public interface GroupService {

    List<Group> getAllGroups(String search,
                             boolean detail,
                             Integer first,
                             Integer max) throws GroupNotFoundException, UserNotFoundException;

    Group getGroup(String id) throws GroupNotFoundException, UserNotFoundException;

    List<User> getGroupsUsers(String groupId) throws GroupNotFoundException, UserNotFoundException;


    Group addGroup(Group group, Principal principal) throws GroupNotFoundException, GroupAlreadyExistsException, UserNotFoundException;


    void deleteGroup(String id, Principal principal) throws UserNotFoundException, GroupNotFoundException, NotGroupMemberException;

    boolean groupContainsUser(String groupId, String userId) throws UserNotFoundException, GroupNotFoundException;

    Group addUser(String groupId, String userId) throws GroupNotFoundException, UserNotFoundException, AlreadyGroupMemberException;

    Group updateGroup(Group group) throws GroupNotFoundException, UserNotFoundException;

    boolean groupExists(String id) throws GroupNotFoundException;


    void checkGroupMembership(String groupId, Principal principal) throws UserNotFoundException, GroupNotFoundException, NotGroupMemberException;

    List<Group> getGroupsByUserId(String userId);
}
