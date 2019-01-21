package com.github.krystiankowalik.splitme.api.usersservice.controller.impl;

import com.github.krystiankowalik.splitme.api.usersservice.model.Group;
import com.github.krystiankowalik.splitme.api.usersservice.model.User;
import com.github.krystiankowalik.splitme.api.usersservice.service.GroupService;
import com.github.krystiankowalik.splitme.api.usersservice.controller.GroupController;
import com.github.krystiankowalik.splitme.api.usersservice.exception.GroupAlreadyExistsException;
import com.github.krystiankowalik.splitme.api.usersservice.exception.GroupNotFoundException;
import com.github.krystiankowalik.splitme.api.usersservice.exception.NotGroupMemberException;
import com.github.krystiankowalik.splitme.api.usersservice.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
public class GroupControllerImpl implements GroupController {

    private GroupService groupService;

    @Override
    public ResponseEntity<List<Group>> getAllGroups(@RequestParam(required = false) String search,
                                                    @RequestParam(required = false) boolean detail,
                                                    @RequestParam(required = false) Integer first,
                                                    @RequestParam(required = false) Integer max) throws GroupNotFoundException, UserNotFoundException {
        return new ResponseEntity<>(groupService.getAllGroups(search, detail, first, max), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Group> getGroup(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(groupService.getGroup(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Group> addGroup(@RequestBody Group group, Principal principal) throws GroupNotFoundException, GroupAlreadyExistsException, UserNotFoundException {
        return new ResponseEntity<>(groupService.addGroup(group, principal), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteGroup(@PathVariable String id, Principal principal) throws UserNotFoundException, GroupNotFoundException, NotGroupMemberException {
        groupService.deleteGroup(id, principal);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Group> updateGroup(@RequestBody Group group) throws GroupNotFoundException, UserNotFoundException {
        return new ResponseEntity<>(groupService.updateGroup(group), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<User>> getGroupsUsers(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(groupService.getGroupsUsers(id), HttpStatus.OK);
    }

    /*@Autowired RealmResource realm;

    @GetMapping("/deleteAll")
    public void deleteAll() throws GroupNotFoundException {
        List<Group> groups = groupService.getAllGroups(null, false, null, null);
        for (Group group : groups) {
            realm.groups().group(group.getId()).remove();
        }

    }*/

}
