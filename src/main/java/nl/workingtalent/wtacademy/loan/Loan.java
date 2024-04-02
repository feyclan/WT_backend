package nl.workingtalent.wtacademy.loan;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import nl.workingtalent.wtacademy.bookcopy.BookCopy;
import nl.workingtalent.wtacademy.reservation.Reservation;
import nl.workingtalent.wtacademy.user.User;

@Entity
public class Loan {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private LocalDate startDate;
	
	private LocalDate endDate;
	
	private String conditionStart;
	
	private String conditionEnd;
	
	@ManyToOne
	private User user;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="reservation_id", referencedColumnName = "id")
	private Reservation reservation;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="bookcopy_id", referencedColumnName = "id")
	private BookCopy bookCopy;

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

	public String getConditionStart() {
		return conditionStart;
	}

	public void setConditionStart(String conditionStart) {
		this.conditionStart = conditionStart;
	}

	public String getConditionEnd() {
		return conditionEnd;
	}

	public void setConditionEnd(String conditionEnd) {
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
	
}
