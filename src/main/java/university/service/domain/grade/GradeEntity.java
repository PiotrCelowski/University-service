package university.service.domain.grade;

import org.springframework.stereotype.Component;
import university.service.domain.identity.UserEntity;

public class GradeEntity {
    private UserEntity student;
    private UserEntity teacher;
    private int grade;

    public UserEntity getStudent() {
        return student;
    }

    public UserEntity getTeacher() {
        return teacher;
    }

    public int getGrade() {
        return grade;
    }
}
