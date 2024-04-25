package nl.workingtalent.wtacademy.bookcopy;

import nl.workingtalent.wtacademy.loan.Loan;

public class ReadBookCopyDto {

	private String state;

	private long id;

	private String WTId;

	private boolean isAvailable;

	private String loanUserName;

	private long userLoanId;

	public ReadBookCopyDto(BookCopy copy) {
		this.state = copy.getState().name();
		this.id = copy.getId();
		this.WTId = copy.getWTId();
		this.isAvailable = copy.isAvailable();
		if (!copy.isAvailable()) {
			for (Loan loan : copy.getLoans()) {
				if (loan.isActive()) {
					this.loanUserName = loan.getUser().getFirstName() + " " + loan.getUser().getLastName();
					this.userLoanId = loan.getUser().getId();
				}
			}
		}
	}

	public String getWTId() {
		return WTId;
	}

	public void setWTId(String wTId) {
		WTId = wTId;
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

	public void setLoanUserName(String loanUserName) {
		this.loanUserName = loanUserName;
	}

	public String getLoanUserName() {
		return loanUserName;
	}

	public void setUserLoanId(long userLoanId) {
		this.userLoanId = userLoanId;
	}

	public long getUserLoanId() {
		return userLoanId;
	}
}
