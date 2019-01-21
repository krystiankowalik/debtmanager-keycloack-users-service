package com.github.krystiankowalik.splitme.api.usersservice.db;


import com.github.krystiankowalik.splitme.api.usersservice.model.Group;
import com.github.krystiankowalik.splitme.api.usersservice.model.MapUsersGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapUsersGroupRepositiory extends JpaRepository<MapUsersGroup, String> {

    List<MapUsersGroup> findAllByGroupId(String groupId);

    List<MapUsersGroup> findAllByUserId(String userId);

}
