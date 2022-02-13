package university.service.ui.programs.forms;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import university.service.domain.event.EventEntity;
import university.service.domain.grade.GradeEntity;

public class GradeForm extends FormLayout {
    IntegerField grade = new IntegerField("Grade");
    private GradeEntity selectedGrade;
    Binder<GradeEntity> binder = new BeanValidationBinder<>(GradeEntity.class);
    Button save = new Button("Save");

    public GradeForm() {
        addClassName("grade-form");
        binder.bindInstanceFields(this);
        grade.setEnabled(true);
        grade.setReadOnly(false);
        add(grade, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);
        save.addClickListener(event -> validateAndSave());
        save.addClickShortcut(Key.ENTER);
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save);
    }

    public void setSelectedEvent(GradeEntity grade) {
        this.selectedGrade = grade;
        binder.readBean(this.selectedGrade);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(this.selectedGrade);
            fireEvent(new SaveEvent(this, this.selectedGrade));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    //Events
    public abstract static class GradeFormEvent extends ComponentEvent<GradeForm> {
        private GradeEntity gradeEntity;

        public GradeFormEvent(GradeForm source, GradeEntity gradeEntity) {
            super(source, true);
            this.gradeEntity = gradeEntity;
        }

        public GradeEntity getGradeEntity() {
            return this.gradeEntity;
        }
    }

    public static class SaveEvent extends GradeFormEvent {
        SaveEvent(GradeForm source, GradeEntity gradeEntity) {
            super(source, gradeEntity);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
