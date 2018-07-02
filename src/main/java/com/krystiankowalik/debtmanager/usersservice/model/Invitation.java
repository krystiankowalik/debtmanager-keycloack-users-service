package com.krystiankowalik.debtmanager.usersservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invitation {

    @Id
    private String id;

    private Group group;
    private User joiner;
    private User initiator;
}
