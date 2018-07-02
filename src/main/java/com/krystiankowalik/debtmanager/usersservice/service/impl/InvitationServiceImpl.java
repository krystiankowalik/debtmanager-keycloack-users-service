package com.krystiankowalik.debtmanager.usersservice.service.impl;

import com.krystiankowalik.debtmanager.usersservice.exception.GroupNotFoundException;
import com.krystiankowalik.debtmanager.usersservice.service.UserService;
import com.krystiankowalik.debtmanager.usersservice.db.InvitationRepositiory;
import com.krystiankowalik.debtmanager.usersservice.exception.AlreadyGroupMemberException;
import com.krystiankowalik.debtmanager.usersservice.exception.InvitationAlreadyExistsException;
import com.krystiankowalik.debtmanager.usersservice.exception.UserNotFoundException;
import com.krystiankowalik.debtmanager.usersservice.model.Invitation;
import com.krystiankowalik.debtmanager.usersservice.service.GroupService;
import com.krystiankowalik.debtmanager.usersservice.service.InvitationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@AllArgsConstructor
public class InvitationServiceImpl implements InvitationService {

    private final InvitationRepositiory invitationRepositiory;
    private final UserService userService;
    private final GroupService groupService;

    @Override
    public Invitation save(Invitation invitation) throws InvitationAlreadyExistsException, UserNotFoundException, GroupNotFoundException, AlreadyGroupMemberException {
        if (exists(invitation)) {
            throw new InvitationAlreadyExistsException("Invitation with those parameters already exists under id: " + invitationRepositiory.getInvitationsByGroup_IdAndJoiner_Id(
                    invitation.getGroup().getId(),
                    invitation.getJoiner().getId()).get(0).getId());
        }
        if(groupService.groupContainsUser(invitation.getGroup().getId(),invitation.getJoiner().getId())){
            throw new AlreadyGroupMemberException("User: "+ invitation.getJoiner().getId() + " is already a member of group: " + invitation.getGroup().getId());
        }
        return invitationRepositiory.save(invitation);
    }

    @Override
    public Invitation find(String id) {
        return invitationRepositiory.findByIdIs(id);
    }

    @Override
    public List<Invitation> findInvitationsByGroupId(String groupId) {
        return invitationRepositiory.getInvitationsByGroup_Id(groupId);
    }

    @Override
    public List<Invitation> getInvitationsByJoinerId(String joinerId) {
        return invitationRepositiory.getInvitationsByJoiner_Id(joinerId);
    }

    @Override
    public Invitation getInvitationsByJoinerIdAndInvitationId(String joinerId, String invitationId) {
//        return invitationRepositiory.getInvitationByIdAndJoiner_Id(joinerId, invitationId);
        return invitationRepositiory.findByIdIs(invitationId);
    }


    @Override
    public boolean exists(Invitation invitation) {
        return invitationRepositiory.getInvitationsByGroup_IdAndJoiner_Id(
                invitation.getGroup().getId(),
                invitation.getJoiner().getId()).size() > 0;
    }


    @Override
    public List<Invitation> getInvitations() {
        return invitationRepositiory.findAll();
    }

    @Override
    public boolean delete(Invitation invitation) {
        invitationRepositiory.delete(invitation);
        return !exists(invitation);
    }

    @Override
    public boolean delete(String invitationId) {
        Invitation invitation = find(invitationId);
        invitationRepositiory.delete(invitation);
        return !exists(invitation);
    }

    @Override
    public void processInvitation(String invitationId, boolean accepted, Principal principal) throws GroupNotFoundException, UserNotFoundException {
        Invitation invitation = find(invitationId);
        if (principal.getName().equals(invitation.getJoiner().getId())
                && !invitation.getInitiator().equals(invitation.getJoiner())) {
            processGroupAddRequest(invitation, accepted);
        }

        if (!principal.getName().equals(invitation.getJoiner().getId())
                && groupService.groupContainsUser(invitation.getGroup().getId(), principal.getName())) {
            processGroupAddRequest(invitation, accepted);
        }
    }

    private void processGroupAddRequest(Invitation invitation, boolean accepted) throws GroupNotFoundException, UserNotFoundException {
        if (accepted) {
            groupService.addUser(invitation.getGroup().getId(), invitation.getJoiner().getId());
            delete(invitation);
        } else {
            delete(invitation);
        }

    }
}
