package com.krystiankowalik.debtmanager.usersservice.service.impl;

import com.krystiankowalik.debtmanager.usersservice.model.User;
import com.krystiankowalik.debtmanager.usersservice.service.UserService;
import com.krystiankowalik.debtmanager.usersservice.exception.UserNotFoundException;
import com.krystiankowalik.debtmanager.usersservice.model.Group;
import com.krystiankowalik.debtmanager.usersservice.util.GroupMapper;
import com.krystiankowalik.debtmanager.usersservice.util.UserMapper;
import lombok.AllArgsConstructor;
import lombok.val;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {


    private final RealmResource realm;

    @Override
    public List<User> getAllUsers(String userName,
                                  String firstName,
                                  String lastName,
                                  String email,
                                  Integer first,
                                  Integer max,
                                  boolean detail) throws UserNotFoundException {

        List<User> users = getUsers(userName, firstName, lastName, email, first, max);

        if (detail) {
            for (User u : users) {
                u.setGroups(getUsersGroups(u.getId()));
            }
        }

        return users;

    }

    private List<User> getUsers(String username,
                                String firstName,
                                String lastName,
                                String email,
                                Integer first,
                                Integer max) {

        val userRepresentations = realm
                .users()
                .search(username, firstName, lastName, email, first, max);
        return UserMapper.from(userRepresentations);
    }


    @Override
    public User getUser(String id) throws UserNotFoundException {
        userExists(id);
        val user = UserMapper.from(realm.users()
                .get(id)
                .toRepresentation());
        user.setGroups(getUsersGroups(user.getId()));
        return user;
    }

    @Override
    public List<Group> getUsersGroups(String userId) throws UserNotFoundException {
        userExists(userId);
        return GroupMapper.from(realm.users().get(userId).groups());
    }


    @Override
    public boolean userExists(String id) throws UserNotFoundException {
        boolean exists;
        try {
            exists = Objects.nonNull(realm.users().get(id).toRepresentation());
        } catch (NotFoundException e) {
            throw new UserNotFoundException("No user with id: " + id + " has been found.", e);

        }
        return exists;
    }

    @Override
    public void checkAuthorizedUser(String userId, Principal principal) {
        if (!userId.equals(principal.getName())) {
            throw new NotAuthorizedException("User: " + userId + " unauthorized to perform actions on another user's account");
        }
    }


}
