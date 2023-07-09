package net.thogau.josiris.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.vaadin.flow.spring.security.VaadinWebSecurity;

import net.thogau.josiris.views.login.LoginView;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {

	public static final String LOGOUT_URL = "/logout";
	public static final String LOGOUT_SUCCESS_URL = "/";

	private final UserDetailsService userDetailsService;

	/**
	 * userDetailsService is required for the Remember Me
	 * https://docs.spring.io/spring-security/site/docs/5.0.x/reference/html/remember-me.html
	 * 
	 * @param userDetailsService
	 */
	public SecurityConfiguration(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SwitchUserFilter switchUserFilter() {
		SwitchUserFilter filter = new SwitchUserFilter();
		filter.setUserDetailsService(userDetailsService);
		filter.setSwitchUserMatcher(new AntPathRequestMatcher("/impersonate*", "GET"));
		filter.setSwitchFailureUrl("/sudo");
		filter.setTargetUrl("/");
		filter.setExitUserMatcher(new AntPathRequestMatcher("/sudo/exit", "GET"));
		filter.setSecurityContextRepository(new DelegatingSecurityContextRepository(
				new HttpSessionSecurityContextRepository(), new RequestAttributeSecurityContextRepository()));
		return filter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests().requestMatchers(new AntPathRequestMatcher("/images/*.png")).permitAll();
		http.authorizeHttpRequests().requestMatchers(new AntPathRequestMatcher("/line-awesome/**/*.svg")).permitAll();
		http.authorizeHttpRequests().requestMatchers(new AntPathRequestMatcher("/impersonate")).hasAnyRole("ADMIN",
				"ROLE_PREVIOUS_ADMINISTRATOR");
		http.authorizeHttpRequests().requestMatchers(new AntPathRequestMatcher("/sudo/exit")).permitAll();

		super.configure(http);

		setLoginView(http, LoginView.class);

		http.rememberMe().key("@nt1c0n5t1tut10n33ll3m3nT").tokenValiditySeconds(60 * 60 * 24 * 7 * 2 /* 2 weeks */)
				.userDetailsService(this.userDetailsService);
		http.logout().invalidateHttpSession(true).deleteCookies("JSESSIONID", "remember-me");

	}

}
