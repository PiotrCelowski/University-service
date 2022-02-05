package university.service.domain.event;

import org.springframework.stereotype.Component;
import university.service.domain.program.SubjectEntity;

import java.util.Date;
import java.util.List;

public class EventEntity {
    private SubjectEntity subject;
    private List<Date> dates;

    public SubjectEntity getSubject() {
        return subject;
    }

    public List<Date> getDates() {
        return dates;
    }
}
