package nl.workingtalent.wtacademy.reservation;

public class UpdateReservationDto {

	private long id;
	private ReservationRequest reservationRequest;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ReservationRequest getReservationRequest() {
		return reservationRequest;
	}

	public void setReservationRequest(ReservationRequest reservationRequest) {
		this.reservationRequest = reservationRequest;
	}

}
