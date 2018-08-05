package com.github.krystiankowalik.splitme.api.usersservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private List<Group> groups;
}
