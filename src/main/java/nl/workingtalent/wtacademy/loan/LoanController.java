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

import nl.workingtalent.wtacademy.bookcopy.BookCopy;
import nl.workingtalent.wtacademy.bookcopy.BookCopyService;
import nl.workingtalent.wtacademy.dto.ResponseDto;
import nl.workingtalent.wtacademy.reservation.Reservation;
import nl.workingtalent.wtacademy.reservation.ReservationRequest;
import nl.workingtalent.wtacademy.reservation.ReservationService;
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
	public ResponseDto findAllLoans() {
		List<Loan> loans = service.findAllLoans();

		if (loans.isEmpty()) {
			return new ResponseDto(false, null, null, "No loans found.");
		}

		Stream<ReadLoanDto> readLoanDtoStream = getLoans(loans);
		return new ResponseDto(true, readLoanDtoStream, null,
				loans.size() + (loans.size() < 2 ? " loan " : " loans ") + "found.");
	}

	@RequestMapping("loan/{id}")
	public ResponseDto findLoanById(@PathVariable("id") long id) {
		Optional<Loan> loanOptional = service.findLoanById(id);

		if (loanOptional.isPresent()) {
			Loan loan = loanOptional.get();
			ReadLoanDto readLoanDto = new ReadLoanDto(loan);
			return new ResponseDto(true, readLoanDto, null, "Loan found.");
		}
		return new ResponseDto(false, null, null, "No loan found.");
	}

	@RequestMapping("loan/search")
	public ResponseDto searchLoan(@RequestBody SearchLoanDto dto) {
		List<Loan> loans = service.searchLoans(dto);
		Stream<ReadLoanDto> dtos = loans.stream().map((loan) -> {
			return new ReadLoanDto(loan);
		});
		return new ResponseDto(true, dtos, null, loans.size() + (loans.size() < 2 ? " loan " : " loans ") + "found.");
	}

	// CREATE
	@PostMapping("loan/create")
	public ResponseDto createLoan(@RequestBody CreateLoanDto dto) {
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
		
		copy.setAvailable(false);
		bookCopyService.update(copy);

		if (reservation != null) {
			reservation.setReservationRequest(ReservationRequest.ACCEPTED);
			reservationService.update(reservation);
		}
		return new ResponseDto(true, null, null, "Loan created successfully.");
	}

	// UPDATE
	@PutMapping("loan/update")
	public ResponseDto updateLoan(@RequestBody UpdateLoanDto dto) {

		// DOES LOAN EXIST?
		Optional<Loan> existingLoan = service.findLoanById(dto.getId());
		if (existingLoan.isEmpty()) {
			return new ResponseDto(false, null, null, "Loan doesn't exist.");
		}

		Loan dbLoan = existingLoan.get();

		// OVERWRITE
		dbLoan.setStartDate(dto.getStartDate());
		dbLoan.setEndDate(dto.getEndDate());
		dbLoan.setConditionStart(dto.getConditionStart());
		dbLoan.setConditionEnd(dto.getConditionEnd());
		dbLoan.setActive(dto.isActive());
		
//		BookCopy is available again if loan is ended
		if(!dbLoan.isActive()) {
			dbLoan.getBookCopy().setAvailable(true);
		}

		// SAVE
		service.update(dbLoan);
		return new ResponseDto(true, null, null, "Loan updated successfully.");
	}

	// DELETE
	@DeleteMapping("loan/delete/{id}")
	public ResponseDto deleteLoan(@PathVariable("id") long id) {
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
