package com.krystiankowalik.debtmanager.usersservice.controller.impl;

import com.krystiankowalik.debtmanager.usersservice.controller.GroupInvitationController;
import com.krystiankowalik.debtmanager.usersservice.exception.*;
import com.krystiankowalik.debtmanager.usersservice.model.Group;
import com.krystiankowalik.debtmanager.usersservice.model.Invitation;
import com.krystiankowalik.debtmanager.usersservice.model.User;
import com.krystiankowalik.debtmanager.usersservice.service.GroupService;
import com.krystiankowalik.debtmanager.usersservice.service.InvitationService;
import com.krystiankowalik.debtmanager.usersservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
public class GroupInvitationControllerImpl implements GroupInvitationController {

    private final InvitationService invitationService;
    private final GroupService groupService;
    private final UserService userService;

    @Override
    public ResponseEntity<List<Invitation>> getInvitations(@PathVariable String groupId, Principal principal)
            throws UserNotFoundException, GroupNotFoundException, NotGroupMemberException {

        groupService.checkGroupMembership(groupId, principal);
        return new ResponseEntity<>(invitationService.findInvitationsByGroupId(groupId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Invitation> getInvitation(@PathVariable String groupId, @PathVariable String invitationId, Principal principal)
            throws UserNotFoundException, GroupNotFoundException, NotGroupMemberException {

        groupService.checkGroupMembership(groupId, principal);
        return new ResponseEntity<>(invitationService.find(invitationId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Invitation> sendJoinGroupRequest(@PathVariable String groupId,
                                                           @RequestParam String userId,
                                                           Principal principal)
            throws GroupNotFoundException,
            UserNotFoundException,
            NotGroupMemberException,
            InvitationAlreadyExistsException, AlreadyGroupMemberException {

        Group group = groupService.getGroup(groupId);
        group.setUsers(null);
        User joiner = userService.getUser(userId);
        joiner.setGroups(null);
        User initiator = userService.getUser(principal.getName());
        initiator.setGroups(null);
        if (!joiner.equals(initiator)) {
            groupService.checkGroupMembership(groupId, principal);
        }
        Invitation invitation = new Invitation(null, group, joiner, initiator);
        return new ResponseEntity<>(invitationService.save(invitation), HttpStatus.CREATED);

    }

    @Override
    public void processInvitation(@PathVariable String groupId, @PathVariable String invitationId, @RequestParam boolean accepted, Principal principal) throws GroupNotFoundException, UserNotFoundException, NotGroupMemberException {
        groupService.checkGroupMembership(groupId, principal);
        invitationService.processInvitation(invitationId, accepted, principal);
    }

}
