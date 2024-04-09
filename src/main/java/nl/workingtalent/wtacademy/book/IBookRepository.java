package nl.workingtalent.wtacademy.book;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nl.workingtalent.wtacademy.category.Category;

public interface IBookRepository extends JpaRepository<Book, Long> {

	List<Book> findByTitleContaining(String title);
	
	@Query("SELECT DISTINCT b from Book b JOIN b.categories c WHERE c.category in :categories")
	List<Book> findByCategoriesIn(@Param("categories") List<String> categories);
}
