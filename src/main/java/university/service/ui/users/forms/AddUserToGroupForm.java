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
import university.service.domain.identity.GroupEntity;

public class AddUserToGroupForm extends FormLayout {
    TextField username = new TextField("User name");

    GroupEntity selectedGroup;
    BaseUser selectedUser;

    Button add = new Button("Add user");

    public AddUserToGroupForm() {
        addClassName("addusertogroup-form");
        username.setEnabled(true);
        username.setReadOnly(false);

        add(username, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add.addClickShortcut(Key.ENTER);

        add.addClickListener(event -> validateAndAdd());

        return new HorizontalLayout(add);
    }

    public void setSelectedGroup(GroupEntity selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public void setSelectedUser(BaseUser user) {
        this.selectedUser = (user);
    }

    private void validateAndAdd() {
        fireEvent(new AddUserToGroupEvent(this, this.username.getValue()));
    }

    //Events
    public abstract static class AddUserToGroupFormEvent extends ComponentEvent<AddUserToGroupForm> {
        private String userName;

        public AddUserToGroupFormEvent(AddUserToGroupForm source, String userName) {
            super(source, false);
            this.userName = userName;
        }

        public String getUserName() {
            return userName;
        }
    }

    public static class AddUserToGroupEvent extends AddUserToGroupForm.AddUserToGroupFormEvent {
        AddUserToGroupEvent(AddUserToGroupForm source, String userName) {
            super(source, userName);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
