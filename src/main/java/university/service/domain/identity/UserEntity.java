package university.service.domain.identity;

public interface UserEntity {
    String generatePassword();
    String getUsername();
    String getUserPassword();
    String getRole();
    Long getUserId();
    void setRole(String role);
    void setUsername(String username);
    void setUserPassword(String userPassword);
}
