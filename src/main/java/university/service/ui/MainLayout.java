package university.service.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import org.springframework.security.core.GrantedAuthority;
import university.service.security.SecurityService;
import university.service.ui.programs.ProgramView;
import university.service.ui.programs.SubjectView;
import university.service.ui.users.GroupView;
import university.service.ui.users.UserView;

import java.util.Collection;

@CssImport("./shared-styles.css")
public class MainLayout extends AppLayout {
    SecurityService securityService;
    private Collection<? extends GrantedAuthority> currentUserAuthorities;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        this.currentUserAuthorities = securityService.getAuthenticatedUser().getAuthorities();

        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("University service");
        logo.addClassName("logo");
        Button logout = new Button("Log out", e -> securityService.logout());
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logout);
        header.setDefaultVerticalComponentAlignment(
                FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidth("100%");
        header.addClassName("header");
        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink programsMenuLink = new RouterLink("Programs", ProgramView.class);
        RouterLink subjectMenuLink = new RouterLink("Subjects", SubjectView.class);
        RouterLink userMenuLink = new RouterLink("Users", UserView.class);
        RouterLink groupMenuLink = new RouterLink("Groups", GroupView.class);

        programsMenuLink.setHighlightCondition(HighlightConditions.sameLocation());
        subjectMenuLink.setHighlightCondition(HighlightConditions.sameLocation());
        userMenuLink.setHighlightCondition(HighlightConditions.sameLocation());
        groupMenuLink.setHighlightCondition(HighlightConditions.sameLocation());

        if (currentUserAuthorities != null
                && (currentUserAuthorities.stream().anyMatch(a -> a.getAuthority().equals("WORKER")))
                || currentUserAuthorities.stream().anyMatch(a -> a.getAuthority().equals("USER"))) {
            addToDrawer(new VerticalLayout(programsMenuLink, subjectMenuLink));
        } else if (currentUserAuthorities != null
                && currentUserAuthorities.stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            addToDrawer(new VerticalLayout(userMenuLink, groupMenuLink));
        }
    }
}
