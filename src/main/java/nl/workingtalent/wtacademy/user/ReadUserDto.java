package nl.workingtalent.wtacademy.user;

import java.util.List;

import java.util.stream.Collectors;

public class ReadUserDto {

	private long id;

	private String firstName;

	private String lastName;

	private String email;

	private Role role;

	private List<Long> reviews;

	private List<Long> reservations;

	private List<Long> loans;

	public ReadUserDto(User user) {
		id = user.getId();
		firstName = user.getFirstName();
		lastName = user.getLastName();
		email = user.getEmail();
		role = user.getRole();
		reviews = user.getReviews().stream().map(review -> review.getId()).collect(Collectors.toList());
		reservations = user.getReservations().stream().map(reservation -> reservation.getId())
				.collect(Collectors.toList());
		loans = user.getLoans().stream().map(loan -> loan.getId()).collect(Collectors.toList());
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

	public List<Long> getReviews() {
		return reviews;
	}

	public void setReviews(List<Long> reviews) {
		this.reviews = reviews;
	}

	public List<Long> getReservations() {
		return reservations;
	}

	public void setReservations(List<Long> reservations) {
		this.reservations = reservations;
	}

	public List<Long> getLoans() {
		return loans;
	}

	public void setLoans(List<Long> loans) {
		this.loans = loans;
	}

}
