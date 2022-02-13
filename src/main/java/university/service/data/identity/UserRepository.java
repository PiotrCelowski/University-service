package university.service.data.identity;

import org.springframework.data.jpa.repository.JpaRepository;
import university.service.domain.identity.BaseUser;

public interface UserRepository extends JpaRepository<BaseUser, Long> {

    BaseUser findByUsername(String userLogin);

}