package nl.workingtalent.wtacademy.bookcopy;

public class PutBookCopyDto {
	private String condition;
	
	private String location;
	
	private long id;

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
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

	public void setId(int bookId) {
		this.id = bookId;
	}
	
	
}
