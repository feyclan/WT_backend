package nl.workingtalent.wtacademy.user;

import java.util.List;

import nl.workingtalent.wtacademy.loan.Loan;
import nl.workingtalent.wtacademy.reservation.Reservation;
import nl.workingtalent.wtacademy.review.Review;

public class ReadUserDto {

	private long id;

	private String firstName;

	private String lastName;

	private String email;

	private Role role;

	private List<Review> reviews;

	private List<Reservation> reservations;

	private List<Loan> loans;

	public ReadUserDto(User user) {
		id = user.getId();
		firstName = user.getFirstName();
		lastName = user.getLastName();
		email = user.getEmail();
		role = user.getRole();
		reviews = user.getReviews();
		reservations = user.getReservations();
		loans = user.getLoans();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}

	public List<Loan> getLoans() {
		return loans;
	}

	public void setLoans(List<Loan> loans) {
		this.loans = loans;
	}

}
