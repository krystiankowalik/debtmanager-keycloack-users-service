package com.github.krystiankowalik.splitme.api.usersservice.controller.impl;

import com.github.krystiankowalik.splitme.api.usersservice.model.Invitation;
import com.github.krystiankowalik.splitme.api.usersservice.service.InvitationService;
import com.github.krystiankowalik.splitme.api.usersservice.controller.UserInvitationController;
import com.github.krystiankowalik.splitme.api.usersservice.exception.GroupNotFoundException;
import com.github.krystiankowalik.splitme.api.usersservice.exception.UserNotFoundException;
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
