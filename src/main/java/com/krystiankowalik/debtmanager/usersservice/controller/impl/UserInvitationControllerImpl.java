package com.krystiankowalik.debtmanager.usersservice.controller.impl;

import com.krystiankowalik.debtmanager.usersservice.controller.UserInvitationController;
import com.krystiankowalik.debtmanager.usersservice.exception.GroupNotFoundException;
import com.krystiankowalik.debtmanager.usersservice.exception.UserNotFoundException;
import com.krystiankowalik.debtmanager.usersservice.model.Invitation;
import com.krystiankowalik.debtmanager.usersservice.service.InvitationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
public class UserInvitationControllerImpl implements UserInvitationController {

    private final InvitationService invitationService;

    @Override
    public ResponseEntity<List<Invitation>> getInvitations(Principal principal) {
        return new ResponseEntity<>(invitationService.getInvitationsByJoinerId(principal.getName()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Invitation> getInvitation(@PathVariable String invitationId, Principal principal) {
        return new ResponseEntity<>(invitationService.getInvitationsByJoinerIdAndInvitationId(principal.getName(), invitationId), HttpStatus.OK);
    }

    @Override
    public void processInvitation(@PathVariable String invitationId, boolean accepted,Principal principal) throws GroupNotFoundException, UserNotFoundException {
        invitationService.processInvitation(invitationId,accepted,principal);
    }

    @Override
    public void deleteInvitation(@PathVariable String invitationId, Principal principal) throws GroupNotFoundException, UserNotFoundException {
        Invitation invitationToDelete = invitationService.getInvitationsByJoinerIdAndInvitationId(principal.getName(), invitationId);
        invitationService.delete(invitationToDelete);
    }
}
