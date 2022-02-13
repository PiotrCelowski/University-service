package university.service.application.grade;

import org.springframework.stereotype.Component;
import university.service.data.grade.GradeData;
import university.service.domain.grade.GradeEntity;
import university.service.domain.program.SubjectEntity;
import java.util.List;

@Component
public class GradeUseCase {
    private GradeData gradeData;

    public GradeUseCase(GradeData gradeData) {
        this.gradeData = gradeData;
    }

    public List<GradeEntity> loadAllGradesForSubject(SubjectEntity selectedSubject, String username) {
        return gradeData.loadAllGradesForSubject(selectedSubject, username);
    }
}
