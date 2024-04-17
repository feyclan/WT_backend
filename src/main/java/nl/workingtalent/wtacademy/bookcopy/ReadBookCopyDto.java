package nl.workingtalent.wtacademy.bookcopy;

public class ReadBookCopyDto {

	private String state;

	private long id;
	
	private boolean isAvailable;

	public ReadBookCopyDto(BookCopy copy) {
		this.state = copy.getState();
		this.id = copy.getId();
		this.isAvailable = copy.isAvailable();
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

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	
	

}
