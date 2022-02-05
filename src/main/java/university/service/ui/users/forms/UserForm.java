package university.service.ui.users.forms;

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
import university.service.domain.identity.UserEntity;

public class UserForm extends FormLayout {
    TextField userName = new TextField("User name");
    TextField userPassword = new TextField("User password");
    TextField userRole = new TextField("User role");

    UserEntity userEntity;
    Binder<UserEntity> binder = new BeanValidationBinder<>(UserEntity.class);

    Button save = new Button("Create");
    Button delete = new Button("Delete");

    public UserForm() {
        addClassName("user-form");
        binder.bindInstanceFields(this);
        userName.setEnabled(true);
        userName.setReadOnly(false);
        userPassword.setEnabled(true);
        userPassword.setReadOnly(false);
        userRole.setEnabled(true);
        userRole.setReadOnly(false);

        add(userName, userPassword, userRole, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, this.userEntity)));

        save.addClickShortcut(Key.ENTER);

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete);
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
        binder.readBean(this.userEntity);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(this.userEntity);
            fireEvent(new SaveEvent(this, this.userEntity));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    //Events
    public abstract static class UserFormEvent extends ComponentEvent<UserForm> {
        private UserEntity userEntity;

        public UserFormEvent(UserForm source, UserEntity userEntity) {
            super(source, true);
            this.userEntity = userEntity;
        }

        public UserEntity getUserEntity() {
            return userEntity;
        }
    }

    public static class SaveEvent extends UserForm.UserFormEvent {
        SaveEvent(UserForm source, UserEntity userEntity) {
            super(source, userEntity);
        }
    }
    public static class DeleteEvent extends  UserForm.UserFormEvent {
        DeleteEvent(UserForm source, UserEntity userEntity) {
            super(source, userEntity);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
