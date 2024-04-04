package nl.workingtalent.wtacademy.bookcopy;

public class BookCopyDto {
	
	public BookCopyDto(BookCopy copy) {
		this.condition = copy.getCondition();
		this.location = copy.getLocation();
		this.id = copy.getId();
	}
	
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

	public void setId(long id) {
		this.id = id;
	}

	private String condition;
	
	private String location;
	
	private long id;
	
	
}
