package university.service.domain.event;

import org.springframework.stereotype.Component;
import university.service.domain.program.SubjectEntity;

import java.util.Date;

public class EventEntity {
    private SubjectEntity subject;
    private Date date;

    public SubjectEntity getSubject() {
        return subject;
    }

    public Date getDate() {
        return date;
    }
}
