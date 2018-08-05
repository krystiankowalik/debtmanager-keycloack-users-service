package com.github.krystiankowalik.splitme.api.usersservice.util;

import com.github.krystiankowalik.splitme.api.usersservice.model.User;
import lombok.val;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public final class UserMapper {

    public static User from(UserRepresentation userRepresentation) {
        val user = new User();
        BeanUtils.copyProperties(userRepresentation, user);
        return user;
    }

    public static UserRepresentation from(User user) {
        val userRepresentation = new UserRepresentation();
        BeanUtils.copyProperties(user, userRepresentation);
        return userRepresentation;
    }

    public static List<User> from(List<UserRepresentation> userRepresentations) {
        val users = new ArrayList<User>();
        for (UserRepresentation userRepresentation : userRepresentations) {
            users.add(from(userRepresentation));
        }
        return users;
    }


}
