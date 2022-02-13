package university.service.data.identity;

import org.springframework.data.jpa.repository.JpaRepository;
import university.service.domain.identity.GroupEntity;

public interface GroupRepository  extends JpaRepository<GroupEntity, Long> {
}
