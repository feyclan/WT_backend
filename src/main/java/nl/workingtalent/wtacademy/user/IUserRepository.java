package nl.workingtalent.wtacademy.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findUserByLastName(String user);

}