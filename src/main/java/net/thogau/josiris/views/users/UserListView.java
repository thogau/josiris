package net.thogau.josiris.views.users;

import java.util.stream.Collectors;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;
import net.thogau.josiris.data.Role;
import net.thogau.josiris.data.entity.User;
import net.thogau.josiris.data.service.UserService;
import net.thogau.josiris.data.service.UserService.EmailAlreadyUsedException;
import net.thogau.josiris.views.MainLayout;
import net.thogau.josiris.views.utils.ExternalToolbarView;

@Route(value = "users", layout = MainLayout.class)
@PageTitle("Users")
@RolesAllowed("ADMIN")
public class UserListView extends VerticalLayout implements ExternalToolbarView {

	Grid<User> grid = new Grid<>(User.class);
	TextField filterText = new TextField();
	UserForm form;
	UserService userService;

	public UserListView(UserService service) {
		this.userService = service;
		addClassName("users-view");
		setSizeFull();
		configureGrid();
		configureForm();
		add(getContent());
		updateList();
		closeEditor();
	}

	private void closeEditor() {
		form.setUser(null);
		form.setVisible(false);
		removeClassName("editing");
	}

	private void configureGrid() {
		grid.addClassNames("users-grid");
		grid.setSizeFull();

		grid.removeAllColumns();

		grid.addColumn("username").setHeader("Username");
		grid.addColumn("firstname").setHeader("Firstname");
		grid.addColumn("lastname").setHeader("Lastname");
		grid.addColumn("email").setHeader("Email");

		grid.addColumn(user -> user.getRoles().stream().map(Role::name).collect(Collectors.toList()).stream()
				.collect(Collectors.joining(", ", "", ""))).setHeader("Roles");

		grid.addComponentColumn((user) -> {
			Checkbox checkBox = new Checkbox();
			checkBox.setValue(user.isEnabled());
			checkBox.setEnabled(false);
			return checkBox;
		}).setHeader("Enabled").setTextAlign(ColumnTextAlign.CENTER);

		grid.addComponentColumn((user) -> {
			Checkbox checkBox = new Checkbox();
			checkBox.setValue(!user.isAccountNonExpired());
			checkBox.setEnabled(false);
			return checkBox;
		}).setHeader("Account expired").setTextAlign(ColumnTextAlign.CENTER);

		grid.addComponentColumn((user) -> {
			Checkbox checkBox = new Checkbox();
			checkBox.setValue(!user.isAccountNonLocked());
			checkBox.setEnabled(false);
			return checkBox;
		}).setHeader("Account locked").setTextAlign(ColumnTextAlign.CENTER);

		grid.addComponentColumn((user) -> {
			Checkbox checkBox = new Checkbox();
			checkBox.setValue(!user.isCredentialsNonExpired());
			checkBox.setEnabled(false);
			return checkBox;
		}).setHeader("Credentials expired").setTextAlign(ColumnTextAlign.CENTER);

		grid.getColumns().forEach(col -> col.setAutoWidth(true));
		grid.asSingleSelect().addValueChangeListener(event -> editUser(event.getValue()));
	}

	public void editUser(User user) {
		if (user == null) {
			closeEditor();
		} else {
			form.setUser(user);
			form.setVisible(true);
			addClassName("editing");
		}
	}

	private void configureForm() {
		form = new UserForm();
		form.setWidth("22em");
		form.addSaveListener(this::saveUser);
		form.addDeleteListener(this::deleteUser);
		form.addCloseListener(e -> closeEditor());
	}

	private void saveUser(UserForm.SaveEvent event) {
		try {
			userService.save(event.getUser());
		} catch (EmailAlreadyUsedException e) {
			Notification.show("Email already in use", 5000, Position.TOP_CENTER).addThemeName("error");
		}
		updateList();
		closeEditor();
	}

	private void deleteUser(UserForm.DeleteEvent event) {
		userService.delete(event.getUser().getId());
		updateList();
		closeEditor();
	}

	public HorizontalLayout getToolbar() {
		filterText.setPlaceholder("Filter by name");
		filterText.setClearButtonVisible(true);
		filterText.setValueChangeMode(ValueChangeMode.LAZY);
		filterText.addValueChangeListener(e -> updateList());
		Button addUserButton = new Button("New user");
		addUserButton.addClickListener(click -> addUser());
		var toolbar = new HorizontalLayout(filterText, addUserButton);
		toolbar.addClassName("toolbar");
		return toolbar;
	}

	private void addUser() {
		grid.asSingleSelect().clear();
		editUser(new User());
	}

	private Component getContent() {
		HorizontalLayout content = new HorizontalLayout(grid, form);
		content.setFlexGrow(2, grid);
		content.setFlexGrow(1, form);
		content.addClassNames("content");
		content.setSizeFull();
		return content;
	}

	private void updateList() {
		grid.setItems(userService.search(filterText.getValue()));
	}
}