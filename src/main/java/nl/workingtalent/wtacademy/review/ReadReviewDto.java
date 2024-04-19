package nl.workingtalent.wtacademy.review;

import java.time.LocalDate;

public class ReadReviewDto {

    private byte rating;

    private String reviewText;

    private LocalDate date;

    private long bookId;

    private long userId;

    public ReadReviewDto(Review review) {
        this.rating = review.getRating();
        this.reviewText = review.getReviewText();
        this.date = review.getDate();
        this.bookId = review.getBook().getId();
        this.userId = review.getUser().getId();
    }

    public byte getRating() {
        return rating;
    }

    public void setRating(byte rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
