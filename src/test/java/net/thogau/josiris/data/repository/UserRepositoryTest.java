package net.thogau.josiris.data.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import net.thogau.josiris.data.Role;
import net.thogau.josiris.data.entity.User;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class UserRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UserRepository repository;

	@Test
	public void should_find_users_not_having_role() {
		User u1 = User.builder().username("user1").firstname("firstname").lastname("lastname")
				.email("email@example.com").password("password").emailVerificationCode("xxxx")
				.roles(new HashSet<>(Arrays.asList(Role.USER))).build();
		User u2 = User.builder().username("admin").firstname("firstname").lastname("lastname")
				.email("email2@example.com").password("password").emailVerificationCode("xxxx")
				.roles(new HashSet<>(Arrays.asList(Role.ADMIN))).build();
		User u3 = User.builder().username("user2").firstname("firstname").lastname("lastname")
				.email("email3@example.com").password("password").emailVerificationCode("xxxx")
				.roles(new HashSet<>(Arrays.asList(Role.USER))).build();

		entityManager.persist(u1);
		entityManager.persist(u2);
		entityManager.persist(u3);

		List<User> l = repository.findByRolesNotIn(Arrays.asList(Role.ADMIN));
		assertThat(l).isNotEmpty();
		assertThat(l.size()).isEqualTo(2);
		assertThat(l).isEqualTo(new ArrayList<>(Arrays.asList(u1, u3)));
	}

}
