package nl.workingtalent.wtacademy.reservation;

import java.time.LocalDate;

import nl.workingtalent.wtacademy.loan.Loan;

public class ReadReservationDto {

	private long id;
	private String reservationRequest;
	private LocalDate requestDate;
	private long bookId;
	private long userId;
	private Long loanId;

	public ReadReservationDto(Reservation reservation) {
		id = reservation.getId();
		reservationRequest = reservation.getReservationRequest().toString();
		requestDate = reservation.getRequestDate();
		bookId = reservation.getBook().getId();
		userId = reservation.getUser().getId();
		// loan moet null kunnen zijn
		Loan reservationLoan = reservation.getLoan();
		loanId = (reservationLoan != null) ? reservationLoan.getId() : null;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getReservationRequest() {
		return reservationRequest;
	}

	public void setReservationRequest(String reservationRequest) {
		this.reservationRequest = reservationRequest;
	}

	public LocalDate getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(LocalDate requestDate) {
		this.requestDate = requestDate;
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

	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}

}
