package nl.workingtalent.wtacademy.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User, Long> {
	
	List<User> findUserByFirstName(String user);
	List<User> findUserByLastName(String user);
	Optional<User> findUserByEmail(String user);
	List<User> findUserByRole(Role role);
	
}