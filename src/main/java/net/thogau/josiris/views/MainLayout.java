package net.thogau.josiris.views;

import java.util.Optional;
import java.util.stream.Collectors;

import org.vaadin.lineawesome.LineAwesomeIcon;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;

import net.thogau.josiris.data.entity.User;
import net.thogau.josiris.data.service.PatientService;
import net.thogau.josiris.security.AuthenticatedUser;
import net.thogau.josiris.views.components.appnav.AppNav;
import net.thogau.josiris.views.components.appnav.AppNavItem;
import net.thogau.josiris.views.patient.ImportView;
import net.thogau.josiris.views.patient.PatientListView;
import net.thogau.josiris.views.users.UserProfileView;
import net.thogau.josiris.views.utils.ExternalToolbarView;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

	private Span viewTitle;
	private HorizontalLayout toolbar;
	private AppNav navigation;
	private AuthenticatedUser authenticatedUser;
	@SuppressWarnings("unused")
	private AccessAnnotationChecker accessChecker;
	@SuppressWarnings("unused")
	private PatientService patientService;

	public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker,
			PatientService patientService) {
		this.authenticatedUser = authenticatedUser;
		this.accessChecker = accessChecker;
		this.patientService = patientService;
		setPrimarySection(Section.DRAWER);
		addHeaderContent();
		addDrawerContent();
	}

	private void addHeaderContent() {
		DrawerToggle toggle = new DrawerToggle();
		toggle.getElement().setAttribute("aria-label", "Menu toggle");

		viewTitle = new Span();
		viewTitle.addClassNames(LumoUtility.FontSize.LARGE);
		viewTitle.getStyle().set("white-space", "nowrap");
		viewTitle.getStyle().set("margin-right", "15px");
		toolbar = new HorizontalLayout();

		addToNavbar(true, toggle, viewTitle, toolbar);

		// user menu or login link
		VerticalLayout layout = new VerticalLayout();
		Optional<User> maybeUser = authenticatedUser.get();
		if (maybeUser.isPresent()) {
			User user = maybeUser.get();
			MenuBar userMenu = new MenuBar();
			MenuItem userName = userMenu.addItem("");
			Div div = new Div();
			div.add(new Icon("lumo", "dropdown"));
			div.add(user.getFullName());
			div.getElement().getStyle().set("display", "flex");
			div.getElement().getStyle().set("align-items", "center");
			div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
			userName.add(div);
			userName.getSubMenu().addItem("Logout", e -> {
				authenticatedUser.logout();
			});
			userName.getSubMenu().addItem("Profile", e -> {
				UI.getCurrent().navigate(UserProfileView.class);
			});
			layout.add(userMenu);
		} else {
			Anchor loginLink = new Anchor("login", "Login");
			layout.add(loginLink);
		}
		layout.setAlignItems(Alignment.END);
		addToNavbar(layout);
	}

	private void addDrawerContent() {
		addToDrawer(createHeader(), new Scroller(createNavigation()), createFooter());
	}

	private Header createHeader() {
		return new Header(new Anchor(RouteConfiguration.forSessionScope().getUrl(HomeView.class), new H2("Josiris")));
	}

	private AppNav createNavigation() {
		navigation = new AppNav();
		navigation.setCollapsible(false);
		navigation.addItem(new AppNavItem("Import", ImportView.class));
		navigation.addItem(new AppNavItem("Browse", PatientListView.class));
		return navigation;
	}

	private Footer createFooter() {
		Footer layout = new Footer();
		Span icon = (Span) LineAwesomeIcon.COPYRIGHT.create();
		icon.getStyle().set("position", "relative");
		icon.getStyle().set("top", "2px");
		icon.getStyle().set("left", "3px");
		layout.add(icon, new Text("2023 thogau.net"));
		return layout;
	}

	@Override
	protected void afterNavigation() {
		super.afterNavigation();
		toolbar.removeAll();
		if (getContent() instanceof ExternalToolbarView) {
			toolbar.add(((ExternalToolbarView) getContent()).getToolbar().getChildren().collect(Collectors.toList()));
			toolbar.setVisible(true);
		}
		if (getContent() instanceof HasDynamicTitle) {
			viewTitle.setText(((HasDynamicTitle) getContent()).getPageTitle());
		} else {
			PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
			viewTitle.setText(title == null ? "" : title.value());
		}
	}

	public void updateHeader(String title) {
		viewTitle.setText(title);
	}
}
