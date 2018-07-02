package com.krystiankowalik.debtmanager.usersservice.controller.impl;

import com.krystiankowalik.debtmanager.usersservice.controller.GroupController;
import com.krystiankowalik.debtmanager.usersservice.exception.GroupAlreadyExistsException;
import com.krystiankowalik.debtmanager.usersservice.exception.GroupNotFoundException;
import com.krystiankowalik.debtmanager.usersservice.exception.NotGroupMemberException;
import com.krystiankowalik.debtmanager.usersservice.exception.UserNotFoundException;
import com.krystiankowalik.debtmanager.usersservice.model.Group;
import com.krystiankowalik.debtmanager.usersservice.model.User;
import com.krystiankowalik.debtmanager.usersservice.service.GroupService;
import lombok.AllArgsConstructor;
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

    private final GroupService groupService;

    @Override
    public ResponseEntity<List<Group>> getAllGroups(@RequestParam(required = false) String search,
                                                    @RequestParam(required = false) boolean detail,
                                                    @RequestParam(required = false) Integer first,
                                                    @RequestParam(required = false) Integer max) throws GroupNotFoundException {
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
    public ResponseEntity<Group> updateGroup(@RequestBody Group group) throws GroupNotFoundException {
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
