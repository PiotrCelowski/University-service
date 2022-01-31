package university.service.ui.programs;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import university.service.application.program.ProgramUseCase;
import university.service.domain.program.ProgramEntity;
import university.service.domain.program.SubjectEntity;
import university.service.ui.MainLayout;
import university.service.ui.programs.forms.SubjectForm;

@Route(value="program", layout = MainLayout.class)
@PageTitle("Subjects | University service")
public class SubjectView extends VerticalLayout implements HasUrlParameter<String> {
    private Grid<SubjectEntity> grid = new Grid<>(SubjectEntity.class);
    private ProgramUseCase programUseCase;
    private ProgramEntity currentProgram;
    private SubjectForm subjectForm;
    private SubjectEntity selectedSubject;

    public SubjectView(ProgramUseCase programUseCase) {
        this.programUseCase = programUseCase;
        addClassName("list-view");
        setSizeFull();
        configureGrid();

        subjectForm = new SubjectForm();
        subjectForm.addListener(SubjectForm.SaveEvent.class, this::saveSubjectEntity);
        subjectForm.addListener(SubjectForm.DeleteEvent.class, this::deleteProgramEntity);

        FlexLayout content = new FlexLayout(grid, subjectForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, subjectForm);
        content.setFlexShrink(1, subjectForm);
        content.addClassNames("content", "gap-m");
        content.setSizeFull();

        add(getToolbar(), content);

        closeEditor();
        grid.asSingleSelect().addValueChangeListener(event ->
                setSelectedSubject(event.getValue()));

    }

    private void setSelectedSubject(SubjectEntity selectedSubject) {
        this.selectedSubject = selectedSubject;
        editSubjectEntity(this.selectedSubject);
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
        Button addSubjectButton = new Button("Add subject");
        addSubjectButton.addClickListener(this::handleForm);

        HorizontalLayout toolbar = new HorizontalLayout(addSubjectButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void updateList() {
        grid.setItems(programUseCase.getAllSubjectForProgram(this.currentProgram));
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        this.currentProgram = programUseCase.getProgramByName(parameter);
        updateList();
    }

    private void saveSubjectEntity(SubjectForm.SaveEvent event) {
        programUseCase.saveSubject(event.getSubjectEntity(), this.currentProgram);
        updateList();
        closeEditor();
    }

    private void deleteProgramEntity(SubjectForm.DeleteEvent event) {
        programUseCase.deleteSubject(event.getSubjectEntity(), this.currentProgram);
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
