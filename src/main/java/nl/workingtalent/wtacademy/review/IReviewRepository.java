package nl.workingtalent.wtacademy.review;

import nl.workingtalent.wtacademy.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IReviewRepository extends JpaRepository<Review, Long>{

    Page<Review> findAllByBookId(Long bookId, Pageable pageable);

}
