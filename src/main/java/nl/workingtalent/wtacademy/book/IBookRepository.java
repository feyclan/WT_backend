package nl.workingtalent.wtacademy.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBookRepository extends JpaRepository<Book, Long> {

	// Derived query
	Page<Book> findAll(Specification<Book> spec, Pageable page);

}
