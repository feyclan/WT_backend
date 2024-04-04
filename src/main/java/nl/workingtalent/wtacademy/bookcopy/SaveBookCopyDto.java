package nl.workingtalent.wtacademy.bookcopy;

public class SaveBookCopyDto {
	private String condition;
	
	private String location;
	
	private long bookId;

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

	public long getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	
	
}
