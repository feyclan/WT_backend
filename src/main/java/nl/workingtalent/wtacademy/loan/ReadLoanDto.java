package nl.workingtalent.wtacademy.loan;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import nl.workingtalent.wtacademy.bookcopy.State;
import nl.workingtalent.wtacademy.reservation.Reservation;

public class ReadLoanDto {

	private long id;
	private LocalDate startDate;
	private LocalDate endDate;
	private State conditionStart;
	private State conditionEnd;
	private String bookTitle;
	private List<String> bookAuthors;
	private long userId;
	private Long reservationId;
	private Long bookCopyId;
	private Long bookId;
	private Boolean isActive;

	public ReadLoanDto(Loan loan) {
		id = loan.getId();
		startDate = loan.getStartDate();
		endDate = loan.getEndDate();
		conditionStart = loan.getConditionStart();
		conditionEnd = loan.getConditionEnd();
		userId = loan.getUser().getId();
		isActive = loan.isActive();
		bookCopyId = loan.getBookCopy().getId();
		bookId = loan.getBookCopy().getBook().getId();
		bookTitle = loan.getBookCopy().getBook().getTitle();
		bookAuthors = loan.getBookCopy().getBook().getAuthors().stream().map(author -> author.getName())
				.collect(Collectors.toList());
		
		// Reservation is null if the book has never been reserved before
		Reservation reservation = loan.getReservation();
		reservationId = reservation != null ? reservation.getId() : null;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public State getConditionStart() {
		return conditionStart;
	}

	public void setConditionStart(State conditionStart) {
		this.conditionStart = conditionStart;
	}

	public State getConditionEnd() {
		return conditionEnd;
	}

	public void setConditionEnd(State conditionEnd) {
		this.conditionEnd = conditionEnd;
	}

	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public List<String> getBookAuthors() {
		return bookAuthors;
	}

	public void setBookAuthors(List<String> bookAuthors) {
		this.bookAuthors = bookAuthors;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Long getReservationId() {
		return reservationId;
	}

	public void setReservationId(Long reservationId) {
		this.reservationId = reservationId;
	}

	public Long getBookCopyId() {
		return bookCopyId;
	}

	public void setBookCopyId(Long bookCopyId) {
		this.bookCopyId = bookCopyId;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
}
