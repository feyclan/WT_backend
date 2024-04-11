package nl.workingtalent.wtacademy.reservation;

import java.time.LocalDate;

public class CreateReservationDto {

	private boolean reservationRequest;
	private LocalDate requestDate;

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

}
