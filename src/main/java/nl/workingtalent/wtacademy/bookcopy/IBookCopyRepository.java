package nl.workingtalent.wtacademy.bookcopy;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IBookCopyRepository extends JpaRepository<BookCopy, Long> {
	List<BookCopy> findByBookId(long id);
}
