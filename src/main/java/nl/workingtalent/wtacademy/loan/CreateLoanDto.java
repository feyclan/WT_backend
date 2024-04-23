package nl.workingtalent.wtacademy.loan;

import nl.workingtalent.wtacademy.bookcopy.State;

import java.time.LocalDate;

public class CreateLoanDto {

	private LocalDate startDate;
	private LocalDate endDate;
	private State conditionStart;
	private State conditionEnd;
	private long userId;
	private long reservationId;
	private long bookCopyId;

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

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getReservationId() {
		return reservationId;
	}

	public void setReservationId(long reservationId) {
		this.reservationId = reservationId;
	}

	public long getBookCopyId() {
		return bookCopyId;
	}

	public void setBookCopyId(long bookCopyId) {
		this.bookCopyId = bookCopyId;
	}

}
