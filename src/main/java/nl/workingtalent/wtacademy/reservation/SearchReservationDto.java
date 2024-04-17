package nl.workingtalent.wtacademy.reservation;

import java.time.LocalDate;

public class SearchReservationDto {

	private ReservationRequest reservationRequest;
	private LocalDate requestDate;

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

}
