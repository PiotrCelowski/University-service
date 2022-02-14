package university.service.ui.programs;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import university.service.application.program.ProgramUseCase;
import university.service.domain.program.SubjectEntity;
import university.service.security.SecurityService;
import university.service.ui.MainLayout;
import university.service.ui.programs.forms.SubjectForm;

import java.util.Collection;

@Secured({"WORKER","USER"})
@Route(value="subjects", layout = MainLayout.class)
@PageTitle("Subjects | University service")
public class SubjectView extends VerticalLayout {
    private Grid<SubjectEntity> grid = new Grid<>(SubjectEntity.class);
    private ProgramUseCase programUseCase;
    private SubjectForm subjectForm;
    private SubjectEntity selectedSubject;
    private SecurityService securityService;
    private Collection<? extends GrantedAuthority> currentUserAuthorities;

    public SubjectView(ProgramUseCase programUseCase, SecurityService securityService) {
        this.programUseCase = programUseCase;
        this.securityService = securityService;
        this.currentUserAuthorities = securityService.getAuthenticatedUser().getAuthorities();

        addClassName("list-view");
        setSizeFull();
        configureGrid();

        subjectForm = new SubjectForm();
        subjectForm.addListener(SubjectForm.SaveEvent.class, this::saveSubjectEntity);
        subjectForm.addListener(SubjectForm.DeleteEvent.class, this::deleteSubjectEntity);

        FlexLayout content = new FlexLayout(grid, subjectForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, subjectForm);
        content.setFlexShrink(1, subjectForm);
        content.addClassNames("content", "gap-m");
        content.setSizeFull();

        add(getToolbar(), content);

        updateList();

        closeEditor();

        grid.asSingleSelect().addValueChangeListener(event ->
                setSelectedSubject(event.getValue()));
    }

    private void setSelectedSubject(SubjectEntity selectedSubject) {
        this.selectedSubject = selectedSubject;

        if(currentUserAuthorities != null && currentUserAuthorities.stream().anyMatch(a -> a.getAuthority().equals("WORKER"))) {
            editSubjectEntity(this.selectedSubject);
        }
    }

    private void configureGrid() {
        grid.addClassName("subject-grid");
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.addColumn(SubjectEntity::getSubjectName).setHeader("Subject name")
                .setFlexGrow(1);
        grid.addColumn(SubjectEntity::getSubjectDescription).setHeader("Subject description")
                .setFlexGrow(4);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
    }

    private HorizontalLayout getToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        Button showSubjectDetailsButton = new Button("Show subject details");
        showSubjectDetailsButton.addClickListener(this::navigateToSubjectDetails);

        if(currentUserAuthorities != null && currentUserAuthorities.stream().anyMatch(a -> a.getAuthority().equals("WORKER"))) {
            Button addSubjectButton = new Button("Add subject");
            Button removeSubjectButton = new Button("Remove subject");

            addSubjectButton.addClickListener(this::handleForm);
            removeSubjectButton.addClickListener(this::handleForm);
            toolbar.add(addSubjectButton, removeSubjectButton, showSubjectDetailsButton);
        } else {
            toolbar.add(showSubjectDetailsButton);
        }

        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void navigateToSubjectDetails(ClickEvent<Button> buttonClickEvent) {
        UI.getCurrent().navigate("subject" + "/" + getSelectedSubjectName());
    }

    private String getSelectedSubjectName() {
        return this.selectedSubject.getSubjectName();
    }

    private void updateList() {
        grid.setItems(programUseCase.getAllSubjects());
    }


    private void saveSubjectEntity(SubjectForm.SaveEvent event) {
        updateList();
        closeEditor();
    }

    private void deleteSubjectEntity(SubjectForm.DeleteEvent event) {
        updateList();
        closeEditor();
    }

    public void editSubjectEntity(SubjectEntity subjectEntity) {
        if (subjectEntity == null) {
            closeEditor();
        } else {
            subjectForm.setSubjectEntity(subjectEntity);
            subjectForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void handleForm(ClickEvent<Button> buttonClickEvent) {
        if(!subjectForm.isVisible()) {
            addSubjectEntity();
        } else {
            closeEditor();
        }
    }

    void addSubjectEntity() {
        grid.asSingleSelect().clear();
        editSubjectEntity(new SubjectEntity());
    }

    private void closeEditor() {
        subjectForm.setSubjectEntity(null);
        subjectForm.setVisible(false);
        removeClassName("editing");
    }
}
