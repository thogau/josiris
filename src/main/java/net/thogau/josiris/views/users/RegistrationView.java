package net.thogau.josiris.views.users;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import net.thogau.josiris.data.service.UserService;
import net.thogau.josiris.data.service.UserService.EmailAlreadyUsedException;
import net.thogau.josiris.views.MainLayout;

@Route(value = "register", layout = MainLayout.class)
@AnonymousAllowed
public class RegistrationView extends VerticalLayout implements HasDynamicTitle {

	UserService userService;
	UserProfileForm form;

	public RegistrationView(UserService service) {
		this.userService = service;
		addClassName("registration-view");
		form = new UserProfileForm("Register");
		form.setWidth("25em");
		form.addRegisterListener(this::register);
		add(form);
	}

	private void register(UserProfileForm.SaveEvent event) {
		try {
			userService.register(event.getUser());
			UI.getCurrent().navigate("");
			Notification.show("Check email", 5000, Position.TOP_CENTER);
		} catch (EmailAlreadyUsedException e) {
			Notification.show("Email in use", 5000, Position.TOP_CENTER).addThemeName("error");
		}
	}

	@Override
	public String getPageTitle() {
		return "Register";
	}

}
