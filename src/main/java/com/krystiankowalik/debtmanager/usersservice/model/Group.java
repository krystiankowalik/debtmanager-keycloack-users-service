package com.krystiankowalik.debtmanager.usersservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group {

    private String id;
    private String name;
    private List<User> users;

}
