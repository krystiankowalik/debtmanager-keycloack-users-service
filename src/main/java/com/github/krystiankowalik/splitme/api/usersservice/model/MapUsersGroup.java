package com.github.krystiankowalik.splitme.api.usersservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "map_users_group")
public class MapUsersGroup {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "group_id")
    private String groupId;
    @Column(name = "user_id")
    private String userId;

}
