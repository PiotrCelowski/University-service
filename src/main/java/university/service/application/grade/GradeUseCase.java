package university.service.application.grade;

import org.springframework.stereotype.Component;
import university.service.domain.grade.GradeEntity;
import university.service.domain.program.SubjectEntity;

import javax.annotation.PostConstruct;

@Component
public class GradeUseCase {

    public GradeUseCase() {
    }

    public GradeEntity getAllGrades(SubjectEntity selectedSubject) {
        return null;
    }

}
