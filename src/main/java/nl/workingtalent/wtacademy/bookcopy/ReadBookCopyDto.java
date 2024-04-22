package nl.workingtalent.wtacademy.bookcopy;

public class ReadBookCopyDto {

	private State state;

	private long id;
	
	private String WTId;
	
	private boolean isAvailable;

	public ReadBookCopyDto(BookCopy copy) {
		this.state = copy.getState();
		this.id = copy.getId();
		this.WTId = copy.getWTId();		
		this.isAvailable = copy.isAvailable();
	}

	public String getWTId() {
		return WTId;
	}

	public void setWTId(String wTId) {
		WTId = wTId;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
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
