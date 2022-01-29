package university.service.domain.program;

public class ProgramEntity {

    private String programName;
    private String programDescription;

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
}
