package university.service.data.identity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import university.service.domain.identity.UserEntity;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);

    @Query("select u from UserEntity u " +
            "where u.username = :searchTerm")
    List<UserEntity> search(@Param("searchTerm") String searchTerm);

}