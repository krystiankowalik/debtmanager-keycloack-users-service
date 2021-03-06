package com.github.krystiankowalik.splitme.api.usersservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "groups")
@Proxy(lazy = false)
public class Group {

    @Id
    @Column(name = "group_id")
    private String id;
    @Column(name = "group_name")
    private String name;
    @Transient
    private List<User> users;

}
