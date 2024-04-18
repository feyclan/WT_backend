package nl.workingtalent.wtacademy.dto;

public class LoginResponseDto {

	private String token;

	private String name;

	private String Role;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRole(String role) {
		Role = role;
	}

	public String getRole() {
		return Role;
	}

}