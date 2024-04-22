package nl.workingtalent.wtacademy.loan;

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
import nl.workingtalent.wtacademy.bookcopy.BookCopy;
import nl.workingtalent.wtacademy.bookcopy.BookCopyService;
import nl.workingtalent.wtacademy.dto.ResponseDto;
import nl.workingtalent.wtacademy.reservation.Reservation;
import nl.workingtalent.wtacademy.reservation.ReservationRequest;
import nl.workingtalent.wtacademy.reservation.ReservationService;
import nl.workingtalent.wtacademy.user.Role;
import nl.workingtalent.wtacademy.user.User;
import nl.workingtalent.wtacademy.user.UserService;

@RestController
@CrossOrigin(maxAge = 3600)
public class LoanController {

	@Autowired
	private LoanService service;

	@Autowired
	private UserService userService;

	@Autowired
	private BookCopyService bookCopyService;

	@Autowired
	private ReservationService reservationService;

	// READ
	@RequestMapping("loan/all")
	public ResponseDto findAllLoans(HttpServletRequest request) {

		User user = (User) request.getAttribute("WT_USER");
		if (user == null || user.getRole() != Role.TRAINER) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		List<Loan> loans = service.findAllLoans();

		if (loans.isEmpty()) {
			return new ResponseDto(false, null, null, "No loans found.");
		}

		Stream<ReadLoanDto> readLoanDtoStream = getLoans(loans);
		return new ResponseDto(true, readLoanDtoStream, null,
				loans.size() + (loans.size() < 2 ? " loan " : " loans ") + "found.");
	}

	@RequestMapping("loan/{id}")
	public ResponseDto findLoanById(@PathVariable("id") long id, HttpServletRequest request) {

		User user = (User) request.getAttribute("WT_USER");
		if (user == null || user.getRole() != Role.TRAINER) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		Optional<Loan> loanOptional = service.findLoanById(id);

		if (loanOptional.isPresent()) {
			Loan loan = loanOptional.get();
			ReadLoanDto readLoanDto = new ReadLoanDto(loan);
			return new ResponseDto(true, readLoanDto, null, "Loan found.");
		}
		return new ResponseDto(false, null, null, "No loan found.");
	}

	//Find all loans for currently logged in user, to be displayed on profile for example
	@RequestMapping("loan/user/all")
	public ResponseDto findLoansForUser(HttpServletRequest request) {

		User user = (User) request.getAttribute("WT_USER");
		if (user == null) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		List<Loan> loans = service.findAllLoansForUser(user.getId());
		Stream<ReadLoanDto> readLoanDtoStream = getLoans(loans);

		return new ResponseDto(true, readLoanDtoStream, null,
				loans.size() + (loans.size() < 2 ? " loan " : " loans ") + "found.");
	}

	/**
	 * Endpoint to search for loans based on the criteria provided in the
	 * SearchLoanDto. The SearchLoanDto is expected to be provided in the body of
	 * the POST request.
	 *
	 * @param dto The SearchLoanDto object containing the search criteria. This is
	 *            deserialized from the request body.
	 * @return A ResponseDto object containing the result of the search. The data
	 *         field of the ResponseDto contains a stream of ReadLoanDto objects,
	 *         each representing a loan that matches the search criteria. The
	 *         message field contains a string indicating the number of loans found.
	 */
	@PostMapping("loan/search")
	public ResponseDto searchLoan(@RequestBody SearchLoanDto dto, HttpServletRequest request) {

		User user = (User) request.getAttribute("WT_USER");
		if (user == null || user.getRole() != Role.TRAINER) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		// Call the searchLoans method of the service to get a list of loans that match
		// the search criteria
		List<Loan> loans = service.searchLoans(dto);

		// Convert each Loan object in the list to a ReadLoanDto object using a stream
		// and map operation
		Stream<ReadLoanDto> dtos = loans.stream().map((loan) -> {
			return new ReadLoanDto(loan);
		});

		// Return a ResponseDto object with the stream of ReadLoanDto objects and a
		// message indicating the number of loans found
		return new ResponseDto(true, dtos, null, loans.size() + (loans.size() == 1 ? " loan " : " loans ") + "found.");
	}

	// CREATE
	@PostMapping("loan/create")
	public ResponseDto createLoan(@RequestBody CreateLoanDto dto, HttpServletRequest request) {

		User requestUser = (User) request.getAttribute("WT_USER");
		if (requestUser == null || requestUser.getRole() != Role.TRAINER) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		if (dto.getStartDate() == null) {
			return new ResponseDto(false, null, null, "Start date is required.");
		}

		if (dto.getConditionStart() == null ) {
			return new ResponseDto(false, null, null, "Condition at start is required.");
		}

		Optional<User> optionalUser = userService.findUserById(dto.getUserId());

		// DOES USER EXIST?
		if (optionalUser.isEmpty()) {
			return new ResponseDto(false, null, null, "User not found.");
		}
		User user = optionalUser.get();

//		 DOES BOOKCOPY EXIST?
		Optional<BookCopy> optionalBookCopy = bookCopyService.getBookCopyById(dto.getBookCopyId());
		if (optionalBookCopy.isEmpty()) {
			return new ResponseDto(false, null, null, "BookCopy not found.");
		}
		BookCopy copy = optionalBookCopy.get();
		if (!copy.isAvailable()) {
			return new ResponseDto(false, null, null, "BookCopy is not available.");
		}

//		 DOES RESERVATION EXIST?
		Optional<Reservation> optionalReservation = reservationService.findReservationById(dto.getReservationId());
		Reservation reservation = optionalReservation.orElse(null);

//		 CREATE LOAN AND SET VALUES
		Loan newLoan = new Loan();
		newLoan.setStartDate(dto.getStartDate());
		newLoan.setEndDate(dto.getEndDate());
		newLoan.setConditionStart(dto.getConditionStart());
		newLoan.setConditionEnd(dto.getConditionEnd());
		newLoan.setUser(user);
		newLoan.setBookCopy(copy);
		newLoan.setReservation(reservation);
		newLoan.setActive(true);

		service.create(newLoan);

//		BookCopy is not available anymore
		copy.setAvailable(false);
		bookCopyService.update(copy);

//		 Reservation is accepted
		if (reservation != null) {
			reservation.setReservationRequest(ReservationRequest.ACCEPTED);
			reservationService.update(reservation);
		}
		return new ResponseDto(true, null, null, "Loan created successfully.");
	}

	// UPDATE
	@PutMapping("loan/update")
	public ResponseDto updateLoan(@RequestBody UpdateLoanDto dto, HttpServletRequest request) {

		User user = (User) request.getAttribute("WT_USER");
		if (user == null || user.getRole() != Role.TRAINER) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		// DOES LOAN EXIST?
		Optional<Loan> existingLoan = service.findLoanById(dto.getId());
		if (existingLoan.isEmpty()) {
			return new ResponseDto(false, null, null, "Loan doesn't exist.");
		}

		Loan dbLoan = existingLoan.get();

		// OVERWRITE
		if (dto.getStartDate() != null) {
			dbLoan.setStartDate(dto.getStartDate());
		}
		if (dto.getEndDate() != null) {
			dbLoan.setEndDate(dto.getEndDate());
		}
		if (dto.getConditionStart() != null) {
			dbLoan.setConditionStart(dto.getConditionStart());
		}
		if (dto.getConditionEnd() != null) {
			dbLoan.setConditionEnd(dto.getConditionEnd());
		}
		if (dto.getIsCurrentlyUsed() != null) {
			dbLoan.setActive(dto.getIsCurrentlyUsed());
		}

//		BookCopy is available again if loan is ended
		if (!dbLoan.isActive()) {
			dbLoan.getBookCopy().setAvailable(true);
		}

		// SAVE
		service.update(dbLoan);
		return new ResponseDto(true, null, null, "Loan updated successfully.");
	}

	// DELETE
	@DeleteMapping("loan/delete/{id}")
	public ResponseDto deleteLoan(@PathVariable("id") long id, HttpServletRequest request) {

		User requestUser = (User) request.getAttribute("WT_USER");
		if (requestUser == null || requestUser.getRole() != Role.TRAINER) {
			return ResponseDto.createPermissionDeniedResponse();
		}

		Optional<Loan> loan = service.findLoanById(id);

		if (loan.isEmpty()) {
			return new ResponseDto(false, null, null, "Loan doesn't exist.");
		}
		service.delete(id);
		return new ResponseDto(true, null, null, "Loan deleted successfully.");
	}

	// Gets a list of users using the DTO
	private Stream<ReadLoanDto> getLoans(List<Loan> loans) {
		return loans.stream().map(loan -> {
			return new ReadLoanDto(loan);
		});
	}

}
