package university.service.ui.users;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.catalina.User;
import org.springframework.security.access.annotation.Secured;
import university.service.domain.identity.UserEntity;
import university.service.domain.program.ProgramEntity;
import university.service.ui.MainLayout;
import university.service.ui.programs.forms.ProgramForm;
import university.service.ui.users.forms.UserForm;

@Secured("ADMIN")
@Route(value="users", layout = MainLayout.class)
@PageTitle("Users | University service")
public class UserView extends VerticalLayout {
    private Grid<UserEntity> grid = new Grid<>(UserEntity.class);
    private UserEntity selectedUser;
    private UserForm userForm;

    public UserView() {
        addClassName("list-view");
        setSizeFull();
        configureGrid();

        userForm = new UserForm();
        userForm.addListener(UserForm.SaveEvent.class, this::saveUserEntity);
        userForm.addListener(UserForm.DeleteEvent.class, this::deleteUserEntity);

        FlexLayout content = new FlexLayout(grid, userForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, userForm);
        content.setFlexShrink(1, userForm);
        content.addClassNames("content", "gap-m");
        content.setSizeFull();

        add(getToolbar(), content);

        updateList();

        closeEditor();
        grid.asSingleSelect().addValueChangeListener(event ->
                setSelectedUser(event.getValue()));

    }

    private void configureGrid() {
        grid.addClassName("user-grid");
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.addColumn(UserEntity::getUsername).setHeader("User name");
        grid.addColumn(UserEntity::getPassword).setHeader("User password");
        grid.addColumn(UserEntity::getRole).setHeader("Role");
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
    }

    private HorizontalLayout getToolbar() {
        Button editUserButton = new Button("Add user");
        editUserButton.addClickListener(this::handleForm);
        Button removeUserButton = new Button("Remove user");
        removeUserButton.addClickListener(this::handleForm);

        HorizontalLayout toolbar = new HorizontalLayout(editUserButton, removeUserButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void updateList() {
        grid.setItems();
    }

    private void closeEditor() {
        userForm.setUserEntity(null);
        userForm.setVisible(false);
        removeClassName("editing");
    }

    private void saveUserEntity(UserForm.SaveEvent event) {
        updateList();
        closeEditor();
    }

    private void deleteUserEntity(UserForm.DeleteEvent event) {
        updateList();
        closeEditor();
    }

    public void setSelectedUser(UserEntity selectedUser) {
        this.selectedUser = selectedUser;
        editUserEntity(this.selectedUser);

    }

    public void editUserEntity(UserEntity userEntity) {
        if (userEntity == null) {
            closeEditor();
        } else {
            userForm.setUserEntity(userEntity);
            userForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void handleForm(ClickEvent<Button> buttonClickEvent) {
        if(!userForm.isVisible()) {
            addUserEntity();
        } else {
            closeEditor();
        }
    }

    void addUserEntity() {
        grid.asSingleSelect().clear();
        editUserEntity(new UserEntity());
    }
}
