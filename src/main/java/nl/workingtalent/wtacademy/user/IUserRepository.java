package nl.workingtalent.wtacademy.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findUserByFirstName(String user);
	Optional<User> findUserByLastName(String user);
	Optional<User> findUserByEmail(String user);
	Optional<User> findUserByRole(Role role);
	
}