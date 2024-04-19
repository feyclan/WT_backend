package nl.workingtalent.wtacademy.review;

import jakarta.servlet.http.HttpServletRequest;
import nl.workingtalent.wtacademy.book.Book;
import nl.workingtalent.wtacademy.book.BookService;
import nl.workingtalent.wtacademy.dto.ResponseDto;
import nl.workingtalent.wtacademy.user.Role;
import nl.workingtalent.wtacademy.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
public class ReviewController {

    private final int pageSize = 5;

    @Autowired
    private ReviewService service;

    @Autowired
    private BookService bookService;

    @GetMapping("review/book/all/{bookId}")
    public ResponseDto getAllReviews(@PathVariable("bookId") long bookId, HttpServletRequest request, @RequestBody int pageNr) {
        Optional<Book> bookOptional = bookService.getBookById(bookId);
        if (bookOptional.isEmpty()) {
            return new ResponseDto(false, null, null, "Book not found.");
        }
        Book book = bookOptional.get();

        User requestUser = (User) request.getAttribute("WT_USER");
        if (requestUser == null) {
            return new ResponseDto(false, null, null, "No user found.");
        }

        Pageable pageable = PageRequest.of(pageNr, pageSize);
        Page<Review> reviews = service.getAllReviewsForBook(bookId, pageable);
        Stream<ReadReviewDto> dtos = reviews.stream().map((review) -> {
            return new ReadReviewDto(review);
        });

        return new ResponseDto(true, null, null, "Reviews found.");
    }

    @PostMapping("review/create")
    public ResponseDto createReview(@RequestBody CreateReviewDto dto, HttpServletRequest request) {
        User requestUser = (User) request.getAttribute("WT_USER");
        if (requestUser == null) {
            return new ResponseDto(false, null, null, "No user found.");
        }

        Optional<Book> bookOptional = bookService.getBookById(dto.getBookId());
        if (bookOptional.isEmpty()) {
            return new ResponseDto(false, null, null, "Book not found.");
        }
        Book book = bookOptional.get();

//		Check if user has borrowed and returned the book
        boolean hasReturnedBook = requestUser.getLoans().stream()
                .anyMatch(loan -> loan.getBookCopy().getBook().getId() == book.getId() && loan.getEndDate() != null);

        if (!hasReturnedBook) {
            return new ResponseDto(false, null, null, "You have not returned the book yet.");
        }

        Review review = new Review();
//		Check rating
        if (dto.getRating() < 1 || dto.getRating() > 5) {
            return new ResponseDto(false, null, null, "Rating must be between 1 and 5.");
        } else {
            review.setRating(dto.getRating());
        }

//		Check entered date
        if (dto.getDate() != null) {
            review.setDate(dto.getDate());
        } else {
            review.setDate(LocalDate.now());
        }


        if (dto.getReviewText() != null) {
            review.setReviewText(dto.getReviewText());
        }

        review.setBook(book);
        service.addReview(review);

        return new ResponseDto(true, null, null, "Review created.");
    }

    @PutMapping("review/update")
    public ResponseDto updateReview(UpdateReviewDto dto, HttpServletRequest request) {
        User requestUser = (User) request.getAttribute("WT_USER");
        if (requestUser == null) {
            return new ResponseDto(false, null, null, "No user found.");
        }

        Optional<Review> reviewOptional = service.getReviewById(dto.getReviewId());
        if (reviewOptional.isEmpty()) {
            return new ResponseDto(false, null, null, "Review not found.");
        }
        Review review = reviewOptional.get();

        if (requestUser.getRole() == Role.TRAINER || review.getUser().getId() == requestUser.getId()) {
            if (dto.getRating() < 1 || dto.getRating() > 5) {
                return new ResponseDto(false, null, null, "Rating must be between 1 and 5.");
            } else {
                review.setRating(dto.getRating());
            }

            if (dto.getDate() != null) {
                review.setDate(dto.getDate());
            }

            if (dto.getReviewText() != null) {
                review.setReviewText(dto.getReviewText());
            }

            service.updateReview(review);
            return new ResponseDto(true, null, null, "Review updated.");
        } else {
            return new ResponseDto(false, null, null, "You are not allowed to update this review.");
        }
    }

    @DeleteMapping("review/delete/{id}")
    public ResponseDto deleteReview(@PathVariable("id") long id, HttpServletRequest request) {
        User requestUser = (User) request.getAttribute("WT_USER");
        if (requestUser == null) {
            return new ResponseDto(false, null, null, "No user found.");
        }

        Optional<Review> reviewOptional = service.getReviewById(id);
        if (reviewOptional.isEmpty()) {
            return new ResponseDto(false, null, null, "Review not found.");
        }
        Review review = reviewOptional.get();

        if (requestUser.getRole() == Role.TRAINER || review.getUser().getId() == requestUser.getId()) {
            service.deleteReviewById(id);
            return new ResponseDto(true, null, null, "Review deleted.");
        } else {
            return new ResponseDto(false, null, null, "You are not allowed to delete this review.");
        }
    }
}
