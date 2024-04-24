package nl.workingtalent.wtacademy.user;

public class SearchUserDto {

	private String firstName;

	private String lastName;

	private String email;

	private String role;

	private int pageNr;
	
	private String fullName;

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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setPageNr(int pageNr) {
		this.pageNr = pageNr;
	}

	public int getPageNr() {
		return pageNr;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getFullName() {
		return fullName;
	}

}
