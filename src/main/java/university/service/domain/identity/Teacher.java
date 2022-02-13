package university.service.domain.identity;

public class Teacher extends BaseUserDecorator {

    public Teacher(BaseUser user) {
        super(user);
        this.setRole("WORKER");
    }

    @Override
    public void setRole(String role) {
        this.baseUserWrapped.setRole(role);
    }

    @Override
    public String getUsername() {
        return this.baseUserWrapped.getUsername();
    }

    @Override
    public String getUserPassword() {
        return this.baseUserWrapped.getUserPassword();
    }

    @Override
    public String getRole() {
        return this.baseUserWrapped.getRole();
    }

    @Override
    public Long getUserId() {
        return this.baseUserWrapped.getUserId();
    }
}
