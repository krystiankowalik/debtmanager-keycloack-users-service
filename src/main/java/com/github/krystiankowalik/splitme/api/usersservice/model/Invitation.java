package com.github.krystiankowalik.splitme.api.usersservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invitation {

    @Id
    private String id;

    private Group group;
    private User joiner;
    private User initiator;
    private boolean accepted;
}
