package nl.workingtalent.wtacademy.reservation;

import java.time.LocalDate;
import java.util.stream.Collectors;

import nl.workingtalent.wtacademy.book.Book;
import nl.workingtalent.wtacademy.loan.Loan;
import nl.workingtalent.wtacademy.user.User;

public class ReadReservationDto {
	
	private long id;

	private boolean reservationRequest;

	private LocalDate requestDate;
	
	private long book;
	
	private long user;
	
	private long loan;
	
	public ReadReservationDto(Reservation reservation) {
		id = reservation.getId();
		reservationRequest = reservation.isReservationRequest();
		requestDate = reservation.getRequestDate();	
		book = reservation.getBook().getId();
		user = reservation.getUser().getId();
		loan = reservation.getLoan().getId();
		
//		
//		books = reservation.getBook().stream().map(book -> book.getId()).collect(Collectors.toList());
//		
//		
//		
//		
//		
//		reservations = user.getReservations().stream().map(reservation -> reservation.getId())
//				.collect(Collectors.toList());
//		loans = user.getLoans().stream().map(loan -> loan.getId()).collect(Collectors.toList());
		
		
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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


	public long getBook() {
		return book;
	}

	public void setBook(long book) {
		this.book = book;
	}

	public Long getUser() {
		return user;
	}

	public void setUser(Long user) {
		this.user = user;
	}

	public Long getLoan() {
		return loan;
	}

	public void setLoan(Long loan) {
		this.loan = loan;
	}
	
}
