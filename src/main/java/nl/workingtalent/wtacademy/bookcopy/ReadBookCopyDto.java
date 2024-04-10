package nl.workingtalent.wtacademy.bookcopy;

public class ReadBookCopyDto {

	private String state;

	private String location;

	private long id;

	public ReadBookCopyDto(BookCopy copy) {
		this.state = copy.getState();
		this.location = copy.getLocation();
		this.id = copy.getId();
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
