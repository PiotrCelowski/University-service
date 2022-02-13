package university.service.domain.grade;

import university.service.domain.identity.UserEntity;
import university.service.domain.program.SubjectEntity;

import java.util.ArrayList;

public class GradeEntity {
    private SubjectEntity subject;
    private UserEntity student;
    private UserEntity teacher;
    private int grade;

    public GradeEntity(SubjectEntity subject, UserEntity student, UserEntity teacher, int grade) {
        this.subject = subject;
        this.student = student;
        this.teacher = teacher;
        this.grade = grade;
    }

    public UserEntity getStudent() {
        return student;
    }

    public String getStudentName() {
        return student.getUsername();
    }

    public String getTeacherName() {
        return teacher.getUsername();
    }

    public UserEntity getTeacher() {
        return teacher;
    }

    public int getGrade() {
        return grade;
    }

    public SubjectEntity getSubject(){return subject;}
    

}
