package com.github.krystiankowalik.splitme.api.usersservice.service.impl;

import com.github.krystiankowalik.splitme.api.usersservice.db.InvitationDtoRepositiory;
import com.github.krystiankowalik.splitme.api.usersservice.exception.AlreadyGroupMemberException;
import com.github.krystiankowalik.splitme.api.usersservice.exception.GroupNotFoundException;
import com.github.krystiankowalik.splitme.api.usersservice.exception.InvitationAlreadyExistsException;
import com.github.krystiankowalik.splitme.api.usersservice.exception.UserNotFoundException;
import com.github.krystiankowalik.splitme.api.usersservice.model.Invitation;
import com.github.krystiankowalik.splitme.api.usersservice.model.dto.InvitationDto;
import com.github.krystiankowalik.splitme.api.usersservice.service.GroupService;
import com.github.krystiankowalik.splitme.api.usersservice.service.InvitationService;
import com.github.krystiankowalik.splitme.api.usersservice.service.UserService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional("jpaTransactionManager")
public class InvitationServiceMySqlImpl implements InvitationService {

    private final InvitationDtoRepositiory invitationRepositiory;
    private final UserService userService;
    private final GroupService groupService;

    @Override
    public Invitation add(Invitation invitation) throws InvitationAlreadyExistsException, UserNotFoundException, GroupNotFoundException, AlreadyGroupMemberException {
        if (exists(invitation)) {
            throw new InvitationAlreadyExistsException("Invitation with those parameters already exists under id: " + invitationRepositiory.getInvitationsByGroupIdAndJoinerId(
                    invitation.getGroup().getId(),
                    invitation.getJoiner().getId()).get(0).getId());
        }
        if (groupService.groupContainsUser(invitation.getGroup().getId(), invitation.getJoiner().getId())) {
            throw new AlreadyGroupMemberException("User: " + invitation.getJoiner().getId() + " is already a member of group: " + invitation.getGroup().getId());
        }
        return save(invitation);
    }

    private Invitation save(Invitation invitation) throws GroupNotFoundException, UserNotFoundException {
        return from(invitationRepositiory.save(from(invitation)));

    }

    @Override
    public Invitation find(String id) throws GroupNotFoundException, UserNotFoundException {
        return from(invitationRepositiory.findByIdIs(id));
    }

    @Override
    public List<Invitation> findInvitationsByGroupId(String groupId) throws GroupNotFoundException, UserNotFoundException {
        return fromDtos(invitationRepositiory.getInvitationsByGroupId(groupId));
    }

    @Override
    public List<Invitation> getInvitationsByJoinerId(String joinerId) throws GroupNotFoundException, UserNotFoundException {
        return fromDtos(invitationRepositiory.getInvitationsByJoinerId(joinerId));
    }

    @Override
    public Invitation getInvitationsByJoinerIdAndInvitationId(String joinerId, String invitationId) throws GroupNotFoundException, UserNotFoundException {
//        return invitationRepositiory.getInvitationByIdAndJoiner_Id(joinerId, invitationId);
        return from(invitationRepositiory.findByIdIs(invitationId));
    }


    @Override
    public boolean exists(Invitation invitation) {
        return invitationRepositiory.getInvitationsByGroupIdAndJoinerId(
                invitation.getGroup().getId(),
                invitation.getJoiner().getId()).size() > 0;
    }


    @Override
    public List<Invitation> getInvitations() throws GroupNotFoundException, UserNotFoundException {
        return fromDtos(invitationRepositiory.findAll());
    }

    @Override
    public boolean delete(Invitation invitation) {
        invitationRepositiory.delete(from(invitation));
        return !exists(invitation);
    }

    @Override
    public boolean delete(String invitationId) throws GroupNotFoundException, UserNotFoundException {
        Invitation invitation = find(invitationId);
        invitationRepositiory.delete(from(invitation));
        return !exists(invitation);
    }

    @Override
    public void processInvitation(String invitationId, boolean accepted, Principal principal) throws GroupNotFoundException, UserNotFoundException, AlreadyGroupMemberException {
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

    private void processGroupAddRequest(Invitation invitation, boolean accepted) throws GroupNotFoundException, UserNotFoundException, AlreadyGroupMemberException {
        if (accepted) {
            groupService.addUser(invitation.getGroup().getId(), invitation.getJoiner().getId());
        }
//        delete(invitation);
        invitation.setAccepted(accepted);

        save(invitation);

    }

    private InvitationDto from(Invitation invitation) {
        return new InvitationDto(
                invitation.getId(),
                invitation.getGroup().getId(),
                invitation.getJoiner().getId(),
                invitation.getInitiator().getId(),
                invitation.isAccepted()
        );
    }

    private Invitation from(InvitationDto invitationDto) throws GroupNotFoundException, UserNotFoundException {
        val invitation = new Invitation();
        invitation.setId(invitationDto.getId());
        invitation.setGroup(groupService.getGroup(invitationDto.getGroupId()));
        invitation.setJoiner(userService.getUser(invitationDto.getJoinerId()));
        invitation.setInitiator(userService.getUser(invitationDto.getInitiatorId()));
        invitation.setAccepted(invitationDto.isAccepted());
        return invitation;
    }

    private List<Invitation> fromDtos(List<InvitationDto> invitationDtos) throws GroupNotFoundException, UserNotFoundException {
        List<Invitation> invitations = new ArrayList<>();
        for (InvitationDto dto : invitationDtos) {
            invitations.add(from(dto));
        }
        return invitations;
    }
/*
    private List<InvitationDto> fromInvites(List<Invitation> invitationDtos) throws GroupNotFoundException, UserNotFoundException {
        List<InvitationDto> invitations = new ArrayList<>();
        for (Invitation invite : invitationDtos) {
            invitations.add(from(invite));
        }
        return invitations;
    }*/
}
