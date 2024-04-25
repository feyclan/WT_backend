package nl.workingtalent.wtacademy.reservation;

import java.time.LocalDateTime;
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

	/**
	 * Endpoint to get all reservations.
	 * Only users with the 'TRAINER' role can access this endpoint.
	 * @param request The HttpServletRequest object that contains the request the client made of the servlet.
	 * @return A ResponseDto object containing a list of reservations and additional information.
	 */
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

	/**
	 * Endpoint to get a reservation by its id.
	 * Only users with the 'TRAINER' role can access this endpoint.
	 * @param id The id of the reservation to fetch.
	 * @param request The HttpServletRequest object that contains the request the client made of the servlet.
	 * @return A ResponseDto object containing the requested reservation and additional information.
	 */
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

	/**
	 * Endpoint to get all reservations for the currently logged in user.
	 * @param request The HttpServletRequest object that contains the request the client made of the servlet.
	 * @return A ResponseDto object containing a list of reservations for the requested user and additional information.
	 */
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

	/**
	 * Endpoint to get all reservations for a specific user by user id.
	 * Only users with the 'TRAINER' role can access this endpoint.
	 * @param id The id of the user to fetch reservations for.
	 * @param request The HttpServletRequest object that contains the request the client made of the servlet.
	 * @return A ResponseDto object containing a list of reservations for the requested user and additional information.
	 */
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

	/**
	 * Endpoint to search for reservations based on certain criteria.
	 * Only users with the 'TRAINER' role can access this endpoint.
	 * @param dto The SearchReservationDto object that contains the search criteria.
	 * @param request The HttpServletRequest object that contains the request the client made of the servlet.
	 * @return A ResponseDto object containing the search results and additional information.
	 */
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

	/**
	 * Endpoint to add a new reservation.
	 * @param dto The CreateReservationDto object that contains the details of the reservation to be added.
	 * @param request The HttpServletRequest object that contains the request the client made of the servlet.
	 * @return A ResponseDto object containing the details of the added reservation and additional information.
	 */
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

	/**
	 * Endpoint to update an existing reservation.
	 * Only users with the 'TRAINER' role can access this endpoint.
	 * @param dto The UpdateReservationDto object that contains the updated details of the reservation.
	 * @param request The HttpServletRequest object that contains the request the client made of the servlet.
	 * @return A ResponseDto object containing the details of the updated reservation and additional information.
	 */
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
		dbReservation.setHandleDate(LocalDateTime.now());
		dbReservation.setHandledBy(user);

		// SAVE
		service.update(dbReservation);
		return new ResponseDto(true, null, null, "Reservation updated successfully.");
	}

	/**
	 * Endpoint to delete a reservation by its id.
	 * Only users with the 'TRAINER' role can access this endpoint.
	 * @param id The id of the reservation to be deleted.
	 * @param request The HttpServletRequest object that contains the request the client made of the servlet.
	 * @return A ResponseDto object containing the details of the deleted reservation and additional information.
	 */
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

	/**
	 * This method is used to map a list of Reservation objects to a stream of ReadReservationDto objects.
	 * @param reservations The list of Reservation objects to be mapped.
	 * @return A stream of ReadReservationDto objects.
	 */
	private Stream<ReadReservationDto> getReservations(List<Reservation> reservations) {
		return reservations.stream().map(reservation -> {
			return new ReadReservationDto(reservation);
		});
	}

}
