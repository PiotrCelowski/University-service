package university.service.application.identity;

import org.springframework.stereotype.Component;
import university.service.data.identity.GroupRepository;
import university.service.data.identity.UserRepository;
import university.service.domain.identity.*;
import university.service.domain.program.ProgramEntity;
import university.service.ui.users.forms.GroupForm;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class IdentityUseCase {
    private UserRepository userRepository;
    private GroupRepository groupRepository;

    public IdentityUseCase(UserRepository userRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    public void createUser(String login, String role) {
        BaseUserDecorator user;

        switch(role) {
            case "WORKER":
                user = new Teacher(new BaseUser(login));
                break;
            case "ADMIN":
                user = new Admin(new BaseUser(login));
                break;
            default:
                user = new Student(new BaseUser(login));
                break;
        }

        userRepository.saveAndFlush(user.getBaseUser());

    }

    public void removeUser(BaseUser user) {
        user.getAssignedGroups().stream().forEach(group -> {
            group.removeUserFromGroup(user);
            groupRepository.saveAndFlush(group);
        });
        userRepository.delete(user);
    }

    public List<BaseUser> getAllUsers() {
        return userRepository.findAll();
    }

    public List<GroupEntity> getAllGroups() {
        return groupRepository.findAll();
    }

    public void createGroup(String groupName) {
        GroupEntity groupEntity = new GroupEntity(groupName);
        groupRepository.save(groupEntity);
    }

    public void removeGroup(GroupEntity group) {
        groupRepository.delete(group);
    }

    @PostConstruct
    public void createDummyGroup() {
        GroupEntity group = new GroupEntity("Grupa 1");
        groupRepository.save(group);
    }

    public GroupEntity getGroupByName(String parameter) {
        List<GroupEntity> allGroups = getAllGroups();
        for(int i=0; i<allGroups.size(); i++ ) {
            if(allGroups.get(i).getGroupName().equals(parameter)) {
                return allGroups.get(i);
            }
        }
        return null;
    }

    public List<BaseUser> getAllMembersOfGroup(GroupEntity currentGroup) {
        GroupEntity group = getGroupByName(currentGroup.getGroupName());
        return group.getGroupMemebers();
    }

    public void addUserToGroup(String userName, GroupEntity currentGroup) {
        GroupEntity group = getGroupByName(currentGroup.getGroupName());
        BaseUser user = userRepository.findByUsername(userName);
        user.addGroup(group);
        group.addUserToGroup(user);
        groupRepository.saveAndFlush(group);
        userRepository.saveAndFlush(user);
    }

    public void removeUserFromGroup(String userName, GroupEntity currentGroup) {
        GroupEntity group = getGroupByName(currentGroup.getGroupName());
        BaseUser user = userRepository.findByUsername(userName);
        user.removeGroup(group);
        group.removeUserFromGroup(user);
        groupRepository.saveAndFlush(group);
        userRepository.saveAndFlush(user);
    }
}
