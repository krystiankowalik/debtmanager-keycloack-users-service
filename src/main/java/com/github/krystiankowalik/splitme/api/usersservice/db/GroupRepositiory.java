package com.github.krystiankowalik.splitme.api.usersservice.db;


import com.github.krystiankowalik.splitme.api.usersservice.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepositiory extends JpaRepository<Group, String> {

}
