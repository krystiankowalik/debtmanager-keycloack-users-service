package com.krystiankowalik.debtmanager.usersservice.controller;

import com.krystiankowalik.debtmanager.usersservice.model.User;
import com.krystiankowalik.debtmanager.usersservice.exception.UserNotFoundException;
import com.krystiankowalik.debtmanager.usersservice.model.Group;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

//@RequestMapping(value = "/users", produces = "application/json")
public interface UserController {

    @GetMapping
    @ResponseBody
    ResponseEntity<List<User>> getUsers(String userName,
                                        String firstName,
                                        String lastName,
                                        String email,
                                        Integer first,
                                        Integer max,
                                        boolean detail) throws UserNotFoundException;

    @GetMapping(value = "/{id}")
    @ResponseBody
    ResponseEntity<User> getUser(String id) throws UserNotFoundException;

    @GetMapping(value = "/{id}/groups")
    @ResponseBody
    ResponseEntity<List<Group>> getUsersGroup(String id) throws UserNotFoundException;

}
