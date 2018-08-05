package com.github.krystiankowalik.splitme.api.usersservice.util;

import com.github.krystiankowalik.splitme.api.usersservice.model.Group;
import lombok.val;
import org.keycloak.representations.idm.GroupRepresentation;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public final class GroupMapper {

    public static Group from(GroupRepresentation groupRepresentation) {
        val group = new Group();
        BeanUtils.copyProperties(groupRepresentation, group);
        return group;
    }

    public static GroupRepresentation from(Group group) {
        val groupRepresentation = new GroupRepresentation();
        BeanUtils.copyProperties(group, groupRepresentation);
        return groupRepresentation;
    }

    public static List<Group> from(List<GroupRepresentation> groupRepresentations) {
        val groups = new ArrayList<Group>();
        for (GroupRepresentation groupRepresentation : groupRepresentations) {
            groups.add(from(groupRepresentation));
        }
        return groups;
    }




}
