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

    Invitation save(Invitation invitation) throws InvitationAlreadyExistsException, UserNotFoundException, GroupNotFoundException, AlreadyGroupMemberException;

    Invitation find(String id);

    List<Invitation> findInvitationsByGroupId(String groupId);

    List<Invitation> getInvitationsByJoinerId(String joinerId);

    Invitation getInvitationsByJoinerIdAndInvitationId(String joinerId, String invitationId);

    boolean exists(Invitation invitation);

    List<Invitation> getInvitations();

    boolean delete(Invitation invitation);
    boolean delete(String invitationId);

    void processInvitation(String invitationId, boolean accepted, Principal principal) throws GroupNotFoundException, UserNotFoundException;
}
