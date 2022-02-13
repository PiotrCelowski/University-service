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
import university.service.domain.identity.BaseUser;

public class UserForm extends FormLayout {
    TextField username = new TextField("User name");
    TextField role = new TextField("User role");

    BaseUser user;
    Binder<BaseUser> binder = new BeanValidationBinder<>(BaseUser.class);

    Button save = new Button("Create");

    public UserForm() {
        addClassName("user-form");
        binder.bindInstanceFields(this);
        username.setEnabled(true);
        username.setReadOnly(false);
        role.setEnabled(true);
        role.setReadOnly(false);

        add(username, role, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);

        save.addClickListener(event -> validateAndSave());

        save.addClickShortcut(Key.ENTER);

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save);
    }

    public void setUserEntity(BaseUser baseUser) {
        this.user = baseUser;
        binder.readBean(this.user);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(this.user);
            fireEvent(new SaveEvent(this, this.user));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    //Events
    public abstract static class UserFormEvent extends ComponentEvent<UserForm> {
        private BaseUser user;

        public UserFormEvent(UserForm source, BaseUser user) {
            super(source, true);
            this.user = user;
        }

        public BaseUser getUserEntity() {
            return user;
        }
    }

    public static class SaveEvent extends UserForm.UserFormEvent {
        SaveEvent(UserForm source, BaseUser user) {
            super(source, user);
        }
    }
    public static class DeleteEvent extends  UserForm.UserFormEvent {
        DeleteEvent(UserForm source, BaseUser user) {
            super(source, user);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
