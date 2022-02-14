package university.service.data.grade;

import org.springframework.stereotype.Component;
import university.service.domain.grade.GradeEntity;
import university.service.domain.identity.UserEntity;
import university.service.domain.program.ProgramEntity;
import university.service.domain.program.SubjectEntity;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class GradeData {
    ArrayList<GradeEntity> allGrades = new ArrayList<>();


    @PostConstruct
    private void createDummyGrades() {

        allGrades.add(new GradeEntity(new SubjectEntity("Subject1", "aaaa"), new UserEntity("user", "pass", "USER"),  new UserEntity("worker", "pawss", "WORKER"), 5));

    }

    public List<GradeEntity> loadAllGradesForSubject(SubjectEntity selectedSubject, String username, String userRole) {
        ArrayList<GradeEntity> grades = new ArrayList<>();

        if(selectedSubject.getGrades().isEmpty()) {
            for (GradeEntity allGrade : allGrades) {
                if (((allGrade.getStudentName().equals(username) && userRole.equals("USER")) || (allGrade.getTeacherName().equals(username) && userRole.equals("WORKER")))
                        && selectedSubject.getSubjectName().equals(allGrade.getSubject().getSubjectName())
                        && !selectedSubject.getGrades().contains(allGrade)) {
                    selectedSubject.addGrade(allGrade);
                }
            }
            return selectedSubject.getGrades();
        }

        if( userRole.equals("WORKER"))
           grades = loadGradesByTeacher(selectedSubject, username);
        else if ( userRole.equals("USER"))
         grades = loadStudentGrades(selectedSubject, username);

        return grades;
    }

    private ArrayList<GradeEntity> loadStudentGrades(SubjectEntity selectedSubject, String username) {
        ArrayList<GradeEntity> grades = new ArrayList<>();
        for (int i = 0; i < selectedSubject.getGrades().size(); i++) {
            if (selectedSubject.getGrades().get(i).getStudentName().equals(username))
                grades.add(selectedSubject.getGrades().get(i));
        }
        return grades;
    }

    private ArrayList<GradeEntity> loadGradesByTeacher(SubjectEntity selectedSubject, String username) {
        ArrayList<GradeEntity> grades = new ArrayList<>();
        for (int i = 0; i < selectedSubject.getGrades().size(); i++) {
            if (selectedSubject.getGrades().get(i).getTeacherName().equals(username))
                grades.add(selectedSubject.getGrades().get(i));
        }
        return grades;
    }

    public void saveGrade(SubjectEntity subjectEntity, GradeEntity gradeEntity) {
        subjectEntity.addGrade(gradeEntity);
    }

    public void deleteGrade(SubjectEntity subjectEntity, GradeEntity gradeEntity) {
        subjectEntity.removeGrade(gradeEntity);
    }


}
