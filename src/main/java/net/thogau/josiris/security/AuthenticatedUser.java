package net.thogau.josiris.security;

import java.util.Optional;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.vaadin.flow.server.VaadinServletRequest;

import jakarta.servlet.ServletException;
import net.thogau.josiris.data.entity.User;
import net.thogau.josiris.data.repository.UserRepository;

@Component
public class AuthenticatedUser {

	private final UserRepository userRepository;

	public AuthenticatedUser(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public Optional<User> get() {
		return getAuthentication().map(authentication -> userRepository.findByUsername(authentication.getName()));
	}

	public void logout() {
		try {
			VaadinServletRequest.getCurrent().getHttpServletRequest().logout();
		} catch (ServletException e) {
			throw new RuntimeException(e);
		}
	}

	private Optional<Authentication> getAuthentication() {
		SecurityContext context = SecurityContextHolder.getContext();
		return Optional.ofNullable(context.getAuthentication())
				.filter(authentication -> !(authentication instanceof AnonymousAuthenticationToken));
	}
}
