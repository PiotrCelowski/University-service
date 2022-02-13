package university.service.domain.program;

import university.service.domain.grade.GradeEntity;

import java.util.ArrayList;

public class SubjectEntity {
    private String subjectName;
    private String subjectDescription;
    private ArrayList<GradeEntity> grades = new ArrayList<>();

    public SubjectEntity(String subjectName, String subjectDescription) {
        this.subjectName = subjectName;
        this.subjectDescription = subjectDescription;
    }

    public SubjectEntity() {

    }

    public String getSubjectName() {
        return this.subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectDescription() {
        return subjectDescription;
    }

    public void setSubjectDescription(String subjectDescription) {
        this.subjectDescription = subjectDescription;
    }

    public void addGrade(GradeEntity grade) {
        grades.add(grade);
    }

    public void removeGrade(GradeEntity grade) {
        grades.removeIf(currentGrade -> currentGrade.getGrade() == grade.getGrade());
    }

    public ArrayList<GradeEntity> getGrades() {
        return grades;
    }
}
