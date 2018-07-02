package com.krystiankowalik.debtmanager.usersservice.service;

import com.krystiankowalik.debtmanager.usersservice.exception.AlreadyGroupMemberException;
import com.krystiankowalik.debtmanager.usersservice.exception.GroupNotFoundException;
import com.krystiankowalik.debtmanager.usersservice.exception.InvitationAlreadyExistsException;
import com.krystiankowalik.debtmanager.usersservice.exception.UserNotFoundException;
import com.krystiankowalik.debtmanager.usersservice.model.Invitation;
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
