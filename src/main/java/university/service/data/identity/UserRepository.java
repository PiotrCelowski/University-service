package university.service.data.identity;

import org.springframework.data.jpa.repository.JpaRepository;
import university.service.domain.identity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);

}