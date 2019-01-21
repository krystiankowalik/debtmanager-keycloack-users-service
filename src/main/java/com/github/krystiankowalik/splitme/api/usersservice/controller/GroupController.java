package com.github.krystiankowalik.splitme.api.usersservice.controller;

import com.github.krystiankowalik.splitme.api.usersservice.model.Group;
import com.github.krystiankowalik.splitme.api.usersservice.model.User;
import com.github.krystiankowalik.splitme.api.usersservice.exception.NotGroupMemberException;
import com.github.krystiankowalik.splitme.api.usersservice.exception.GroupNotFoundException;
import com.github.krystiankowalik.splitme.api.usersservice.exception.UserNotFoundException;
import com.github.krystiankowalik.splitme.api.usersservice.exception.GroupAlreadyExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping(value = "/groups", produces = "application/json")
public interface GroupController {

    @GetMapping
    @ResponseBody
    ResponseEntity<List<Group>> getAllGroups(@RequestParam(required = false) String search,
                                             @RequestParam(required = false) boolean detail,
                                             @RequestParam(required = false) Integer first,
                                             @RequestParam(required = false) Integer max) throws GroupNotFoundException, UserNotFoundException;

    @GetMapping(value = "/{id}")
    @ResponseBody
    ResponseEntity<Group> getGroup(@PathVariable String id) throws Exception;

    @GetMapping(value = "/{id}/users")
    @ResponseBody
    ResponseEntity<List<User>> getGroupsUsers(@PathVariable String id) throws Exception;

    @PostMapping
    @ResponseBody
    ResponseEntity<Group> addGroup(@RequestBody Group group, Principal principal) throws GroupNotFoundException, GroupAlreadyExistsException, UserNotFoundException;

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteGroup(@PathVariable String id, Principal principal) throws UserNotFoundException, GroupNotFoundException, NotGroupMemberException;

    @PutMapping(value = "/{id}", consumes = "application/json")
    ResponseEntity<Group> updateGroup(@RequestBody Group group) throws GroupNotFoundException, UserNotFoundException;



}
