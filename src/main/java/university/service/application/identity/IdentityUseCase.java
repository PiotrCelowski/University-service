package university.service.application.identity;

import org.springframework.stereotype.Component;
import university.service.data.identity.UserRepository;
import university.service.domain.identity.*;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class IdentityUseCase {
    private UserRepository userRepository;

    public IdentityUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        userRepository.delete(user);
    }

    public List<BaseUser> getAllUsers() {
        return userRepository.findAll();
    }
}
