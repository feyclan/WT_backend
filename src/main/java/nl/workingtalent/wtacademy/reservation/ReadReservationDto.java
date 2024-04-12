package nl.workingtalent.wtacademy.reservation;

import java.time.LocalDate;

import nl.workingtalent.wtacademy.loan.Loan;

public class ReadReservationDto {
	
	private long id;

	private boolean reservationRequest;

	private LocalDate requestDate;
	
	private long book;
	
	private long user;
	
	private Long loan;
	
	public ReadReservationDto(Reservation reservation) {
		id = reservation.getId();
		reservationRequest = reservation.isReservationRequest();
		requestDate = reservation.getRequestDate();	
		book = reservation.getBook().getId();
		user = reservation.getUser().getId();
		// loan moet null kunnen zijn
		Loan reservationLoan = reservation.getLoan();
	    loan = (reservationLoan != null) ? reservationLoan.getId() : null;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isReservationRequest() {
		return reservationRequest;
	}

	public void setReservationRequest(boolean reservationRequest) {
		this.reservationRequest = reservationRequest;
	}

	public LocalDate getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(LocalDate requestDate) {
		this.requestDate = requestDate;
	}

	public long getBook() {
		return book;
	}

	public void setBook(long book) {
		this.book = book;
	}

	public long getUser() {
		return user;
	}

	public void setUser(long user) {
		this.user = user;
	}

	public Long getLoan() {
		return loan;
	}

	public void setLoan(Long loan) {
		this.loan = loan;
	}
	
}
