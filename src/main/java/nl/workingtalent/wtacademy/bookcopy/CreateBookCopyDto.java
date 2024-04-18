package nl.workingtalent.wtacademy.bookcopy;

import java.util.List;

public class CreateBookCopyDto {
	
	private List<String> states;

	private long bookId;

	public List<String> getStates() {
		return states;
	}

	public void setStates(List<String> states) {
		this.states = states;
	}

	public long getBookId() {
		return bookId;
	}

	public void setBookId(long bookId) {
		this.bookId = bookId;
	}

}
