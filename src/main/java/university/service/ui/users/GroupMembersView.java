package university.service.ui.users;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import university.service.application.identity.IdentityUseCase;
import university.service.domain.identity.BaseUser;
import university.service.domain.identity.GroupEntity;
import university.service.security.SecurityService;
import university.service.ui.MainLayout;
import university.service.ui.users.forms.AddUserToGroupForm;

import java.util.Collection;

@Secured({"WORKER","ADMIN"})
@Route(value="group", layout = MainLayout.class)
@PageTitle("Groups | University service")
public class GroupMembersView extends VerticalLayout implements HasUrlParameter<String> {
    private Grid<BaseUser> grid = new Grid<>(BaseUser.class);
    private BaseUser selectedUser;
    private GroupEntity selectedGroup;
    private SecurityService securityService;
    private Collection<? extends GrantedAuthority> currentUserAuthorities;
    private IdentityUseCase identityUseCase;
    private AddUserToGroupForm addUserToGroupForm;
    private Binder<BaseUser> binder;

    public GroupMembersView(IdentityUseCase identityUseCase, SecurityService securityService) {
        this.identityUseCase = identityUseCase;
        this.securityService = securityService;
        this.currentUserAuthorities = securityService.getAuthenticatedUser().getAuthorities();

        addClassName("list-view");
        setSizeFull();
        configureGrid();

        addUserToGroupForm = new AddUserToGroupForm();
        addUserToGroupForm.addListener(AddUserToGroupForm.AddUserToGroupEvent.class, this::addUserToGroup);

        FlexLayout content = new FlexLayout(grid, addUserToGroupForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, addUserToGroupForm);
        content.setFlexShrink(1, addUserToGroupForm);
        content.addClassNames("content", "gap-m");
        content.setSizeFull();

        add(getToolbar(), content);

        closeEditor();

        this.binder = new BeanValidationBinder<>(BaseUser.class);

        grid.asSingleSelect().addValueChangeListener(event ->
                setSelectedMember(event.getValue()));
    }

    private void setSelectedMember(BaseUser user) {
        this.selectedUser = user;
        binder.readBean(this.selectedUser);
    }

    private void configureGrid() {
        grid.addClassName("group-user-grid");
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.addColumn(BaseUser::getUsername).setHeader("User name")
                .setFlexGrow(1);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
    }

    private HorizontalLayout getToolbar() {
        Button addMemberButton = new Button("Add member");
        addMemberButton.addClickListener(this::handleForm);
        Button removeMemberButton = new Button("Remove member");
        removeMemberButton.addClickListener(this::removeUserFromGroup);

        HorizontalLayout toolbar = new HorizontalLayout(addMemberButton, removeMemberButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void updateList() {
        grid.setItems(identityUseCase.getAllMembersOfGroup(this.selectedGroup));
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        this.selectedGroup = identityUseCase.getGroupByName(parameter);
        updateList();
    }

    private void addUserToGroup(AddUserToGroupForm.AddUserToGroupEvent event) {
        identityUseCase.addUserToGroup(event.getUserName(), this.selectedGroup);
        updateList();
        closeEditor();
    }

    private void removeUserFromGroup(ClickEvent<Button> buttonClickEvent) {
        identityUseCase.removeUserFromGroup(this.selectedUser.getUsername(), this.selectedGroup);
        updateList();
        closeEditor();
    }

    public void createUserEntity() {
        grid.asSingleSelect().clear();
        addUserToGroupForm.setSelectedUser(new BaseUser());
        addUserToGroupForm.setVisible(true);
        addClassName("editing");
    }

    private void handleForm(ClickEvent<Button> buttonClickEvent) {
        if(!addUserToGroupForm.isVisible()) {
            createUserEntity();
        } else {
            closeEditor();
        }
    }

    private void closeEditor() {
        addUserToGroupForm.setSelectedGroup(null);
        addUserToGroupForm.setVisible(false);
        removeClassName("editing");
    }
}
