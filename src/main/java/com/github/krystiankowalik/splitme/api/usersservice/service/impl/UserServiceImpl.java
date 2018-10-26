package com.github.krystiankowalik.splitme.api.usersservice.service.impl;

import com.github.krystiankowalik.splitme.api.usersservice.model.Group;
import com.github.krystiankowalik.splitme.api.usersservice.model.User;
import com.github.krystiankowalik.splitme.api.usersservice.service.UserService;
import com.github.krystiankowalik.splitme.api.usersservice.util.GroupMapper;
import com.github.krystiankowalik.splitme.api.usersservice.util.UserMapper;
import com.github.krystiankowalik.splitme.api.usersservice.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import lombok.val;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {


    private final RealmResource realm;

    @Override
    public List<User> getUsers(String userName,
                               String firstName,
                               String lastName,
                               String email,
                               Integer first,
                               Integer max,
                               boolean detail,
                               List<String> ids) throws UserNotFoundException {


        List<User> users;
        if (ids == null) {
            users = getUsers(userName, firstName, lastName, email, first, max);

        } else {
            users = getUsers(ids);
        }

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
        val userRepresentationsExcludingAdmin =userRepresentations
                .stream()
                .filter(u->!u.getUsername()
                        .equals("admin"))
                .collect(Collectors.toList());
        return UserMapper.from(userRepresentationsExcludingAdmin);
    }

    @Override
    public List<User> getUsers(List<String> ids) throws UserNotFoundException {
        val allUsers = getUsers(null, null, null, null, null, null);
        return allUsers.stream().filter(u -> ids.contains(u.getId())).collect(Collectors.toList());

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
