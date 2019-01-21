package com.github.krystiankowalik.splitme.api.usersservice;

import com.github.krystiankowalik.splitme.api.usersservice.db.InvitationDtoRepositiory;
import com.github.krystiankowalik.splitme.api.usersservice.exception.GroupAlreadyExistsException;
import com.github.krystiankowalik.splitme.api.usersservice.exception.GroupNotFoundException;
import com.github.krystiankowalik.splitme.api.usersservice.exception.UserNotFoundException;
import com.github.krystiankowalik.splitme.api.usersservice.model.dto.InvitationDto;
import com.github.krystiankowalik.splitme.api.usersservice.service.GroupService;
import com.github.krystiankowalik.splitme.api.usersservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional("jpaTransactionManager")
public class ScheduledTask {

//    @Autowired
//    private UserService userService;
//    @Autowired
//    private GroupService groupService;
    @Autowired
    private InvitationDtoRepositiory invitationDtoRepositiory;

    @Scheduled(fixedDelay = 50000)
    void scheduled() throws GroupNotFoundException, GroupAlreadyExistsException, UserNotFoundException {
        System.out.println("I'm scheduled...");
        /*System.out.println(groupRepository.findAll());
//        System.out.println(usersGroupMapRepository.findAll());
        ;
        List<User> users = new ArrayList<>();
        groupService.addGroup(new Group("newgroupid1", "My Group Name", users), () -> "84a686b4-ca6b-4cda-8c63-da7445ec0767");*/

//        invitationDtoRepositiory.add(new InvitationDto("1","2","3","4"));
//        System.out.println(invitationDtoRepositiory.findAll());
        }
}
