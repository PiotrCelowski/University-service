package university.service.domain.identity;

public class BaseUserDecorator implements UserEntity {
    protected BaseUser baseUserWrapped;

    public BaseUserDecorator(BaseUser baseUser) {
        this.baseUserWrapped = baseUser;
    }

    public BaseUser getBaseUser() {
        return this.baseUserWrapped;
    }

    public void setRole(String role) {
        this.baseUserWrapped.setRole(role);
    }

    @Override
    public void setUsername(String name) {
        this.baseUserWrapped.setUsername(name);
    }

    @Override
    public void setUserPassword(String userPassword) {
        this.baseUserWrapped.setUserPassword(userPassword);
    }

    @Override
    public String generatePassword() {
        return baseUserWrapped.generatePassword();
    }

    @Override
    public String getUsername() {
        return baseUserWrapped.getUsername();
    }

    @Override
    public String getUserPassword() {
        return baseUserWrapped.getUserPassword();
    }

    @Override
    public String getRole() {
        return baseUserWrapped.getRole();
    }

    @Override
    public Long getUserId() {
        return baseUserWrapped.getUserId();
    }
}
