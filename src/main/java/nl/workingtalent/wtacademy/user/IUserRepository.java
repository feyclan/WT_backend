package nl.workingtalent.wtacademy.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User, Long> {

	// Derived query
	Page<User> findAll(Specification<User> spec, Pageable page);
	
	Optional<User> findByEmailAndPassword(String email, String password);
	
	Optional<User> findByToken(String token);

}