package nl.workingtalent.wtacademy.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private IReviewRepository repository;

    public void addReview(Review review) {
        repository.save(review);
    }

    public void updateReview(Review review) {
        repository.save(review);
    }

    public void deleteReviewById(long id) {
        repository.deleteById(id);
    }

    public Optional<Review> getReviewById(long id) {
        return repository.findById(id);
    }

    public Page<Review> getAllReviewsForBook(long bookId, Pageable pageable) {
        return repository.findAllByBookId(bookId, pageable);
    }

}
