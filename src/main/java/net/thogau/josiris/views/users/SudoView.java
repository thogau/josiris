package net.thogau.josiris.views.users;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;
import net.thogau.josiris.data.entity.User;
import net.thogau.josiris.data.service.UserService;
import net.thogau.josiris.views.MainLayout;

@Route(value = "sudo", layout = MainLayout.class)
@PageTitle("Connect as")
@RolesAllowed({ "ADMIN", "ROLE_PREVIOUS_ADMINISTRATOR" })
public class SudoView extends HorizontalLayout {

	UserService userService;
	Select<User> user = new Select<>();
	Button connect = new Button("Connect");
	Button disconnect = new Button("Disconnect");

	public SudoView(UserService userService) {
		this.userService = userService;
		getStyle().set("padding-left", "10px");
		user.setItems(userService.getImitableUsers());
		user.setRenderer(new ComponentRenderer<>(u -> {
			Div d = new Div();
			d.add(new Text(u.getUsername() + " (" + u.getFullName() + ")"));
			return d;
		}));
		user.setPlaceholder("Username");
		user.getStyle().set("width", "300px");

		if (isImpersonated()) {
			disconnect.addClickListener(event -> UI.getCurrent().getPage()
					.setLocation("sudo/exit"));
			H3 d = new H3(new Text(
					"Connected as \"" + SecurityContextHolder.getContext().getAuthentication().getName() + "\""));
			d.getStyle().set("padding", "7px 5px 5px 5px");
			add(d, disconnect);
		} else {
			connect.addClickListener(event -> UI.getCurrent().getPage()
					.setLocation("impersonate?username=" + user.getValue().getUsername()));
			add(user, connect);
		}

	}

	private boolean isImpersonated() {
		for (GrantedAuthority grantedAuthority : SecurityContextHolder.getContext().getAuthentication()
				.getAuthorities()) {
			if (SwitchUserFilter.ROLE_PREVIOUS_ADMINISTRATOR.equals(grantedAuthority.getAuthority())) {
				return true;
			}
		}
		return false;
	}

}
