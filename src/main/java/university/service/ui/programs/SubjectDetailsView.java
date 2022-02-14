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
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import university.service.application.event.EventUseCase;
import university.service.application.grade.GradeUseCase;
import university.service.application.program.ProgramUseCase;
import university.service.domain.event.EventEntity;
import university.service.domain.grade.GradeEntity;
import university.service.domain.program.ProgramEntity;
import university.service.domain.program.SubjectEntity;
import university.service.security.MyUserDetailService;
import university.service.security.SecurityService;
import university.service.ui.MainLayout;
import university.service.ui.programs.forms.GradeForm;
import university.service.ui.programs.forms.ProgramForm;

import java.util.Collection;

@Secured({"WORKER","USER"})
@Route(value="subject", layout = MainLayout.class)
@PageTitle("Subject Details | University service")
public class SubjectDetailsView extends VerticalLayout implements HasUrlParameter<String> {
    private VerticalLayout eventLayout = new VerticalLayout();
    private VerticalLayout gradeLayout = new VerticalLayout();
    private Grid<EventEntity> eventGrid = new Grid<>(EventEntity.class);
    private Grid<GradeEntity> gradeGrid = new Grid<>(GradeEntity.class);
    private SubjectEntity selectedSubject;
    private ProgramUseCase programUseCase;
    private EventUseCase eventUseCase;
    private GradeUseCase gradeUseCase;
    private GradeForm gradeForm;
    private Collection<? extends GrantedAuthority> currentUserAuthorities;
    private UserDetails userDetails;
    MyUserDetailService myUserDetailService;

    public SubjectDetailsView(GradeUseCase gradeUseCase, EventUseCase eventUseCase, ProgramUseCase programUseCase, SecurityService securityService,
                              MyUserDetailService myUserDetailService) {
        this.currentUserAuthorities = securityService.getAuthenticatedUser().getAuthorities();
        this.userDetails = securityService.getAuthenticatedUser();
        this.myUserDetailService = myUserDetailService;
        this.programUseCase = programUseCase;
        this.gradeUseCase = gradeUseCase;
        this.eventUseCase = eventUseCase;

        addClassName("two-lists-view");
        setSizeFull();
        configureGrids();

        gradeForm = new GradeForm(securityService.getAuthenticatedUser(), myUserDetailService);
        gradeForm.addListener(GradeForm.SaveEvent.class, this::saveGradeEntity);
        gradeForm.addListener(GradeForm.DeleteEvent.class, this::deleteGradeEntity);

        eventLayout.add(getEventToolbar(), eventGrid);
        gradeLayout.add(getGradeToolbar(), gradeGrid);

        FlexLayout content = new FlexLayout(eventLayout, gradeLayout, gradeForm);
        content.setFlexGrow(2, eventLayout);
        content.setFlexGrow(2, gradeLayout);

        content.setFlexGrow(1, gradeForm);
        content.setFlexShrink(1, gradeForm);

        content.addClassNames("content", "gap-m");
        content.setSizeFull();

        add(content);

        closeEditor();
        eventGrid.asSingleSelect().addValueChangeListener(event ->
                setSelectedEvent(event.getValue()));
        gradeGrid.asSingleSelect().addValueChangeListener(event ->
                setSelectedGrade(event.getValue()));
    }

    private void configureGrids() {
        eventGrid.addClassName("event-grid");
        eventGrid.setSizeFull();
        eventGrid.removeAllColumns();
        eventGrid.addColumn(EventEntity::getDates).setHeader("Class dates");
        eventGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        gradeGrid.addClassName("grade-grid");
        gradeGrid.setSizeFull();
        gradeGrid.removeAllColumns();
        gradeGrid.addColumn(GradeEntity::getStudentName).setHeader("Student");
        gradeGrid.addColumn(GradeEntity::getTeacherName).setHeader("Teacher");
        gradeGrid.addColumn(GradeEntity::getGrade).setHeader("Grade");
        gradeGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
    }

    private HorizontalLayout getEventToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        if(currentUserAuthorities != null && currentUserAuthorities.stream().anyMatch(a -> a.getAuthority().equals("WORKER"))) {
            Button addEventButton = new Button("Add event");
            Button removeEventButton = new Button("Remove event");
            addEventButton.addClickListener(this::handleEventForm);
            removeEventButton.addClickListener(this::handleEventForm);
            toolbar.add(addEventButton);
        }

        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private HorizontalLayout getGradeToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();

        if(currentUserAuthorities != null && currentUserAuthorities.stream().anyMatch(a -> a.getAuthority().equals("WORKER"))) {
            Button addGradeButton = new Button("Add grade");
            Button removeGradeButton = new Button("Remove grade");
            addGradeButton.addClickListener(this::handleGradeForm);
            removeGradeButton.addClickListener(this::handleGradeForm);
            toolbar.add(addGradeButton);
        }

        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void handleGradeForm(ClickEvent<Button> buttonClickEvent) {
        if(!gradeForm.isVisible()) {
            addGradeEntity();
        } else {
            closeEditor();
        }
    }

    void addGradeEntity() {
        gradeGrid.asSingleSelect().clear();
        editGradeEntity(new GradeEntity());
    }

    public void editGradeEntity(GradeEntity gradeEntity) {
        if (gradeEntity == null) {
            closeEditor();
        } else {
            gradeForm.setGradeEntity(gradeEntity);
            gradeForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void handleEventForm(ClickEvent<Button> buttonClickEvent) {
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        this.selectedSubject = programUseCase.getSubjectByName(parameter);
        updateLists();
    }

    private void setSelectedGrade(GradeEntity value) {
        if(currentUserAuthorities != null && currentUserAuthorities.stream().anyMatch(a -> a.getAuthority().equals("WORKER"))) {
            editGradeEntity(value);
        }
    }

    private void saveGradeEntity(GradeForm.SaveEvent event) {
        gradeUseCase.saveGrade(this.selectedSubject, event.getGradeEntity());
        updateLists();
        closeEditor();
    }

    private void deleteGradeEntity(GradeForm.DeleteEvent event) {
        gradeUseCase.deleteGrade(this.selectedSubject, event.getGradeEntity());
        updateLists();
        closeEditor();
    }

    private void setSelectedEvent(EventEntity value) {
    }

    private void closeEditor() {
        gradeForm.setGradeEntity(null);
        gradeForm.setVisible(false);
        removeClassName("editing");
    }

    private void updateLists() {
        String authority = "unknown";
        if(currentUserAuthorities != null && currentUserAuthorities.stream().anyMatch(a -> a.getAuthority().equals("WORKER"))){
            authority = "WORKER";
        }
        else if(currentUserAuthorities != null && currentUserAuthorities.stream().anyMatch(a -> a.getAuthority().equals("USER"))){
            authority = "USER";
        }

        gradeGrid.setItems(gradeUseCase.loadAllGradesForSubject(this.selectedSubject, this.userDetails.getUsername(),  authority));
        eventGrid.setItems();
    }

}
