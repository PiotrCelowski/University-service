package university.service.domain.program;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ProgramEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long programId;
    private String programName;
    private String programDescription;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "PROGRAM_SUBJECT",
            joinColumns = @JoinColumn(name = "programId"),
            inverseJoinColumns = @JoinColumn(name = "subjectId"))
    List<SubjectEntity> subjects;

    public ProgramEntity() {
        subjects = new ArrayList<>();
    }

    public ProgramEntity(String name, String description) {
        this.programName = name;
        this.programDescription = description;
        subjects = new ArrayList<>();
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

    public List<SubjectEntity> getSubjects() {
        return subjects;
    }
}
