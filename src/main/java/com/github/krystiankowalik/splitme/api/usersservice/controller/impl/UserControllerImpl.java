package com.github.krystiankowalik.splitme.api.usersservice.controller.impl;

import com.github.krystiankowalik.splitme.api.usersservice.model.Group;
import com.github.krystiankowalik.splitme.api.usersservice.model.User;
import com.github.krystiankowalik.splitme.api.usersservice.service.UserService;
import com.github.krystiankowalik.splitme.api.usersservice.controller.UserController;
import com.github.krystiankowalik.splitme.api.usersservice.exception.UserNotFoundException;
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
                                               @RequestParam(required = false) boolean detail,
                                               @RequestParam(required = false) List<String> ids) throws UserNotFoundException {
        return new ResponseEntity<>(userService.getUsers(userName, firstName, lastName, email, first, max, detail, ids),
                HttpStatus.OK);
    }

   /* @Override
    public ResponseEntity<List<User>> getUsersByIds(@RequestBody List<String> ids) throws UserNotFoundException {
        return new ResponseEntity<>(userService.getUsers(ids), HttpStatus.OK);
    }*/

    @Override
    public ResponseEntity<User> getUser(@PathVariable String id) throws UserNotFoundException {
        return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Group>> getUsersGroup(@PathVariable String id) throws UserNotFoundException {
        return new ResponseEntity<>(userService.getUsersGroups(id), HttpStatus.OK);
    }

}
