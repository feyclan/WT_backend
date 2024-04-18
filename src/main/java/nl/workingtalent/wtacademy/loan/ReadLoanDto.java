package nl.workingtalent.wtacademy.loan;

import java.time.LocalDate;

import nl.workingtalent.wtacademy.bookcopy.BookCopy;
import nl.workingtalent.wtacademy.reservation.Reservation;

public class ReadLoanDto {

	private long id;
	private LocalDate startDate;
	private LocalDate endDate;
	private String conditionStart;
	private String conditionEnd;
	private long userId;
	private Long reservationId;
	private Long bookCopyId;
	private boolean isActive;

	public ReadLoanDto(Loan loan) {
		id = loan.getId();
		startDate = loan.getStartDate();
		endDate = loan.getEndDate();
		conditionStart = loan.getConditionStart();
		conditionEnd = loan.getConditionEnd();
		userId = loan.getUser().getId();
		isActive = loan.isActive();
		// reservation en bookcopy moeten null kunnen zijn
		Reservation reservation = loan.getReservation();
		reservationId = (reservation != null) ? reservation.getId() : null;
		BookCopy bookCopy = loan.getBookCopy();
		bookCopyId = (bookCopy != null) ? bookCopy.getId() : null;
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

	public String getConditionStart() {
		return conditionStart;
	}

	public void setConditionStart(String conditionStart) {
		this.conditionStart = conditionStart;
	}

	public String getConditionEnd() {
		return conditionEnd;
	}

	public void setConditionEnd(String conditionEnd) {
		this.conditionEnd = conditionEnd;
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

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
