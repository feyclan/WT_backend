package nl.workingtalent.wtacademy.book;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBookRepository extends JpaRepository<Book, Long> {

	List<Book> findAll(Specification<Book> spec);
}
