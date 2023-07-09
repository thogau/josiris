package net.thogau.josiris.data.entity;

import java.util.Date;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thogau.josiris.data.Role;

@Entity
@Table(name = "application_user")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractEntity {

	@NotEmpty
	@Column(unique = true)
	private String username;

	@NotEmpty
	private String firstname;

	@NotEmpty
	private String lastname;

	@Email
	@NotEmpty
	@Column(unique = true)
	private String email;

	@Builder.Default
	private boolean accountNonExpired = true;

	@Builder.Default
	private boolean accountNonLocked = true;

	@Builder.Default
	private boolean credentialsNonExpired = true;

	@Builder.Default
	private boolean enabled = true;

	@NotEmpty
	private String password;

	@NotEmpty
	private String emailVerificationCode;

	private String resetPasswordCode;

	@Builder.Default
	private boolean emailVerified = false;

	private Date passwordValidity;

	private Date accountValidity;

	@Enumerated(EnumType.STRING)
	@ElementCollection(fetch = FetchType.EAGER)
	private Set<Role> roles;

	@Transient
	public String getFullName() {
		return firstname + " " + lastname;
	}

}
