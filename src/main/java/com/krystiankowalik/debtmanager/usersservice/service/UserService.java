package com.krystiankowalik.debtmanager.usersservice.service;

import com.krystiankowalik.debtmanager.usersservice.model.User;
import com.krystiankowalik.debtmanager.usersservice.exception.UserNotFoundException;
import com.krystiankowalik.debtmanager.usersservice.model.Group;

import java.security.Principal;
import java.util.List;

public interface UserService {


   /* List<User> getUsers(String userName,
                        String firstName,
                        String lastName,
                        String email,
                        Integer first,
                        Integer max,
                        boolean detail) throws UserNotFoundException;*/

    List<User> getUsers(String userName,
                        String firstName,
                        String lastName,
                        String email,
                        Integer first,
                        Integer max,
                        boolean detail,
                        List<String> ids) throws UserNotFoundException;

    List<User> getUsers(List<String> ids) throws UserNotFoundException;

    User getUser(String id) throws UserNotFoundException;

    List<Group> getUsersGroups(String userId) throws UserNotFoundException;

    boolean userExists(String id) throws UserNotFoundException;


    void checkAuthorizedUser(String userId, Principal principal);
}
