package nl.workingtalent.wtacademy.reservation;

import java.time.LocalDate;

public class CreateReservationDto {

	private ReservationRequest reservationRequest;
	private LocalDate requestDate;
	private long book;
	private long user;

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

}
