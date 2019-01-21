package com.github.krystiankowalik.splitme.api.usersservice.model.dto;

import com.github.krystiankowalik.splitme.api.usersservice.model.Group;
import com.github.krystiankowalik.splitme.api.usersservice.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "invitation")
public class InvitationDto {

    @Id
    @GenericGenerator(name = "id_generator", strategy = "com.github.krystiankowalik.splitme.api.usersservice.model.StringIdGenerator")
    @GeneratedValue(generator = "id_generator")
    @Column(name = "id")
    private String id;
    @Column(name = "group_id")
    private String groupId;
    @Column(name = "joiner_id")
    private String joinerId;
    @Column(name = "initiator_id")
    private String initiatorId;
    @Column(name = "accepted")
    private boolean accepted;

}
