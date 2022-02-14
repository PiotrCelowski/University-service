package university.service.application.program;

import org.springframework.stereotype.Component;
import university.service.data.program.ProgramData;
import university.service.domain.program.ProgramEntity;
import university.service.domain.program.SubjectEntity;

import java.util.List;

@Component
public class ProgramUseCase {
    private ProgramData programData;

    public ProgramUseCase(ProgramData programData) {
        this.programData = programData;
    }

    public ProgramEntity createProgram(String name, String description) {
        return new ProgramEntity(name, description);
    }

    public List<ProgramEntity> getAllPrograms() {
        return programData.getAllPrograms();
    }

    public void saveProgram(ProgramEntity programEntity) {
        programData.saveProgram(programEntity);
    }

    public void deleteProgram(ProgramEntity programEntity) {
        programData.deleteProgram(programEntity);
    }

    public ProgramEntity getProgramByName(String parameter) {
        return programData.getProgramByName(parameter);
    }


    public List<SubjectEntity> getAllSubjectForProgram(ProgramEntity currentProgram) {
        return programData.getAllSubjectsForProgram(currentProgram);
    }

    public void saveSubject(SubjectEntity subjectEntity, ProgramEntity programEntity) {
        programData.saveSubject(subjectEntity, programEntity);
    }

    public void deleteSubject(SubjectEntity subjectEntity, ProgramEntity programEntity) {
        programData.deleteSubject(subjectEntity, programEntity);
    }

    public SubjectEntity getSubjectByName(String parameter) {
        return programData.getSubjectByName(parameter);
    }

    public List<SubjectEntity> getAllSubjects() {
        return programData.getAllSubjects();
    }
}
