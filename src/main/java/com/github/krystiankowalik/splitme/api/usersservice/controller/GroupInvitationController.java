package com.github.krystiankowalik.splitme.api.usersservice.controller;

import com.github.krystiankowalik.splitme.api.usersservice.exception.*;
import com.github.krystiankowalik.splitme.api.usersservice.model.Invitation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping(value = "/groups", produces = "application/json")
public interface GroupInvitationController {



    @GetMapping("/{groupId}/invitations")
    @ResponseBody
    ResponseEntity<List<Invitation>> getInvitations(@PathVariable String groupId, Principal principal)
            throws UserNotFoundException, GroupNotFoundException, NotGroupMemberException;

    @GetMapping("/{groupId}/invitations/{invitationId}")
    @ResponseBody
    ResponseEntity<Invitation> getInvitation(@PathVariable String groupId, @PathVariable String invitationId, Principal principal)
            throws UserNotFoundException, GroupNotFoundException, NotGroupMemberException;

    @PostMapping("/{groupId}/invitations")
    @ResponseBody
    ResponseEntity<Invitation> sendJoinGroupRequest(@PathVariable String groupId,
                                                    @RequestParam String userId,
                                                    Principal principal)
            throws GroupNotFoundException, UserNotFoundException, NotGroupMemberException, InvitationAlreadyExistsException, AlreadyGroupMemberException;

    @PutMapping("/{groupId}/invitations/{invitationId}")
    void processInvitation(@PathVariable String groupId, @PathVariable String invitationId,
                           @RequestParam boolean accepted, Principal principal)
            throws GroupNotFoundException, UserNotFoundException, NotGroupMemberException, AlreadyGroupMemberException;
}
