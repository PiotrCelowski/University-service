package university.service.domain.program;

public class SubjectEntity {
    private String subjectName;
    private String subjectDescription;

    public SubjectEntity(String subjectName, String subjectDescription) {
        this.subjectName = subjectName;
        this.subjectDescription = subjectDescription;
    }

    public SubjectEntity() {

    }

    public String getSubjectName() {
        return subjectName;
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
}
