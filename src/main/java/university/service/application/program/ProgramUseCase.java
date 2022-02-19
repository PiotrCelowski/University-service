package university.service.application.program;

import org.springframework.stereotype.Component;
import university.service.data.program.ProgramRepository;
import university.service.data.program.SubjectRepository;
import university.service.domain.program.ProgramEntity;
import university.service.domain.program.SubjectEntity;

import java.util.List;

@Component
public class ProgramUseCase {
    private ProgramRepository programRepository;
    private SubjectRepository subjectRepository;

    public ProgramUseCase(ProgramRepository programRepository, SubjectRepository subjectRepository) {
        this.programRepository = programRepository;
        this.subjectRepository = subjectRepository;
    }

    public ProgramEntity createProgram(String name, String description) {
        return new ProgramEntity(name, description);
    }

    public List<ProgramEntity> getAllPrograms() {
        return programRepository.findAll();
    }

    public void saveProgram(ProgramEntity programEntity) {
        programRepository.saveAndFlush(programEntity);
    }

    public void deleteProgram(ProgramEntity programEntity) {
        programRepository.delete(programEntity);
    }

    public ProgramEntity getProgramByName(String parameter) {
        return programRepository.findByProgramName(parameter);
    }


    public List<SubjectEntity> getAllSubjectForProgram(ProgramEntity currentProgram) {
        ProgramEntity program = programRepository.findByProgramName(currentProgram.getProgramName());
        return program.getSubjects();
    }

    public void saveSubject(SubjectEntity subjectEntity) {
        subjectRepository.saveAndFlush(subjectEntity);
    }

    public void addSubjectToProgram(SubjectEntity subjectEntity, ProgramEntity programEntity) {
        SubjectEntity oldEntity = subjectRepository.findBySubjectName(subjectEntity.getSubjectName());
        subjectRepository.delete(oldEntity);
        subjectEntity.addRelatedProgram(programEntity);
        saveSubject(subjectEntity);
        ProgramEntity oldProgram = programRepository.findByProgramName(programEntity.getProgramName());
        programRepository.delete(oldProgram);
        programEntity.addSubject(subjectEntity);
        saveProgram(programEntity);
    }

    public void deleteSubject(SubjectEntity subjectEntity) {
        List<ProgramEntity> programs = getAllPrograms();
        programs.forEach(program -> {
            program.getSubjects().removeIf(subject -> subject.getSubjectName().equals(subjectEntity.getSubjectName()));
            saveProgram(program);
        });
        subjectRepository.delete(subjectEntity);
    }

    public void removeSubjectFromProgram(SubjectEntity subjectEntity, ProgramEntity programEntity) {
        programEntity.removeSubject(subjectEntity);
        subjectEntity.removeRelatedProgram(programEntity);
        saveProgram(programEntity);
        saveSubject(subjectEntity);
    }

    public SubjectEntity getSubjectByName(String parameter) {
        return subjectRepository.findBySubjectName(parameter);
    }

    public List<SubjectEntity> getAllSubjects() {
        return subjectRepository.findAll();
    }
}
