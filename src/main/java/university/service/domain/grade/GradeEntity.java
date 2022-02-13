package university.service.domain.grade;

import university.service.domain.identity.BaseUser;

public class GradeEntity {
    private BaseUser student;
    private BaseUser teacher;
    private int grade;

    public BaseUser getStudent() {
        return student;
    }

    public BaseUser getTeacher() {
        return teacher;
    }

    public int getGrade() {
        return grade;
    }
}
