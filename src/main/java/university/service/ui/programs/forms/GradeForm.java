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
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.shared.Registration;
import org.springframework.security.core.userdetails.UserDetails;
import university.service.domain.grade.GradeEntity;
import university.service.security.MyUserDetailService;

public class GradeForm extends FormLayout {
    TextField student = new TextField("Student");
    TextField grade = new TextField("Grade");
    TextField teacher = new TextField("Teacher");
    private GradeEntity gradeEntity;
    Binder<GradeEntity> binder = new BeanValidationBinder<>(GradeEntity.class);

    Button save = new Button("Save");
    Button delete = new Button("Delete");

    public GradeForm(UserDetails user, MyUserDetailService myUserDetailService) {

        addClassName("grade-form");

        binder.forField( student )
        .asRequired("The id field is required.")
        .withValidator(
            student -> myUserDetailService.findUser(student).size() != 0, // zasrany zjebańcu!
        "Student doesn't exist in Database")
                .bind(GradeEntity::getStudentName, GradeEntity::setStudentName);

        student.setEnabled(true);
        student.setReadOnly(false);



        binder.forField( grade )
        .withConverter(
            new StringToIntegerConverter(2, "integers only" ) )
        .withValidator(
                grade -> grade >= 2 && grade <= 6,
                "The grading scale is from 2 to 6")
        .bind(GradeEntity::getGrade, GradeEntity::setGrade);

        grade.setEnabled(true);
        grade.setReadOnly(false);

        teacher.setValue(user.getUsername());

        add(student, grade, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> {
            try {
                fireEvent(new DeleteEvent(this, this.gradeEntity));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        save.addClickShortcut(Key.ENTER);

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete);
    }

    public void setGradeEntity(GradeEntity gradeEntity) {
        this.gradeEntity = gradeEntity;

        binder.readBean(this.gradeEntity);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(this.gradeEntity);
            fireEvent(new GradeForm.SaveEvent(this, this.gradeEntity));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    //Events
    public abstract static class GradeFormEvent extends ComponentEvent<GradeForm> {
        private GradeEntity gradeEntity;

        public GradeFormEvent(GradeForm source, GradeEntity gradeEntity) {
            super(source, true);

            gradeEntity.setTeacherName(source.teacher.getValue());
            gradeEntity.setStudentName(source.student.getValue());
            this.gradeEntity = gradeEntity;
        }

        public GradeEntity getGradeEntity() {
            return gradeEntity;
        }
    }

    public static class SaveEvent extends GradeForm.GradeFormEvent {
        SaveEvent(GradeForm source, GradeEntity gradeEntity) {
            super(source, gradeEntity);
        }
    }
    public static class DeleteEvent extends GradeForm.GradeFormEvent {
        DeleteEvent(GradeForm source, GradeEntity gradeEntity) {
            super(source, gradeEntity);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
