package university.service.domain.grade;

import university.service.domain.identity.UserEntity;
import university.service.domain.program.SubjectEntity;


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

    public GradeEntity(){

    }

    public void setSubject(SubjectEntity subject) {
        this.subject = subject;
    }

    public UserEntity getStudent() {
        return student;
    }

    public void setStudent(UserEntity student) {
        this.student = student;
    }

    public UserEntity getTeacher() {
        return teacher;
    }

    public void setTeacher(UserEntity teacher) {
        this.teacher = teacher;
    }

    public String getStudentName() {
        if(student == null)
            return "";
        return student.getUsername();
    }
    public String getTeacherName() {
        if(teacher == null)
            return "";
        return teacher.getUsername();
    }


    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public SubjectEntity getSubject(){return subject;}

    public void setTeacherName(String s) {
        if(teacher == null)
            teacher = new UserEntity(s, "", "WORKER");
        teacher.setUsername(s);
    }
    public void setStudentName(String s) {
        if(student == null)
            student = new UserEntity(s, "", "USER");
        student.setUsername(s);
    }
}
