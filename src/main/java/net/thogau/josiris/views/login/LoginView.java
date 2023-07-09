package net.thogau.josiris.views.login;

import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import net.thogau.josiris.data.service.UserService;
import net.thogau.josiris.security.AuthenticatedUser;
import net.thogau.josiris.security.UserDetailsServiceImpl.EmailNotValidatedException;
import net.thogau.josiris.views.HomeView;

@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
public class LoginView extends LoginOverlay implements BeforeEnterObserver, AfterNavigationObserver {

	private final static String LOGIN_ATTEMPTS_ATTRIBUTE = "LOGIN_ATTEMPTS_ATTRIBUTE";
	private final static int MAX_LOGIN_ATTEMPTS = 5;

	private final AuthenticatedUser authenticatedUser;

	public LoginView(AuthenticatedUser authenticatedUser, UserService service) {
		this.authenticatedUser = authenticatedUser;
		setAction(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), getClass()));
		layout(null);
		addRememberMeCheckbox();
		setOpened(true);
		this.addLoginListener(e -> {
			VaadinServletRequest vsr = VaadinServletRequest.getCurrent();
			HttpServletRequest req = vsr.getHttpServletRequest();
			HttpSession sess = req.getSession();
			if (sess.getAttribute(LOGIN_ATTEMPTS_ATTRIBUTE) != null) {
				if (((Integer) sess.getAttribute(LOGIN_ATTEMPTS_ATTRIBUTE)).equals(MAX_LOGIN_ATTEMPTS)) {
					LoggerFactory.getLogger(LoginView.class.getName())
							.warn(e.getUsername() + " failed " + MAX_LOGIN_ATTEMPTS + " times");
					service.lockAccount(e.getUsername());
				}
				sess.setAttribute(LOGIN_ATTEMPTS_ATTRIBUTE,
						((Integer) sess.getAttribute(LOGIN_ATTEMPTS_ATTRIBUTE)) + 1);
			} else {
				sess.setAttribute(LOGIN_ATTEMPTS_ATTRIBUTE, 1);
			}
		});
	}

	private void layout(LoginI18n.ErrorMessage err) {
		VerticalLayout layout = new VerticalLayout();
		Image logo = new Image("images/logo.png", "Logo");
		logo.setWidth("150px");
		layout.add(logo, new Anchor("register", "Create account"));
		layout.setAlignItems(Alignment.CENTER);
		layout.setHeight("80px");
		this.setTitle(layout);

		LoginI18n i18n = LoginI18n.createDefault();
		LoginI18n.Header i18nHeader = new LoginI18n.Header();
		i18nHeader.setTitle("josiris");
		i18n.setHeader(i18nHeader);
		i18n.setAdditionalInformation(null);
		i18n.getForm().setTitle("Login");
		i18n.getForm().setUsername("Username");
		i18n.getForm().setPassword("Password");
		i18n.getForm().setSubmit("Login");
		i18n.getForm().setForgotPassword("Forgot password");
		if (err != null) {
			i18n.setErrorMessage(err);
			setError(true);
		}
		setI18n(i18n);

		setForgotPasswordButtonVisible(true);
		addForgotPasswordListener(click -> getUI().ifPresent(ui -> ui.navigate("/forgotPassword")));
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		boolean isError = event.getLocation().getQueryParameters().getParameters().containsKey("error");
		if (isError) {
			VaadinServletRequest vsr = VaadinServletRequest.getCurrent();
			HttpServletRequest req = vsr.getHttpServletRequest();
			HttpSession sess = req.getSession();
			AuthenticationException ex = (AuthenticationException) sess
					.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
			if (ex == null) {
				setError(false);
				sess.removeAttribute(LOGIN_ATTEMPTS_ATTRIBUTE);
			} else {
				LoggerFactory.getLogger(LoginView.class.getName())
						.warn("AuthenticationException thrown : " + ex.getMessage());
				LoginI18n.ErrorMessage em = new LoginI18n.ErrorMessage();
				if (ex.getCause() instanceof DisabledException) {
					em.setTitle("Disabled");
					em.setMessage("Account is disabled");
				} else if (ex instanceof UsernameNotFoundException) {
					em.setTitle("Unknown user");
					em.setMessage("This username does not exist");
				} else if (ex instanceof BadCredentialsException) {
					em.setTitle("Bad credentials");
					em.setMessage("The password is not correct");
				} else if (ex.getCause() instanceof EmailNotValidatedException) {
					em.setTitle("Not verified");
					em.setMessage("You have not verified your email");
				} else if (ex instanceof LockedException) {
					em.setTitle("Locked");
					em.setMessage("Your account is locked");
				} else {
					em.setTitle("Authentication error");
					em.setMessage(ex.getMessage());
				}
				layout(em);
				sess.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);// already reported so clear
			}
		}
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		if (authenticatedUser.get().isPresent()) {
			// Already logged in
			setOpened(false);
			event.forwardTo(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), HomeView.class));
		}
		setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
	}

	public void addRememberMeCheckbox() {
		Checkbox rememberMe = new Checkbox("Remember me");
		Element rememberMeElement = rememberMe.getElement();
		rememberMeElement.setAttribute("name", "remember-me");
		Element loginFormElement = getElement();
		loginFormElement.appendChild(rememberMeElement);

		String executeJsForFieldString = "const field = document.getElementById($0);" + "if(field) {"
				+ "   field.after($1)" + "} else {" + "   console.error('could not find field', $0);" + "}";
		getElement().executeJs(executeJsForFieldString, "vaadinLoginPassword", rememberMeElement);
	}
}
