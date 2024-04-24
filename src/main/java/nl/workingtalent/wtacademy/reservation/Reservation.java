package nl.workingtalent.wtacademy.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import nl.workingtalent.wtacademy.book.Book;
import nl.workingtalent.wtacademy.loan.Loan;
import nl.workingtalent.wtacademy.user.User;

@Entity
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ReservationRequest reservationRequest = ReservationRequest.PENDING;

	@Column(nullable = false)
	private LocalDate requestDate;

	@ManyToOne
	private Book book;

	@ManyToOne
	private User user;

	@OneToOne(mappedBy = "reservation")
	private Loan loan;

	private LocalDateTime handleDate;

	@ManyToOne
	private User handledBy;

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

	public LocalDateTime getHandleDate() {
		return handleDate;
	}

	public void setHandleDate(LocalDateTime handleDate) {
		this.handleDate = handleDate;
	}

	public User getHandledBy() {
		return handledBy;
	}

	public void setHandledBy(User handledBy) {
		this.handledBy = handledBy;
	}
}
