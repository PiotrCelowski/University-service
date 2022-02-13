package university.service.ui.programs;

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
import org.springframework.security.core.GrantedAuthority;
import university.service.application.program.ProgramUseCase;
import university.service.domain.program.ProgramEntity;
import university.service.security.SecurityService;
import university.service.ui.MainLayout;
import university.service.ui.programs.forms.ProgramForm;

import java.util.Collection;

@Secured({"WORKER","USER"})
@Route(value="programs", layout = MainLayout.class)
@PageTitle("Programs | University service")
public class ProgramView extends VerticalLayout {
    private Grid<ProgramEntity> grid = new Grid<>(ProgramEntity.class);
    private ProgramUseCase programUseCase;
    private ProgramForm programForm;
    private ProgramEntity selectedProgram;
    private SecurityService securityService;
    private Collection<? extends GrantedAuthority> currentUserAuthorities;

    public ProgramView(ProgramUseCase programUseCase, SecurityService securityService) {
        this.programUseCase = programUseCase;
        this.securityService = securityService;
        this.currentUserAuthorities = securityService.getAuthenticatedUser().getAuthorities();

        addClassName("list-view");
        setSizeFull();
        configureGrid();

        programForm = new ProgramForm();
        programForm.addListener(ProgramForm.SaveEvent.class, this::saveProgramEntity);

        FlexLayout content = new FlexLayout(grid, programForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, programForm);
        content.setFlexShrink(1, programForm);
        content.addClassNames("content", "gap-m");
        content.setSizeFull();

        add(getToolbar(), content);

        updateList();

        closeEditor();

        grid.asSingleSelect().addValueChangeListener(event ->
                setSelectedProgram(event.getValue()));
    }

    private void configureGrid() {
        grid.addClassName("program-grid");
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.addColumn(ProgramEntity::getProgramName).setHeader("Program name")
                .setFlexGrow(1);
        grid.addColumn(ProgramEntity::getProgramDescription).setHeader("Program description")
                .setFlexGrow(4);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
    }

    private HorizontalLayout getToolbar() {
        Button addProgramButton = new Button("Add program");
        Button removeProgramButton = new Button("Remove program");
        Button programDetailsButton = new Button("Show program details");
        addProgramButton.addClickListener(this::handleForm);
        removeProgramButton.addClickListener(this::deleteProgramEntity);
        programDetailsButton.addClickListener(this::navigateToProgramDetails);
        HorizontalLayout toolbar = new HorizontalLayout();

        if(currentUserAuthorities != null && currentUserAuthorities.stream().anyMatch(a -> a.getAuthority().equals("WORKER"))) {
            toolbar.add(addProgramButton, removeProgramButton, programDetailsButton);
        } else {
            toolbar.add(programDetailsButton);
        }
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void navigateToProgramDetails(ClickEvent<Button> buttonClickEvent) {
        UI.getCurrent().navigate("program" + "/" + getProgramName());
    }

    private String getProgramName() {
        return this.selectedProgram.getProgramName();
    }

    private void handleForm(ClickEvent<Button> buttonClickEvent) {
        if(!programForm.isVisible()) {
            addProgramEntity();
        } else {
            closeEditor();
        }
    }

    private void updateList() {
        grid.setItems(programUseCase.getAllPrograms());
    }

    public void addProgramEntity() {
        grid.asSingleSelect().clear();
        programForm.setProgramEntity(new ProgramEntity());
        programForm.setVisible(true);
        addClassName("editing");
    }

    private void closeEditor() {
        programForm.setProgramEntity(null);
        programForm.setVisible(false);
        removeClassName("editing");
    }

    private void saveProgramEntity(ProgramForm.SaveEvent event) {
        programUseCase.saveProgram(event.getProgramEntity());
        updateList();
        closeEditor();
    }

    private void deleteProgramEntity(ClickEvent<Button> buttonClickEvent) {
        programUseCase.deleteProgram(this.selectedProgram);
        updateList();
        closeEditor();
    }

    public void setSelectedProgram(ProgramEntity selectedProgram) {
        this.selectedProgram = selectedProgram;
    }

}
