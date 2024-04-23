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

import jakarta.servlet.http.HttpServletRequest;
import nl.workingtalent.wtacademy.book.Book;
import nl.workingtalent.wtacademy.book.BookService;
import nl.workingtalent.wtacademy.dto.ResponseDto;
import nl.workingtalent.wtacademy.user.Role;
import nl.workingtalent.wtacademy.user.User;
import nl.workingtalent.wtacademy.user.UserService;

@RestController
@CrossOrigin(maxAge = 3600)
public class ReservationController {

	@Autowired
	private ReservationService service;

	@Autowired
	private BookService bookService;

	@Autowired
	private UserService userService;

	// READ
	@RequestMapping("reservation/all")
	public ResponseDto findAllReservations(HttpServletRequest request) {

		User user = (User) request.getAttribute("WT_USER");
		if (user == null || user.getRole() != Role.TRAINER) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		List<Reservation> reservations = service.findAllReservations();

		if (reservations.isEmpty()) {
			return new ResponseDto(false, null, null, "No reservations found.");
		}

		Stream<ReadReservationDto> readReservationDtoStream = getReservations(reservations);
		return new ResponseDto(true, readReservationDtoStream, null,
				reservations.size() + (reservations.size() < 2 ? " reservation " : " reservations ") + "found.");
	}

	@RequestMapping("reservation/{id}")
	public ResponseDto findReservationById(@PathVariable("id") long id, HttpServletRequest request) {

		User user = (User) request.getAttribute("WT_USER");
		if (user == null || user.getRole() != Role.TRAINER) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		Optional<Reservation> reservationOptional = service.findReservationById(id);

		if (reservationOptional.isPresent()) {
			Reservation reservation = reservationOptional.get();
			ReadReservationDto readReservationDto = new ReadReservationDto(reservation);
			return new ResponseDto(true, readReservationDto, null, "Reservation found.");
		}
		return new ResponseDto(false, null, null, "No reservation found.");
	}

	//Find all reservations for currently logged in user, to be displayed on profile for example
	@RequestMapping("reservation/user/all")
	public ResponseDto findReservationsForUser(HttpServletRequest request) {

		User user = (User) request.getAttribute("WT_USER");
		if (user == null) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		List<Reservation> reservations = service.findAllReservationsForUser(user.getId());
		Stream<ReadReservationDto> readReservationDtoStream = getReservations(reservations);

		return new ResponseDto(true, readReservationDtoStream, null,
				reservations.size() + (reservations.size() < 2 ? " reservation " : " reservations ") + "found.");
	}
	
	@RequestMapping("reservation/user/{id}")
	public ResponseDto findReservationsForUser(@PathVariable("id") long id, HttpServletRequest request) {

		User user = (User) request.getAttribute("WT_USER");
		if (user == null || user.getRole() != Role.TRAINER) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		List<Reservation> reservations = service.findAllReservationsForUser(id);
		Stream<ReadReservationDto> readReservationDtoStream = getReservations(reservations);

		return new ResponseDto(true, readReservationDtoStream, null,
				reservations.size() + (reservations.size() < 2 ? " reservation " : " reservations ") + "found.");
	}

	@PostMapping("reservation/search")
	public ResponseDto searchReservation(@RequestBody SearchReservationDto dto, HttpServletRequest request) {

		User user = (User) request.getAttribute("WT_USER");
		if (user == null || user.getRole() != Role.TRAINER) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		List<Reservation> reservations = service.searchReservations(dto);
		Stream<ReadReservationDto> dtos = reservations.stream().map((reservation) -> {
			return new ReadReservationDto(reservation);
		});
		return new ResponseDto(true, dtos, null,
				reservations.size() + (reservations.size() < 2 ? " reservation " : " reservations ") + "found.");
	}

	// CREATE
	@PostMapping("reservation/create")
	public ResponseDto createReservation(@RequestBody CreateReservationDto dto, HttpServletRequest request) {

		User user = (User) request.getAttribute("WT_USER");
		if (user == null) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		if (dto.getRequestDate() == null) {
			return new ResponseDto(false, null, null, "Date required.");
		}

		if (dto.getReservationRequest() == null) {
			return new ResponseDto(false, null, null, "Reservation status is required.");
		}

		Optional<Book> optionalBook = bookService.getBookById(dto.getBookId());

		// DOES BOOK EXIST?
		if (optionalBook.isEmpty()) {
			return new ResponseDto(false, null, null, "Book not found.");
		}

		Book book = optionalBook.get();

		Reservation newReservation = new Reservation();
		newReservation.setReservationRequest(dto.getReservationRequest());
		newReservation.setRequestDate(dto.getRequestDate());
		newReservation.setBook(book);
		newReservation.setUser(user);

		service.create(newReservation);
		return new ResponseDto(true, null, null, "Reservation created successfully.");
	}

	// UPDATE
	@PutMapping("reservation/update")
	public ResponseDto updateReservation(@RequestBody UpdateReservationDto dto, HttpServletRequest request) {

		User user = (User) request.getAttribute("WT_USER");
		if (user == null || user.getRole() != Role.TRAINER) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		if (dto.getReservationRequest() == null) {
			return new ResponseDto(false, null, null, "Reservation status is required.");
		}

		// DOES RESERVATION EXIST?
		Optional<Reservation> existingReservation = service.findReservationById(dto.getId());
		if (existingReservation.isEmpty()) {
			return new ResponseDto(false, existingReservation, null, "Reservation doesn't exist.");
		}

		Reservation dbReservation = existingReservation.get();

		// OVERWRITE
		dbReservation.setReservationRequest(dto.getReservationRequest());

		// SAVE
		service.update(dbReservation);
		return new ResponseDto(true, null, null, "Reservation updated successfully.");
	}

	// DELETE
	@DeleteMapping("reservation/delete/{id}")
	public ResponseDto deleteReservation(@PathVariable("id") long id, HttpServletRequest request) {

		User user = (User) request.getAttribute("WT_USER");
		if (user == null || user.getRole() != Role.TRAINER) {
			return ResponseDto.createPermissionDeniedResponse();
		}

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
