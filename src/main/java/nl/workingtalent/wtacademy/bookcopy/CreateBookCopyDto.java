package nl.workingtalent.wtacademy.bookcopy;

import java.util.List;

public class CreateBookCopyDto {
	
	private List<State> states;

	private long bookId;

	public List<State> getStates() {
		return states;
	}

	public void setStates(List<State> states) {
		this.states = states;
	}

	public long getBookId() {
		return bookId;
	}

	public void setBookId(long bookId) {
		this.bookId = bookId;
	}

}
