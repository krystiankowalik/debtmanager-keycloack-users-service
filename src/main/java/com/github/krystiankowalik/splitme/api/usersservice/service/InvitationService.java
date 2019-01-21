package com.github.krystiankowalik.splitme.api.usersservice.service;

import com.github.krystiankowalik.splitme.api.usersservice.exception.AlreadyGroupMemberException;
import com.github.krystiankowalik.splitme.api.usersservice.exception.GroupNotFoundException;
import com.github.krystiankowalik.splitme.api.usersservice.exception.InvitationAlreadyExistsException;
import com.github.krystiankowalik.splitme.api.usersservice.exception.UserNotFoundException;
import com.github.krystiankowalik.splitme.api.usersservice.model.Invitation;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public interface InvitationService {

    Invitation add(Invitation invitation) throws InvitationAlreadyExistsException, UserNotFoundException, GroupNotFoundException, AlreadyGroupMemberException;

    Invitation find(String id) throws GroupNotFoundException, UserNotFoundException;

    List<Invitation> findInvitationsByGroupId(String groupId) throws GroupNotFoundException, UserNotFoundException;

    List<Invitation> getInvitationsByJoinerId(String joinerId) throws GroupNotFoundException, UserNotFoundException;

    Invitation getInvitationsByJoinerIdAndInvitationId(String joinerId, String invitationId) throws GroupNotFoundException, UserNotFoundException;

    boolean exists(Invitation invitation);

    List<Invitation> getInvitations() throws GroupNotFoundException, UserNotFoundException;

    boolean delete(Invitation invitation);

    boolean delete(String invitationId) throws GroupNotFoundException, UserNotFoundException;

    void processInvitation(String invitationId, boolean accepted, Principal principal) throws GroupNotFoundException, UserNotFoundException, AlreadyGroupMemberException;
}
