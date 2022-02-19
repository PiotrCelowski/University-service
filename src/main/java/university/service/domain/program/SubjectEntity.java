package university.service.domain.program;

import university.service.domain.identity.GroupEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class SubjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long subjectId;
    private String subjectName;
    private String subjectDescription;
    @ManyToMany(mappedBy = "subjects", fetch = FetchType.EAGER)
    List<ProgramEntity> assignedPrograms = new ArrayList<>();

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

    public void addRelatedProgram(ProgramEntity programEntity) {
        assignedPrograms.add(programEntity);
    }

    public void removeRelatedProgram(ProgramEntity programEntity) {
        assignedPrograms.removeIf(program -> program.getProgramName().equals(programEntity.getProgramName()));
    }
}
