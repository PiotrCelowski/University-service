package university.service.domain.identity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class GroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long groupId;
    private String groupName;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "USER_GROUP",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "groupId"))
    List<BaseUser> groupMembers;

    public GroupEntity() {
        this.groupName="default";
        groupMembers = new ArrayList<>();
    }

    public GroupEntity(String groupName) {
        this.groupName = groupName;
        groupMembers = new ArrayList<>();
    }

    public Long getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<BaseUser> getGroupMemebers() {
        return groupMembers;
    }

    public void addUserToGroup(BaseUser user) {
        groupMembers.add(user);
    }

    public void removeUserFromGroup(BaseUser user) {
        groupMembers.removeIf(member -> member.getUsername().equals(user.getUsername()));
    }
}

