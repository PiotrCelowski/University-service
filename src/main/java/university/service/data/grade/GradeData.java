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
    ArrayList<SubjectEntity> allSubjects = new ArrayList<>();


    @PostConstruct
    private void createDummySubjects() {
        allSubjects.add(new SubjectEntity("Subject1", "aaaa"));
        allSubjects.add(new SubjectEntity("Subject2", "aaa"));
        allSubjects.add(new SubjectEntity("Subject3", "aaa"));
    }
    @PostConstruct
    private void createDummyGrades() {

        allGrades.add(new GradeEntity(new SubjectEntity("Subject1", "aaaa"), new UserEntity("user", "pass", "USER"),  new UserEntity("worker", "pawss", "WORKER"), 5));

    }

    public SubjectEntity getSubjectByName(String parameter) {
        List<SubjectEntity> allSubjects = getAllSubjects();
        for(int i=0; i<allSubjects.size(); i++ ) {
            if(allSubjects.get(i).getSubjectName().equals(parameter)) {
                return allSubjects.get(i);
            }
        }
        return null;
    }
    public List<GradeEntity> loadAllGradesForSubject(SubjectEntity selectedSubject, String username) {
        for (GradeEntity allGrade : allGrades) {
            if (allGrade.getStudentName().equals(username)
                && selectedSubject.getSubjectName().equals(allGrade.getSubject().getSubjectName())
                && !selectedSubject.getGrades().contains(allGrade)) {
                selectedSubject.addGrade(allGrade);
            }
        }
        return selectedSubject.getGrades();
    }

    public List<SubjectEntity> getAllSubjects() {
        return allSubjects;
    }

}
