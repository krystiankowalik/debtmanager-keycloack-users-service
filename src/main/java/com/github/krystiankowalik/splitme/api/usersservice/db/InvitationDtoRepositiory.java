package com.github.krystiankowalik.splitme.api.usersservice.db;


import com.github.krystiankowalik.splitme.api.usersservice.model.Invitation;
import com.github.krystiankowalik.splitme.api.usersservice.model.dto.InvitationDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationDtoRepositiory extends JpaRepository<InvitationDto, String> {

    InvitationDto findByIdIs(String id);

    List<InvitationDto> getInvitationsByGroupIdAndInitiatorIdAndJoinerId(String groupId, String initiatorId, String joinerId);

    List<InvitationDto> getInvitationsByGroupId(String groupId);

    List<InvitationDto> getInvitationsByJoinerId(String joinerId);

    List<InvitationDto> getInvitationsByJoinerIdAndId(String joinerId, String invitationId);

    InvitationDto getInvitationByIdAndJoinerId(String invitationId, String joinerId);

    List<InvitationDto> getInvitationsByGroupIdAndJoinerId(String id, String id1);
}
