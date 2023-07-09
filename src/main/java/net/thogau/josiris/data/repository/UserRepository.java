package net.thogau.josiris.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.thogau.josiris.data.Role;
import net.thogau.josiris.data.entity.User;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

	User findByUsername(String username);

	User findByUsernameAndEmailVerificationCode(String username, String activationCode);

	User findByUsernameAndResetPasswordCode(String username, String activationCode);

	User findByUsernameOrEmail(String username, String email);

	@Query("select u from User u where lower(u.firstname) like lower(concat('%', :searchTerm, '%')) "
			+ "or lower(u.lastname) like lower(concat('%', :searchTerm, '%'))")
	List<User> search(@Param("searchTerm") String searchTerm);

	List<User> findByRolesNotIn(List<Role> roles);
}
