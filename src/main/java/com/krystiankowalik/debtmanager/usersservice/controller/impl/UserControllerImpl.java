package com.krystiankowalik.debtmanager.usersservice.controller.impl;

import com.krystiankowalik.debtmanager.usersservice.controller.UserController;
import com.krystiankowalik.debtmanager.usersservice.model.User;
import com.krystiankowalik.debtmanager.usersservice.service.UserService;
import com.krystiankowalik.debtmanager.usersservice.exception.UserNotFoundException;
import com.krystiankowalik.debtmanager.usersservice.model.Group;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) String userName,
                                               @RequestParam(required = false) String firstName,
                                               @RequestParam(required = false) String lastName,
                                               @RequestParam(required = false) String email,
                                               @RequestParam(required = false) Integer first,
                                               @RequestParam(required = false) Integer max,
                                               @RequestParam(required = false) boolean detail) throws UserNotFoundException {
        return new ResponseEntity<>(userService.getAllUsers(userName, firstName, lastName, email, first, max, detail),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> getUser(@PathVariable String id) throws UserNotFoundException {
        return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Group>> getUsersGroup(@PathVariable String id) throws UserNotFoundException {
        return new ResponseEntity<>(userService.getUsersGroups(id), HttpStatus.OK);
    }

}
