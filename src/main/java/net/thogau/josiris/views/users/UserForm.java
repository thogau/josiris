package net.thogau.josiris.views.users;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
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
import net.thogau.josiris.security.Role;

public class UserForm extends FormLayout {

	TextField userName = new TextField("Username");
	TextField firstName = new TextField("Firstname");
	TextField lastName = new TextField("Lastname");
	EmailField email = new EmailField("Email");
	PasswordField password = new PasswordField("Password");
	PasswordField password2 = new PasswordField("Confirm_password");
	MultiSelectListBox<Role> roles = new MultiSelectListBox<>();
	Checkbox enabled = new Checkbox("Enabled");
	Button save = new Button("Save");
	Button delete = new Button("Delete");
	Button close = new Button("Cancel");

	Binder<User> binder = new BeanValidationBinder<>(User.class);

	public UserForm() {
		addClassName("user-form");
		roles.setItems(Role.ADMIN, Role.USER);
		binder.bindInstanceFields(this);
		password2.setRequired(true);

		add(userName, firstName, lastName, email, password, password2, roles, enabled, createButtonsLayout());
	}

	public void setUser(User u) {
		binder.setBean(u);
		password2.setValue(password.getValue());
	}

	private HorizontalLayout createButtonsLayout() {
		save.addClickShortcut(Key.ENTER);
		close.addClickShortcut(Key.ESCAPE);

		save.addClickListener(event -> validateAndSave());
		delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
		close.addClickListener(event -> fireEvent(new CloseEvent(this)));

		binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

		HorizontalLayout layout = new HorizontalLayout(save, delete, close);
		layout.setPadding(true);
		layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
		return layout;
	}

	private void validateAndSave() {
		if (binder.isValid()) {
			if (!password.getValue().equals(password2.getValue())) {
				Notification.show("Passwords do not match", 5000, Position.TOP_CENTER).addThemeName("error");
			} else {
				fireEvent(new SaveEvent(this, binder.getBean()));
			}
		}
	}

	// Events
	public static abstract class UserFormEvent extends ComponentEvent<UserForm> {

		private User user;

		protected UserFormEvent(UserForm source, User user) {
			super(source, false);
			this.user = user;
		}

		public User getUser() {
			return user;
		}
	}

	public static class SaveEvent extends UserFormEvent {
		SaveEvent(UserForm source, User user) {
			super(source, user);
			Notification.show("User saved", 5000, Position.TOP_CENTER);
		}
	}

	public static class DeleteEvent extends UserFormEvent {
		DeleteEvent(UserForm source, User user) {
			super(source, user);
			Notification.show("User deleted", 5000, Position.TOP_CENTER);
		}
	}

	public static class CloseEvent extends UserFormEvent {
		CloseEvent(UserForm source) {
			super(source, null);
		}
	}

	public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
		return addListener(DeleteEvent.class, listener);
	}

	public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
		return addListener(SaveEvent.class, listener);
	}

	public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
		return addListener(CloseEvent.class, listener);
	}

}