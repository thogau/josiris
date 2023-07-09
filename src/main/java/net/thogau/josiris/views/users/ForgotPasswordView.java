package net.thogau.josiris.views.users;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import net.thogau.josiris.data.service.UserService;
import net.thogau.josiris.data.service.UserService.EmailAlreadyUsedException;
import net.thogau.josiris.data.service.UserService.NoUserWithUsernameOrEmailException;
import net.thogau.josiris.views.MainLayout;

@Route(value = "forgotPassword", layout = MainLayout.class)
@AnonymousAllowed
public class ForgotPasswordView extends VerticalLayout implements HasDynamicTitle {

	UserService userService;
	TextField userName = new TextField("Username or email");
	Button send = new Button("Send");

	public ForgotPasswordView(UserService service) {
		this.userService = service;
		addClassName("forgotPassword-view");
		FormLayout form = new FormLayout();
		userName.setRequired(true);
		userName.setMaxWidth("15em");
		send.addClickListener(click -> send());
		send.setMaxWidth("10em");
		send.addClickShortcut(Key.ENTER);
		form.add(userName, send);
		add(form);
	}

	private void send() {
		try {
			if (userName.getValue().length() > 0) {
				userService.resetPassword(userName.getValue());
				UI.getCurrent().navigate("");
				Notification.show("Check email", 5000, Position.TOP_CENTER);
			} else {
				Notification.show("Username or email", 5000, Position.TOP_CENTER);
			}
		} catch (NoUserWithUsernameOrEmailException e) {
			Notification.show("Unknown user", 5000, Position.TOP_CENTER).addThemeName("error");
		} catch (EmailAlreadyUsedException e) {
			Notification.show("Email already in use", 5000, Position.TOP_CENTER).addThemeName("error");
		}
	}

	@Override
	public String getPageTitle() {
		return "Forgot password";
	}

}
