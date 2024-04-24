package nl.workingtalent.wtacademy.loan;

import nl.workingtalent.wtacademy.bookcopy.State;

import java.time.LocalDate;

public class UpdateLoanDto {

	private long id;
	private LocalDate startDate;
	private LocalDate endDate;
	private State conditionStart;
	private State conditionEnd;
	private Boolean isActive;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public State getConditionStart() {
		return conditionStart;
	}

	public void setConditionStart(State conditionStart) {
		this.conditionStart = conditionStart;
	}

	public State getConditionEnd() {
		return conditionEnd;
	}

	public void setConditionEnd(State conditionEnd) {
		this.conditionEnd = conditionEnd;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
}
