package university.service.ui.programs.forms;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import university.service.domain.event.EventEntity;
import university.service.domain.grade.GradeEntity;
import university.service.domain.program.ProgramEntity;
import university.service.domain.program.SubjectEntity;

public class EventForm extends FormLayout {
    DatePicker eventDate = new DatePicker("Event date");
    private EventEntity selectedEvent;
    Binder<EventEntity> binder = new BeanValidationBinder<>(EventEntity.class);
    Button save = new Button("Save");

    public EventForm() {
        addClassName("event-form");
        binder.bind(eventDate, "date");
        eventDate.setEnabled(true);
        eventDate.setReadOnly(false);
        add(eventDate, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);
        save.addClickListener(event -> validateAndSave());
        save.addClickShortcut(Key.ENTER);
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save);
    }

    public void setSelectedEvent(EventEntity event) {
        this.selectedEvent = event;
        binder.readBean(this.selectedEvent);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(this.selectedEvent);
            fireEvent(new SaveEvent(this, this.selectedEvent));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    //Events
    public abstract static class EventFormEvent extends ComponentEvent<EventForm> {
        private EventEntity eventEntity;

        public EventFormEvent(EventForm source, EventEntity eventEntity) {
            super(source, true);
            this.eventEntity = eventEntity;
        }

        public EventEntity getEventEntity() {
            return this.eventEntity;
        }
    }

    public static class SaveEvent extends EventFormEvent {
        SaveEvent(EventForm source, EventEntity eventEntity) {
            super(source, eventEntity);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
