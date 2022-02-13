package university.service.ui.programs;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import university.service.application.event.EventUseCase;
import university.service.application.grade.GradeUseCase;
import university.service.application.program.ProgramUseCase;
import university.service.domain.event.EventEntity;
import university.service.domain.grade.GradeEntity;
import university.service.domain.program.SubjectEntity;
import university.service.security.SecurityService;
import university.service.ui.MainLayout;
import university.service.ui.programs.forms.EventForm;
import university.service.ui.programs.forms.GradeForm;

import java.util.Collection;

@Secured({"WORKER","USER"})
@Route(value="subject", layout = MainLayout.class)
@PageTitle("Subject Details | University service")
public class SubjectDetailsView extends VerticalLayout implements HasUrlParameter<String> {
    private VerticalLayout eventLayout = new VerticalLayout();
    private VerticalLayout gradeLayout = new VerticalLayout();
    private Grid<EventEntity> eventGrid = new Grid<>(EventEntity.class);
    private Grid<GradeEntity> gradeGrid = new Grid<>(GradeEntity.class);
    private SubjectEntity currentSubject;
    private EventEntity selectedEvent;
    private GradeEntity selectedGrade;
    private ProgramUseCase programUseCase;
    private EventUseCase eventUseCase;
    private GradeUseCase gradeUseCase;
    private Collection<? extends GrantedAuthority> currentUserAuthorities;
    private GradeForm gradeForm;
    private EventForm eventForm;

    public SubjectDetailsView(GradeUseCase gradeUseCase, EventUseCase eventUseCase, ProgramUseCase programUseCase, SecurityService securityService) {
        this.currentUserAuthorities = securityService.getAuthenticatedUser().getAuthorities();
        this.programUseCase = programUseCase;
        this.gradeUseCase = gradeUseCase;
        this.eventUseCase = eventUseCase;

        addClassName("two-lists-view");
        setSizeFull();
        configureGrids();

        gradeForm = new GradeForm();
        gradeForm.addListener(GradeForm.SaveEvent.class, this::saveGradeEntity);

        eventForm = new EventForm();
        eventForm.addListener(GradeForm.SaveEvent.class, this::saveEventEntity);

        eventLayout.add(getEventToolbar(), eventGrid);
        gradeLayout.add(getGradeToolbar(), gradeGrid);

        FlexLayout content = new FlexLayout(eventLayout, gradeLayout, gradeForm, eventForm);
        content.setFlexGrow(2, eventLayout);
        content.setFlexGrow(2, gradeLayout);
        content.setFlexGrow(1, gradeForm);
        content.setFlexShrink(1, gradeForm);
        content.setFlexGrow(1, eventForm);
        content.setFlexShrink(1, eventForm);
        content.addClassNames("content", "gap-m");
        content.setSizeFull();

        add(content);

        closeGradeEditor();
        closeEventEditor();

        eventGrid.asSingleSelect().addValueChangeListener(event ->
                setSelectedEvent(event.getValue()));
        gradeGrid.asSingleSelect().addValueChangeListener(event ->
                setSelectedGrade(event.getValue()));
    }

    private void configureGrids() {
        eventGrid.addClassName("event-grid");
        eventGrid.setSizeFull();
        eventGrid.removeAllColumns();
        eventGrid.addColumn(EventEntity::getDate).setHeader("Class date");
        eventGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        gradeGrid.addClassName("grade-grid");
        gradeGrid.setSizeFull();
        gradeGrid.removeAllColumns();
        gradeGrid.addColumn(GradeEntity::getStudent).setHeader("Student");
        gradeGrid.addColumn(GradeEntity::getTeacher).setHeader("Teacher");
        gradeGrid.addColumn(GradeEntity::getGrade).setHeader("Grade");
        gradeGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
    }

    private HorizontalLayout getEventToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();

        if(currentUserAuthorities != null && currentUserAuthorities.stream().anyMatch(a -> a.getAuthority().equals("WORKER"))) {
            Button addEventButton = new Button("Add event");
            Button removeEventButton = new Button("Remove event");
            addEventButton.addClickListener(this::handleEventForm);
            removeEventButton.addClickListener(this::removeEventEntity);
            toolbar.add(addEventButton);
        }

        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void removeEventEntity(ClickEvent<Button> buttonClickEvent) {
    }

    private HorizontalLayout getGradeToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();

        if(currentUserAuthorities != null && currentUserAuthorities.stream().anyMatch(a -> a.getAuthority().equals("WORKER"))) {
            Button addGradeButton = new Button("Add grade");
            Button removeGradeButton = new Button("Remove grade");
            addGradeButton.addClickListener(this::handleGradeForm);
            removeGradeButton.addClickListener(this::removeGradeEntity);
            toolbar.add(addGradeButton);
        }

        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void removeGradeEntity(ClickEvent<Button> buttonClickEvent) {
    }

    private void handleGradeForm(ClickEvent<Button> buttonClickEvent) {
        if(!gradeForm.isVisible()) {
            closeEventEditor();
            addGradeEntity();
        } else {
            closeGradeEditor();
        }
    }

    private void addGradeEntity() {
        gradeGrid.asSingleSelect().clear();
        gradeForm.setSelectedEvent(new GradeEntity());
        gradeForm.setVisible(true);
        addClassName("editing");
    }

    private void handleEventForm(ClickEvent<Button> buttonClickEvent) {
        if(!eventForm.isVisible()) {
            closeGradeEditor();
            addEventEntity();
        } else {
            closeEventEditor();
        }
    }

    private void addEventEntity() {
        eventGrid.asSingleSelect().clear();
        eventForm.setSelectedEvent(new EventEntity());
        eventForm.setVisible(true);
        addClassName("editing");
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        this.currentSubject = programUseCase.getSubjectByName(parameter);
        updateLists();
    }

    private void setSelectedGrade(GradeEntity grade) {
        this.selectedGrade = grade;
    }

    private void setSelectedEvent(EventEntity event) {
        this.selectedEvent = event;
    }

    private void closeEventEditor() {
        eventForm.setSelectedEvent(null);
        eventForm.setVisible(false);
        removeClassName("editing");
    }

    private void closeGradeEditor() {
        gradeForm.setSelectedEvent(null);
        gradeForm.setVisible(false);
        removeClassName("editing");
    }

    private void updateLists() {
        gradeGrid.setItems();
        eventGrid.setItems();
    }

    private <T extends ComponentEvent<?>> void saveEventEntity(T t) {
    }

    private <T extends ComponentEvent<?>> void saveGradeEntity(T t) {
    }

}
