package university.service.ui;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import university.service.application.program.ProgramUseCase;
import university.service.domain.program.ProgramEntity;

@Route("")
public class MainView extends VerticalLayout {
    private Grid<ProgramEntity> grid = new Grid<>(ProgramEntity.class);
    private ProgramUseCase programUseCase;

    public MainView(ProgramUseCase programUseCase) {
        this.programUseCase = programUseCase;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        add(grid);

        updateList();
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

    private void updateList() {
        grid.setItems(programUseCase.getAllPrograms());
    }
}
