package nl.workingtalent.wtacademy.bookcopy;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IBookCopyRepository extends JpaRepository<BookCopy, Long> {
	List<BookCopy> findByBookId(long id);

}
