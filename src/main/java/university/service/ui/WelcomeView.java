package university.service.ui;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@Route(value="", layout = MainLayout.class)
@PageTitle("Welcome | University service")
public class WelcomeView extends VerticalLayout {
    public WelcomeView() {
        VerticalLayout layout = new VerticalLayout();

        Label label = new Label("Welcome to university service...");
        layout.add(label);
        add(layout);
    }
}
