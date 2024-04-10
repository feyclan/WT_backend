package nl.workingtalent.wtacademy.reservation;

import java.time.LocalDate;

import nl.workingtalent.wtacademy.book.Book;
import nl.workingtalent.wtacademy.loan.Loan;
import nl.workingtalent.wtacademy.user.User;

public class ReadReservationDto {
	
	private long id;

	private boolean reservationRequest;

	private LocalDate requestDate;
	
	private Book book;
	
	private User user;
	
	private Loan loan;
	
	public ReadReservationDto(Reservation reservation) {
		id = reservation.getId();
		reservationRequest = reservation.isReservationRequest();
		requestDate = reservation.getRequestDate();	
		book = reservation.getBook();
		user = reservation.getUser();
		loan = reservation.getLoan();
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

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Loan getLoan() {
		return loan;
	}

	public void setLoan(Loan loan) {
		this.loan = loan;
	}
	
}
