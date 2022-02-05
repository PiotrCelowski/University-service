package university.service.data.event;

import org.springframework.stereotype.Component;
import university.service.domain.event.EventEntity;
import university.service.domain.program.ProgramEntity;
import university.service.domain.program.SubjectEntity;

import java.util.ArrayList;
import java.util.List;

@Component
public class EventData {
    ArrayList<EventEntity> allEvents = new ArrayList<>();

    public ArrayList<EventEntity> getAllEvents() {
        return allEvents;
    }
}
