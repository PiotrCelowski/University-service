package university.service.data.program;

import org.springframework.data.jpa.repository.JpaRepository;
import university.service.domain.program.SubjectEntity;

public interface SubjectRepository extends JpaRepository<SubjectEntity, Long> {

    SubjectEntity findBySubjectName(String subjectName);

}
