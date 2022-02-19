package university.service.data.program;

import org.springframework.data.jpa.repository.JpaRepository;
import university.service.domain.program.ProgramEntity;

public interface ProgramRepository  extends JpaRepository<ProgramEntity, Long> {

    ProgramEntity findByProgramName(String programName);

}
