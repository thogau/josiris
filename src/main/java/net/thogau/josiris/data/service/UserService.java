package net.thogau.josiris.data.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.vaadin.flow.router.RouteConfiguration;

import net.thogau.josiris.data.entity.User;
import net.thogau.josiris.data.repository.UserRepository;
import net.thogau.josiris.security.Role;
import net.thogau.josiris.views.users.EmailVerificationView;
import net.thogau.josiris.views.users.ResetPasswordView;

@Service
public class UserService {

	private final UserRepository repository;

	private final MailSender mailSender;

	public UserService(UserRepository repository, MailSender mailSender) {
		this.repository = repository;
		this.mailSender = mailSender;
	}

	public class EmailVerificationException extends Exception {
	}

	public class ResetPasswordException extends Exception {
	}

	public class NoUserWithUsernameOrEmailException extends Exception {
	}

	public class EmailAlreadyUsedException extends Exception {
	}

	public Optional<User> get(Long id) {
		return repository.findById(id);
	}

	public User save(User u) throws EmailAlreadyUsedException {
		if (!u.getPassword().startsWith("$2a$10$")) {
			u.setPassword(new BCryptPasswordEncoder().encode(u.getPassword()));
		}
		if (u.getEmailVerificationCode() == null) {
			u.setEmailVerificationCode(RandomStringUtils.randomAlphanumeric(32));
		}
		try {
			return repository.save(u);
		} catch (DataIntegrityViolationException e) {
			if (e.getMostSpecificCause().getMessage().indexOf("(email)=(" + u.getEmail() + ")") >= 0) {
				throw new EmailAlreadyUsedException();
			}
			throw e;
		}
	}

	public User register(User u) throws EmailAlreadyUsedException {
		u = save(u);
		String url = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString() + "/"
				+ RouteConfiguration.forApplicationScope().getUrl(EmailVerificationView.class);
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("josiris@thogau.net");
		message.setSubject("Email verification");
		message.setText(url + "?username=" + u.getUsername() + "&code=" + u.getEmailVerificationCode());
		message.setTo(u.getEmail());
		mailSender.send(message);
		return u;
	}

	public void resetPassword(String usernameOrEmail)
			throws NoUserWithUsernameOrEmailException, EmailAlreadyUsedException {
		User u = repository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
		if (u == null) {
			throw new NoUserWithUsernameOrEmailException();
		} else {
			String url = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString() + "/"
					+ RouteConfiguration.forApplicationScope().getUrl(ResetPasswordView.class);
			u.setResetPasswordCode(RandomStringUtils.randomAlphanumeric(32));
			u = save(u);
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("josiris@thogau.net");
			message.setSubject("Reset password");
			message.setText(url + "?username=" + u.getUsername() + "&code=" + u.getResetPasswordCode());
			message.setTo(u.getEmail());
			mailSender.send(message);
		}
	}

	public void resetPassword(String username, String password)
			throws ResetPasswordException, EmailAlreadyUsedException {
		User user = repository.findByUsername(username);
		if (user != null) {
			user.setPassword(password);
			user.setResetPasswordCode(null);
			save(user);
		} else {
			throw new ResetPasswordException();
		}
	}

	public void verifyResetPassword(String username, String emailVerificationCode) throws ResetPasswordException {
		User user = repository.findByUsernameAndResetPasswordCode(username, emailVerificationCode);
		if (user == null) {
			throw new ResetPasswordException();
		}
	}

	public void activate(String username, String emailVerificationCode) throws EmailVerificationException {
		User user = repository.findByUsernameAndEmailVerificationCode(username, emailVerificationCode);
		if (user != null) {
			user.setEmailVerified(true);
			repository.save(user);
		} else {
			throw new EmailVerificationException();
		}
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public List<User> search(String filter) {
		if (filter == null || filter.isEmpty()) {
			return repository.findAll();
		} else {
			return repository.search(filter);
		}
	}

	public Page<User> list(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Page<User> list(Pageable pageable, Specification<User> filter) {
		return repository.findAll(filter, pageable);
	}

	public int count() {
		return (int) repository.count();
	}

	public void lockAccount(String username) {
		User u = repository.findByUsername(username);
		u.setAccountNonLocked(false);
		repository.save(u);
	}

	public List<User> getImitableUsers() {
		return repository.findByRolesNotIn(Arrays.asList(Role.ADMIN));
	}

}
