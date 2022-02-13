package university.service.ui.users;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;
import university.service.application.identity.IdentityUseCase;
import university.service.domain.identity.GroupEntity;
import university.service.ui.MainLayout;
import university.service.ui.users.forms.GroupForm;

@Secured("ADMIN")
@Route(value="groups", layout = MainLayout.class)
@PageTitle("Groups | University service")
public class GroupView extends VerticalLayout {
    private Grid<GroupEntity> grid = new Grid<>(GroupEntity.class);
    private GroupEntity selectedGroup;
    private GroupForm groupForm;
    private IdentityUseCase identityUseCase;

    public GroupView(IdentityUseCase identityUseCase) {
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        this.identityUseCase = identityUseCase;

        groupForm = new GroupForm();
        groupForm.addListener(GroupForm.SaveEvent.class, this::saveGroupEntity);

        FlexLayout content = new FlexLayout(grid, groupForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, groupForm);
        content.setFlexShrink(1, groupForm);
        content.addClassNames("content", "gap-m");
        content.setSizeFull();

        add(getToolbar(), content);

        updateList();

        closeEditor();

        grid.asSingleSelect().addValueChangeListener(event ->
                setSelectedGroup(event.getValue()));

    }

    private void configureGrid() {
        grid.addClassName("group-grid");
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.addColumn(GroupEntity::getGroupName).setHeader("Group name");
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
    }

    private HorizontalLayout getToolbar() {
        Button editUserButton = new Button("Add group");
        editUserButton.addClickListener(this::handleForm);
        Button removeUserButton = new Button("Remove group");
        removeUserButton.addClickListener(this::deleteGroupEntity);
        Button groupDetailsButton = new Button("Show group members");
        groupDetailsButton.addClickListener(this::navigateToGroupDetails);


        HorizontalLayout toolbar = new HorizontalLayout(editUserButton, removeUserButton, groupDetailsButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void updateList() {
        grid.setItems(identityUseCase.getAllGroups());
    }

    private void closeEditor() {
        groupForm.setGroupEntity(null);
        groupForm.setVisible(false);
        removeClassName("editing");
    }

    private void saveGroupEntity(GroupForm.SaveEvent event) {
        identityUseCase.createGroup(event.getGroupEntity().getGroupName());
        updateList();
        closeEditor();
    }

    private void deleteGroupEntity(ClickEvent<Button> buttonClickEvent) {
        identityUseCase.removeGroup(selectedGroup);
        updateList();
        closeEditor();
    }

    public void setSelectedGroup(GroupEntity selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public void createGroupEntity() {
        grid.asSingleSelect().clear();
        groupForm.setGroupEntity(new GroupEntity());
        groupForm.setVisible(true);
        addClassName("editing");
    }

    private void handleForm(ClickEvent<Button> buttonClickEvent) {
        if(!groupForm.isVisible()) {
            createGroupEntity();
        } else {
            closeEditor();
        }
    }

    private void navigateToGroupDetails(ClickEvent<Button> buttonClickEvent) {
        UI.getCurrent().navigate("group" + "/" + selectedGroup.getGroupName());
    }
}
