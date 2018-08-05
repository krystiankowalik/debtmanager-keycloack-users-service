package com.github.krystiankowalik.splitme.api.usersservice.controller;

import com.github.krystiankowalik.splitme.api.usersservice.model.Invitation;
import com.github.krystiankowalik.splitme.api.usersservice.exception.GroupNotFoundException;
import com.github.krystiankowalik.splitme.api.usersservice.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping(value = "/me/invitations", produces = "application/json")
public interface UserInvitationController {

    @GetMapping
    @ResponseBody
    ResponseEntity<List<Invitation>> getInvitations(Principal principal);

    @GetMapping("/{invitationId}")
    @ResponseBody
    ResponseEntity<Invitation> getInvitation(@PathVariable String invitationId, Principal principal);

    @PutMapping("/{invitationId}")
    void processInvitation(@PathVariable String invitationId, boolean accepted, Principal principal) throws GroupNotFoundException, UserNotFoundException;

    @DeleteMapping("/{invitationId}")
    void deleteInvitation(@PathVariable String invitationId, Principal principal) throws GroupNotFoundException, UserNotFoundException;
}
