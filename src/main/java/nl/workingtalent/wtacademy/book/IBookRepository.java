package nl.workingtalent.wtacademy.book;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IBookRepository extends JpaRepository<Book, Long> {

	// Derived query
	List<Book> findAll(Specification<Book> spec);
	
	// JPQL
	@Query("SELECT b FROM Book b WHERE b.title LIKE ?1")
	List<Book> zoeken(String title);
	
}
