package university.service.application.event;

import org.springframework.stereotype.Component;
import university.service.data.event.EventData;
import university.service.domain.event.EventEntity;
import university.service.domain.program.SubjectEntity;

import java.util.ArrayList;

@Component
public class EventUseCase {
    private EventData eventData;

    public EventUseCase(EventData eventData) {
        this.eventData = eventData;
    }

    public ArrayList<EventEntity> getAllEvents(SubjectEntity selectedSubject) {
        return eventData.getAllEvents();
    }
}
