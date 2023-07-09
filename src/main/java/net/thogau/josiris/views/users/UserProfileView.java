package net.thogau.josiris.views.users;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.PermitAll;
import net.thogau.josiris.data.service.UserService;
import net.thogau.josiris.data.service.UserService.EmailAlreadyUsedException;
import net.thogau.josiris.security.AuthenticatedUser;
import net.thogau.josiris.views.MainLayout;

@PageTitle("User profile")
@Route(value = "profile", layout = MainLayout.class)
@PermitAll
public class UserProfileView extends VerticalLayout {

	UserService userService;
	UserProfileForm form;

	public UserProfileView(UserService service, AuthenticatedUser authenticatedUser) {
		this.userService = service;
		addClassName("user_profile-view");
		form = new UserProfileForm("Save");
		form.setUser(authenticatedUser.get().get());
		form.setWidth("25em");
		form.addRegisterListener(this::register);
		add(form);
	}

	private void register(UserProfileForm.SaveEvent event) {
		try {
			form.setUser(userService.save(event.getUser()));
			Notification.show("Profile saved", 5000, Position.TOP_CENTER);
		} catch (EmailAlreadyUsedException e) {
			Notification.show("Email already in use", 5000, Position.TOP_CENTER);
		}
	}

}
