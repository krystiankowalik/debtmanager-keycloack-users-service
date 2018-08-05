package com.github.krystiankowalik.splitme.api.usersservice.db;


import com.github.krystiankowalik.splitme.api.usersservice.model.Invitation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InvitationRepositiory extends MongoRepository<Invitation, String> {

    Invitation findByIdIs(String id);

    List<Invitation> getInvitationsByGroup_IdAndInitiator_IdAndJoiner_Id(String groupId, String initiatorId, String joinerId);

    List<Invitation> getInvitationsByGroup_Id(String groupId);

    List<Invitation> getInvitationsByJoiner_Id(String joinerId);

    List<Invitation> getInvitationsByJoiner_IdAndId(String joinerId, String invitationId);

    Invitation getInvitationByIdAndJoiner_Id(String invitationId, String joinerId);

    List<Invitation> getInvitationsByGroup_IdAndJoiner_Id(String id, String id1);
}
