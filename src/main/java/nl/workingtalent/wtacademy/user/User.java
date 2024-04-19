package nl.workingtalent.wtacademy.user;

import java.util.List;

import jakarta.persistence.*;
import nl.workingtalent.wtacademy.loan.Loan;
import nl.workingtalent.wtacademy.reservation.Reservation;
import nl.workingtalent.wtacademy.review.Review;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(length = 50, nullable = false)
	private String firstName;
	
	@Column(length = 50, nullable = false)
	private String lastName;
	
	@Column(length = 100, nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@OneToMany(mappedBy = "user")
	private List<Review> reviews;
	
	@OneToMany(mappedBy = "user")
	private List<Reservation> reservations;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	private List<Loan> loans;
	
	@Column(length = 100, unique = true)
	private String token;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	

}
