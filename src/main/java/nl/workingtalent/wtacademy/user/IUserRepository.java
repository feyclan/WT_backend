package nl.workingtalent.wtacademy.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import nl.workingtalent.wtacademy.book.Book;

public interface IUserRepository extends JpaRepository<User, Long> {

	Optional<User> findUserByEmail(String user);

	// Derived query
	List<User> findAll(Specification<User> spec);

}