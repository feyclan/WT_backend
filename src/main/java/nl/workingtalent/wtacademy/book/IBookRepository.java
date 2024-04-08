package nl.workingtalent.wtacademy.book;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IBookRepository extends JpaRepository<Book, Long> {

	List<Book> findByTitleContaining(String title);
	
	List<Book> findByCategoriesIn(List<String> categories);
}
