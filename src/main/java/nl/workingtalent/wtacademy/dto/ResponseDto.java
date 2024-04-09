package nl.workingtalent.wtacademy.dto;

import java.util.List;

public class ResponseDto {

	private boolean success;
	private Object data;
	private List<String> errors;
	private String validationMessage;

	// CONSTRUCTOR
	public ResponseDto(boolean success, Object data, List<String> errors, String validationMessage) {
		this.success = success;
		this.data = data;
		this.errors = errors;
		this.validationMessage = validationMessage;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public String getValidationMessage() {
		return validationMessage;
	}

	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}

}
