package nl.workingtalent.wtacademy.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User, Long> {

	// Derived query
	List<User> findAll(Specification<User> spec);
	
	Optional<User> findByEmailAndPassword(String email, String password);

}