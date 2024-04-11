package nl.workingtalent.wtacademy.reservation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nl.workingtalent.wtacademy.dto.ResponseDto;

@RestController
@CrossOrigin(maxAge = 3600)
public class ReservationController {

	@Autowired
	private ReservationService service;

	// READ
	@RequestMapping("reservation/all")
	public ResponseDto findAllReservations() {
		List<Reservation> reservations = service.findAllReservations();

		if (reservations.isEmpty()) {
			return new ResponseDto(false, null, null, "No reservations found.");
		}

		Stream<ReadReservationDto> readReservationDtoStream = getReservations(reservations);
		return new ResponseDto(true, readReservationDtoStream, null,
				reservations.size() + (reservations.size() < 2 ? " reservation " : " reservations ") + "found.");
	}

	@RequestMapping("reservation/{id}")
	public ResponseDto findReservationById(@PathVariable("id") long id) {
		Optional<Reservation> reservationOptional = service.findReservationById(id);

		if (reservationOptional.isPresent()) {
			Reservation reservation = reservationOptional.get();
			ReadReservationDto readReservationDto = new ReadReservationDto(reservation);
			return new ResponseDto(true, readReservationDto, null, "Reservation found.");
		}
		return new ResponseDto(false, null, null, "No reservations found.");
	}

	@RequestMapping("reservation/search")
	public Stream<ReadReservationDto> searchReservation(@RequestBody SearchReservationDto dto) {

		Stream<ReadReservationDto> dtos = service.searchReservations(dto).stream().map((reservation) -> {
			return new ReadReservationDto(reservation);
		});
		return dtos;
	}

	// CREATE
	@PostMapping("reservation/create")
	public ResponseDto createReservation(@RequestBody CreateReservationDto dto) {

		Reservation newReservation = new Reservation();
		newReservation.setReservationRequest(dto.isReservationRequest());
		newReservation.setRequestDate(dto.getRequestDate());
		service.create(newReservation);

		return new ResponseDto(true, newReservation, null, "Reservation created successfully.");
	}

	// UPDATE
	@PutMapping("reservation/update/{id}")
	public ResponseDto updateReservation(@RequestBody UpdateReservationDto dto, @PathVariable("id") long id) {

		// DOES RESERVATION EXIST?
		Optional<Reservation> existingReservation = service.findReservationById(id);
		if (existingReservation.isEmpty()) {
			return new ResponseDto(false, existingReservation, null, "Reservation doesn't exist.");
		}

		Reservation dbReservation = existingReservation.get();

		// OVERWRITE
		dbReservation.setReservationRequest(dto.isReservationRequest());
		dbReservation.setRequestDate(dto.getRequestDate());

		// SAVE
		service.update(dbReservation);
		return new ResponseDto(true, dbReservation, null, "Reservation updated successfully.");
	}

	// DELETE
	@DeleteMapping("reservation/delete/{id}")
	public ResponseDto deleteReservation(@PathVariable("id") long id) {
		Optional<Reservation> reservation = service.findReservationById(id);

		if (reservation.isEmpty()) {
			return new ResponseDto(false, null, null, "Reservation doesn't exist.");
		}
		service.delete(id);
		return new ResponseDto(true, null, null, "Reservation deleted successfully.");
	}

	// Gets a list of users using the DTO
	private Stream<ReadReservationDto> getReservations(List<Reservation> reservations) {
		return reservations.stream().map(reservation -> {
			return new ReadReservationDto(reservation);
		});
	}

}
