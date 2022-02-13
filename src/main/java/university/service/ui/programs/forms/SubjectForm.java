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
import university.service.domain.program.SubjectEntity;

public class SubjectForm extends FormLayout {
    TextField subjectName = new TextField("Subject Name");
    TextField subjectDescription = new TextField("Subject Description");

    SubjectEntity subjectEntity;
    Binder<SubjectEntity> binder = new BeanValidationBinder<>(SubjectEntity.class);

    Button save = new Button("Save");

    public SubjectForm() {
        addClassName("subject-form");
        binder.bindInstanceFields(this);
        subjectName.setEnabled(true);
        subjectName.setReadOnly(false);
        subjectDescription.setEnabled(true);
        subjectDescription.setReadOnly(false);

        add(subjectName, subjectDescription, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);

        save.addClickListener(event -> validateAndSave());

        save.addClickShortcut(Key.ENTER);

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save);
    }

    public void setSubjectEntity(SubjectEntity subjectEntity) {
        this.subjectEntity = subjectEntity;
        binder.readBean(this.subjectEntity);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(this.subjectEntity);
            fireEvent(new SaveEvent(this, this.subjectEntity));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    //Events
    public abstract static class SubjectFormEvent extends ComponentEvent<SubjectForm> {
        private SubjectEntity subjectEntity;

        public SubjectFormEvent(SubjectForm source, SubjectEntity subjectEntity) {
            super(source, true);
            this.subjectEntity = subjectEntity;
        }

        public SubjectEntity getSubjectEntity() {
            return subjectEntity;
        }
    }

    public static class SaveEvent extends SubjectForm.SubjectFormEvent {
        SaveEvent(SubjectForm source, SubjectEntity subjectEntity) {
            super(source, subjectEntity);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
