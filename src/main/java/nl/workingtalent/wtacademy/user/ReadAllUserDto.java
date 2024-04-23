package nl.workingtalent.wtacademy.user;

import java.util.stream.Stream;

public class ReadAllUserDto {

	private int totalPages;

	private Stream<ReadUserDto> users;

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public Stream<ReadUserDto> getUsers() {
		return users;
	}

	public void setUsers(Stream<ReadUserDto> users) {
		this.users = users;
	}

}
