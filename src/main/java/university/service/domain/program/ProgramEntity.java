package university.service.domain.program;

import java.util.ArrayList;

public class ProgramEntity {

    private String programName;
    private String programDescription;
    private ArrayList<SubjectEntity> subjects = new ArrayList<>();

    public ProgramEntity() {
    }

    public ProgramEntity(String name, String description) {
        this.programName = name;
        this.programDescription = description;
    }

    public String getProgramName() {
        return programName;
    }

    public String getProgramDescription() {
        return programDescription;
    }

    public void setProgramName(String name) {
        this.programName = name;
    }

    public void setProgramDescription(String description) {
        this.programDescription = description;
    }

    public void addSubject(SubjectEntity subject) {
        subjects.add(subject);
    }

    public void removeSubject(SubjectEntity subject) {
        subjects.removeIf(currentSubject -> currentSubject.getSubjectName().equals(subject.getSubjectName()));
    }

    public ArrayList<SubjectEntity> getSubjects() {
        return subjects;
    }
}
