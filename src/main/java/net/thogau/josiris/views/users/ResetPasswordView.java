package net.thogau.josiris.views.users;

import java.util.List;
import java.util.Map;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import net.thogau.josiris.data.service.UserService;
import net.thogau.josiris.data.service.UserService.EmailAlreadyUsedException;
import net.thogau.josiris.data.service.UserService.ResetPasswordException;
import net.thogau.josiris.views.MainLayout;

@Route(value = "resetPassword", layout = MainLayout.class)
@AnonymousAllowed
public class ResetPasswordView extends VerticalLayout implements BeforeEnterObserver, HasDynamicTitle {

	private final UserService userService;
	PasswordField password = new PasswordField("Password");
	PasswordField password2 = new PasswordField("Confirm password");
	Button reset = new Button("Reset password");
	String username;

	public ResetPasswordView(UserService userService) {
		this.userService = userService;
		addClassName("resetPassword-view");
		FormLayout layout = new FormLayout();
		layout.setWidth("25em");
		password.setRequired(true);
		password.setMaxWidth("15em");
		password2.setRequired(true);
		password2.setMaxWidth("15em");
		reset.addClickListener(click -> reset());
		reset.setMaxWidth("10em");
		reset.addClickShortcut(Key.ENTER);

		layout.add(new HorizontalLayout(password, password2), createToolbar());
		add(layout);
	}

	private HorizontalLayout createToolbar() {
		reset.addClickListener(click -> reset());
		reset.setMaxWidth("10em");
		reset.addClickShortcut(Key.ENTER);

		HorizontalLayout layout = new HorizontalLayout(reset);
		layout.setPadding(true);
		layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
		return layout;
	}

	private void reset() {
		if (!password.getValue().equals(password2.getValue())) {
			Notification.show("Passwords do not match", 5000, Position.TOP_CENTER).addThemeName("error");
		} else {
			try {
				userService.resetPassword(username, password.getValue());
				UI.getCurrent().navigate("");
				Notification.show("Password is reset", 5000, Position.TOP_CENTER);
			} catch (ResetPasswordException e) {
				Notification.show("Error resetting password", 5000, Position.TOP_CENTER).addThemeName("error");
			} catch (EmailAlreadyUsedException e) {
				Notification.show("Email in use", 5000, Position.TOP_CENTER).addThemeName("error");
			}
		}
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		try {
			Map<String, List<String>> params = event.getLocation().getQueryParameters().getParameters();
			String username = params.get("username").get(0);
			String code = params.get("code").get(0);
			userService.verifyResetPassword(username, code);
			this.username = username;
		} catch (Exception e) {
			add(new Text("Invalid link"));
		}
	}

	@Override
	public String getPageTitle() {
		return "Register";
	}

}
