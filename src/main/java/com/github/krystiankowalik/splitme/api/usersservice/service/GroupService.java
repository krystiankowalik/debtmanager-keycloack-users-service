package com.github.krystiankowalik.splitme.api.usersservice.service;

import com.github.krystiankowalik.splitme.api.usersservice.exception.GroupAlreadyExistsException;
import com.github.krystiankowalik.splitme.api.usersservice.exception.GroupNotFoundException;
import com.github.krystiankowalik.splitme.api.usersservice.model.User;
import com.github.krystiankowalik.splitme.api.usersservice.exception.NotGroupMemberException;
import com.github.krystiankowalik.splitme.api.usersservice.exception.UserNotFoundException;
import com.github.krystiankowalik.splitme.api.usersservice.model.Group;

import java.security.Principal;
import java.util.List;

public interface GroupService {

    List<Group> getAllGroups(String search,
                             boolean detail,
                             Integer first,
                             Integer max) throws GroupNotFoundException;

    Group getGroup(String id) throws GroupNotFoundException;

    List<User> getGroupsUsers(String groupId) throws GroupNotFoundException;


    Group addGroup(Group group, Principal principal) throws GroupNotFoundException, GroupAlreadyExistsException, UserNotFoundException;


    void deleteGroup(String id, Principal principal) throws UserNotFoundException, GroupNotFoundException, NotGroupMemberException;

    boolean groupContainsUser(String groupId, String userId) throws UserNotFoundException, GroupNotFoundException;

    Group addUser(String groupId, String userId) throws GroupNotFoundException, UserNotFoundException;

    Group updateGroup(Group group) throws GroupNotFoundException;

    boolean groupExists(String id) throws GroupNotFoundException;


    void checkGroupMembership(String groupId, Principal principal) throws UserNotFoundException, GroupNotFoundException, NotGroupMemberException;
}
