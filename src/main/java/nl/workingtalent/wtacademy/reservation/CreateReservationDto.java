package nl.workingtalent.wtacademy.reservation;

import java.time.LocalDate;

public class CreateReservationDto {

	private ReservationRequest reservationRequest;
	private LocalDate requestDate;
	private long bookId;
	private long userId;

	public ReservationRequest getReservationRequest() {
		return reservationRequest;
	}

	public void setReservationRequest(ReservationRequest reservationRequest) {
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

	public void setBookId(long book) {
		this.bookId = book;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long user) {
		this.userId = user;
	}

}
