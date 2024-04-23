package nl.workingtalent.wtacademy.loan;

import java.time.LocalDate;

import jakarta.persistence.*;
import nl.workingtalent.wtacademy.bookcopy.BookCopy;
import nl.workingtalent.wtacademy.bookcopy.State;
import nl.workingtalent.wtacademy.reservation.Reservation;
import nl.workingtalent.wtacademy.user.User;

@Entity
public class Loan {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false)
	private LocalDate startDate;
	
	private LocalDate endDate;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private State conditionStart;

	@Enumerated(EnumType.STRING)
	private State conditionEnd;
	
	@ManyToOne
	private User user;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="reservation_id", referencedColumnName = "id")
	private Reservation reservation;
	
	@ManyToOne()
	private BookCopy bookCopy;
	
	private boolean isActive;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public State getConditionStart() {
		return conditionStart;
	}

	public void setConditionStart(State conditionStart) {
		this.conditionStart = conditionStart;
	}

	public State getConditionEnd() {
		return conditionEnd;
	}

	public void setConditionEnd(State conditionEnd) {
		this.conditionEnd = conditionEnd;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Reservation getReservation() {
		return reservation;
	}

	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}

	public BookCopy getBookCopy() {
		return bookCopy;
	}

	public void setBookCopy(BookCopy bookCopy) {
		this.bookCopy = bookCopy;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}	
	
}
