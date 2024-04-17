package nl.workingtalent.wtacademy.bookcopy;

public class ReadBookCopyDto {

	private String state;

	private long id;

	public ReadBookCopyDto(BookCopy copy) {
		this.state = copy.getState();
		this.id = copy.getId();
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
