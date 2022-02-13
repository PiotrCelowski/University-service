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
import university.service.domain.identity.GroupEntity;

public class GroupForm extends FormLayout {
    TextField groupName = new TextField("Group name");

    GroupEntity group;
    Binder<GroupEntity> binder = new BeanValidationBinder<>(GroupEntity.class);

    Button save = new Button("Create");

    public GroupForm() {
        addClassName("group-form");
        binder.bindInstanceFields(this);
        groupName.setEnabled(true);
        groupName.setReadOnly(false);

        add(groupName, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);

        save.addClickListener(event -> validateAndSave());

        save.addClickShortcut(Key.ENTER);

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save);
    }

    public void setGroupEntity(GroupEntity groupEntity) {
        this.group = groupEntity;
        binder.readBean(this.group);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(this.group);
            fireEvent(new SaveEvent(this, this.group));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    //Events
    public abstract static class UserFormEvent extends ComponentEvent<GroupForm> {
        private GroupEntity group;

        public UserFormEvent(GroupForm source, GroupEntity group) {
            super(source, true);
            this.group = group;
        }

        public GroupEntity getGroupEntity() {
            return group;
        }
    }

    public static class SaveEvent extends GroupForm.UserFormEvent {
        SaveEvent(GroupForm source, GroupEntity group) {
            super(source, group);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
