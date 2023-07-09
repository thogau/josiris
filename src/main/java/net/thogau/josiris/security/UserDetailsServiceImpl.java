package net.thogau.josiris.security;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import net.thogau.josiris.data.entity.User;
import net.thogau.josiris.data.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DisabledException, EmailNotValidatedException {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("No user present with username: " + username);
		} else if (!user.isEnabled()) {
			throw new DisabledException("Account is disabled");
		} else if (!user.isEmailVerified()) {
			throw new EmailNotValidatedException("Email has not been verified");
		} else if (user.getAccountValidity() != null && user.getAccountValidity().toInstant().isAfter(Instant.now())) {
			user.setAccountNonExpired(false);
			user = userRepository.save(user);
		} else if (user.getPasswordValidity() != null
				&& user.getPasswordValidity().toInstant().isAfter(Instant.now())) {
			user.setCredentialsNonExpired(false);
			user = userRepository.save(user);
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				user.isEnabled(), user.isAccountNonExpired(), user.isCredentialsNonExpired(), user.isAccountNonLocked(),
				getAuthorities(user));

	}

	private static List<GrantedAuthority> getAuthorities(User user) {
		return user.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role))
				.collect(Collectors.toList());
	}

	public class EmailNotValidatedException extends AccountStatusException {
		public EmailNotValidatedException(String msg) {
			super(msg);
		}
	}

}
