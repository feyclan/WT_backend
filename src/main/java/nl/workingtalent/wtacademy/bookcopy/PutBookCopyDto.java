package nl.workingtalent.wtacademy.bookcopy;

public class PutBookCopyDto {
	private String condition;
	
	private String location;
	
	private long bookCopyId;

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

	public long getBookCopyId() {
		return bookCopyId;
	}

	public void setBookCopyId(int bookId) {
		this.bookCopyId = bookId;
	}
	
	
}
