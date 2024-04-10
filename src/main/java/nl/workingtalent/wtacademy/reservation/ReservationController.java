package nl.workingtalent.wtacademy.reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
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
		return createResponseDtoList(null, reservations, null);
	}

	@RequestMapping("reservation/{id}")
	public ResponseDto findReservationById(@PathVariable("id") long id) {
		Optional<Reservation> reservationOptional = service.findReservationById(id);
		return createResponseDto(id, reservationOptional, "id");
	}

	@RequestMapping("reservation/reservationrequest/{reservationRequest}")
	public ResponseDto findAllReservationsByReservationRequest(@PathVariable("reservationRequest") boolean request) {
		List<Reservation> reservations = service.findReservationByReservationRequest(request);
		return createResponseDtoList(request, reservations, "reservation request");
	}

	@RequestMapping("reservation/requestdate/{requestDate}")
	public ResponseDto findReservationByRequestDate(@PathVariable("requestDate") LocalDate date) {
		List<Reservation> reservations = service.findReservationByRequestDate(date);
		return createResponseDtoList(date, reservations, "request date");
	}

	// Gets a list of users using the DTO
	private Stream<ReadReservationDto> getReservations(List<Reservation> reservations) {
		return reservations.stream().map(reservation -> {
			return new ReadReservationDto(reservation);
		});
	}

	// Gets the reponseDto for objects who return a single value
	private ResponseDto createResponseDto(Object pathVal, Optional<Reservation> reservationOptional, String pathVar) {
		if (reservationOptional.isPresent()) {
			Reservation reservation = reservationOptional.get();
			ReadReservationDto readReservationDto = new ReadReservationDto(reservation);
			ResponseDto responseDto = new ResponseDto(true, readReservationDto, null, "Reservation found.");
			return responseDto;
		}
		ResponseDto responseDto = new ResponseDto(false, pathVal, null,
				"No reservations with " + pathVar + " '" + pathVal + "' found.");
		return responseDto;
	}

	// Gets the responseDto for objects who return a list of values
	private ResponseDto createResponseDtoList(Object pathVal, List<Reservation> reservations, String pathVar) {
		if (reservations.isEmpty()) {
			ResponseDto responseDto = new ResponseDto(false, pathVal, null,
					"No reservations with the " + pathVar + " '" + pathVal + "' found.");
			return responseDto;
		}
		Stream<ReadReservationDto> readReservationDtoStream = getReservations(reservations);
		ResponseDto responseDto = new ResponseDto(true, readReservationDtoStream, null,
				reservations.size() + " " + pathVar + " found.");
		return responseDto;
	}
}
