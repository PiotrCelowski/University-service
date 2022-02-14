package university.service.data.program;

import org.springframework.stereotype.Component;
import university.service.domain.program.ProgramEntity;
import university.service.domain.program.SubjectEntity;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProgramData {
    ArrayList<ProgramEntity> allPrograms = new ArrayList<>();
    ArrayList<SubjectEntity> allSubjects = new ArrayList<>();

    @PostConstruct
    private void createDummyPrograms() {
        allPrograms.add(new ProgramEntity("Physics", "Physics is hard"));
        allPrograms.add(new ProgramEntity("Polish", "Polish is ..."));
        allPrograms.add(new ProgramEntity("Maths", "Maths is complex"));

        getProgramByName("Physics").addSubject(new SubjectEntity("1st subject", "Pierwszy"));
        getProgramByName("Physics").addSubject(new SubjectEntity("2nd subject", "Drugi"));

        getProgramByName("Polish").addSubject(new SubjectEntity("2nd subject", "Drugi"));

    }

    @PostConstruct
    private void createDummySubjects() {
        allSubjects.add(new SubjectEntity("Subject1", "aaaa"));
        allSubjects.add(new SubjectEntity("Subject2", "aaa"));
        allSubjects.add(new SubjectEntity("Subject3", "aaa"));
    }

    public List<ProgramEntity> getAllPrograms() {
        return allPrograms;
    }

    public void saveProgram(ProgramEntity programEntity) {
        allPrograms.add(programEntity);
    }

    public void deleteProgram(ProgramEntity programEntity) {
        allPrograms.removeIf(program -> program.getProgramName().equals(programEntity.getProgramName()));
    }


    public ProgramEntity getProgramByName(String parameter) {
        List<ProgramEntity> allPrograms = getAllPrograms();
        for(int i=0; i<allPrograms.size(); i++ ) {
            if(allPrograms.get(i).getProgramName().equals(parameter)) {
                return allPrograms.get(i);
            }
        }
        return null;
    }

    public List<SubjectEntity> getAllSubjectsForProgram(ProgramEntity currentProgram) {
        return currentProgram.getSubjects();
    }

    public void saveSubject(SubjectEntity subjectEntity, ProgramEntity programEntity) {
        programEntity.addSubject(subjectEntity);
    }

    public void deleteSubject(SubjectEntity subjectEntity, ProgramEntity programEntity) {
        programEntity.removeSubject(subjectEntity);
    }

    public SubjectEntity getSubjectByName(String parameter) {
        List<SubjectEntity> allSubjects = getAllSubjects();
        for(int i=0; i<allSubjects.size(); i++ ) {
            if(allSubjects.get(i).getSubjectName().equals(parameter)) {
                return allSubjects.get(i);
            }
        }
        return null;
    }

    public List<SubjectEntity> getAllSubjects() {
        return allSubjects;
    }
}
