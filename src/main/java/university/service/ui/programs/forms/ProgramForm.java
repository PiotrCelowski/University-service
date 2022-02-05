package university.service.ui.programs.forms;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import university.service.domain.program.ProgramEntity;

public class ProgramForm extends FormLayout {
    TextField programName = new TextField("Program Name");
    TextField programDescription = new TextField("Program Description");

    private ProgramEntity programEntity;
    Binder<ProgramEntity> binder = new BeanValidationBinder<>(ProgramEntity.class);

    Button save = new Button("Save");
    Button delete = new Button("Delete");

    public ProgramForm() {
        addClassName("program-form");
        binder.bindInstanceFields(this);
        programName.setEnabled(true);
        programName.setReadOnly(false);
        programDescription.setEnabled(true);
        programDescription.setReadOnly(false);

        add(programName, programDescription, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, this.programEntity)));

        save.addClickShortcut(Key.ENTER);

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete);
    }

    public void setProgramEntity(ProgramEntity programEntity) {
        this.programEntity = programEntity;
        binder.readBean(this.programEntity);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(this.programEntity);
            fireEvent(new SaveEvent(this, this.programEntity));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    //Events
    public abstract static class ProgramFormEvent extends ComponentEvent<ProgramForm> {
        private ProgramEntity programEntity;

        public ProgramFormEvent(ProgramForm source, ProgramEntity programEntity) {
            super(source, true);
            this.programEntity = programEntity;
        }

        public ProgramEntity getProgramEntity() {
            return programEntity;
        }
    }

    public static class SaveEvent extends ProgramFormEvent {
        SaveEvent(ProgramForm source, ProgramEntity programEntity) {
            super(source, programEntity);
        }
    }
    public static class DeleteEvent extends ProgramFormEvent {
        DeleteEvent(ProgramForm source, ProgramEntity programEntity) {
            super(source, programEntity);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
