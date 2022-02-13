package university.service.domain.identity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class BaseUser implements UserEntity {
    protected String username;
    protected String userPassword;
    protected String role;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long userId;
    @ManyToMany(mappedBy = "groupMembers", fetch = FetchType.EAGER)
    List<GroupEntity> assignedGroups = new ArrayList<>();


    public BaseUser() {
    }

    public BaseUser(String username) {
        this.username = username;
        this.role = "";
        this.userPassword = generatePassword();
    }

    public BaseUser(String username, String role) {
        this.username = username;
        this.role = role;
        this.userPassword = generatePassword();
    }

    public BaseUser(String username, String userPassword, String role) {
        this.username = username;
        this.role = role;
        this.userPassword = userPassword;
    }

    @Override
    public String generatePassword() {
        return "{noop}password";
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getUserPassword() {
        return userPassword;
    }

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public List<GroupEntity> getAssignedGroups() {
        return assignedGroups;
    }

    public void addGroup(GroupEntity group) {
        this.assignedGroups.add(group);
    }

    public void removeGroup(GroupEntity currentGroup) {
        this.assignedGroups.removeIf(group -> group.getGroupName().equals(currentGroup.getGroupName()));
    }
}
