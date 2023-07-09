package net.thogau.josiris.views.users;

import java.util.List;
import java.util.Map;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import net.thogau.josiris.data.service.UserService;
import net.thogau.josiris.views.MainLayout;

@Route(value = "verification", layout = MainLayout.class)
@AnonymousAllowed
public class EmailVerificationView extends VerticalLayout implements BeforeEnterObserver, HasDynamicTitle {

	private final UserService userService;

	public EmailVerificationView(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		try {
			Map<String, List<String>> params = event.getLocation().getQueryParameters().getParameters();
			String username = params.get("username").get(0);
			String code = params.get("code").get(0);
			userService.activate(username, code);
			add(new Text("Email verified"), new Anchor("login", "Login"));
		} catch (Exception e) {
			add(new Text("Invalid link"));
		}
	}

	@Override
	public String getPageTitle() {
		return "Email verification";
	}

}
