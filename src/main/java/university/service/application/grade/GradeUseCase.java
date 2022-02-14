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

    public List<GradeEntity> loadAllGradesForSubject(SubjectEntity selectedSubject, String username, String userRole) {
        return gradeData.loadAllGradesForSubject(selectedSubject, username, userRole);
    }

    public void saveGrade(SubjectEntity subjectEntity, GradeEntity gradeEntity) {
        gradeData.saveGrade(subjectEntity, gradeEntity);
    }

    public void deleteGrade(SubjectEntity subjectEntity, GradeEntity gradeEntity) {
        gradeData.deleteGrade(subjectEntity, gradeEntity);
    }
}
