package net.thogau.josiris.views.users;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import net.thogau.josiris.data.entity.User;

public class UserProfileForm extends FormLayout {

	TextField userName = new TextField("Username");
	TextField firstName = new TextField("Firstname");
	TextField lastName = new TextField("Lastname");
	EmailField email = new EmailField("Email");
	PasswordField password = new PasswordField("Password");
	PasswordField password2 = new PasswordField("Password");
	Button save;

	Binder<User> binder = new BeanValidationBinder<>(User.class);

	public UserProfileForm(String buttonText) {
		addClassName("user-profile-form");
		save = new Button(buttonText);
		binder.bindInstanceFields(this);
		password2.setRequired(true);
		add(new HorizontalLayout(userName, email), new HorizontalLayout(firstName, lastName),
				new HorizontalLayout(password, password2), createButtonsLayout());
		setUser(new User());
	}

	public void setUser(User u) {
		binder.setBean(u);
		password2.setValue(password.getValue());
	}

	private HorizontalLayout createButtonsLayout() {
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		save.addClickShortcut(Key.ENTER);

		save.addClickListener(event -> validateAndSave());

		binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

		HorizontalLayout layout = new HorizontalLayout(save);
		layout.setPadding(true);
		layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
		return layout;
	}

	private void validateAndSave() {
		if (binder.isValid()) {
			if (!password.getValue().equals(password2.getValue())) {
				Notification.show("Password do not match", 5000, Position.TOP_CENTER);
			} else {
				fireEvent(new SaveEvent(this, binder.getBean()));
			}
		}
	}

	// Events
	public static abstract class UserFormEvent extends ComponentEvent<UserProfileForm> {

		private User user;

		protected UserFormEvent(UserProfileForm source, User user) {
			super(source, false);
			this.user = user;
		}

		public User getUser() {
			return user;
		}
	}

	public static class SaveEvent extends UserFormEvent {
		SaveEvent(UserProfileForm source, User user) {
			super(source, user);
		}
	}

	public Registration addRegisterListener(ComponentEventListener<SaveEvent> listener) {
		return addListener(SaveEvent.class, listener);
	}

}