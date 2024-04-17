package nl.workingtalent.wtacademy.bookcopy;

public class CreateBookCopyDto {
	private String state;

	private long bookId;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public long getBookId() {
		return bookId;
	}

	public void setBookId(long bookId) {
		this.bookId = bookId;
	}

}
