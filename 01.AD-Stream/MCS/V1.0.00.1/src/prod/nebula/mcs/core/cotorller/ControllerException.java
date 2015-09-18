package prod.nebula.mcs.core.cotorller;

import prod.nebula.mcs.core.common.CommConstants;



public class ControllerException extends Exception {
	public static final long serialVersionUID = 1L;
	public Throwable rootCause = null;
	public String identifier;
	public int errorCode;
	public String message;

	
	public String getMessage() {
		return message;
	}

	public Throwable getRootCause() {
		return rootCause;
	}

	public String getIdentifier() {
		return identifier;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public ControllerException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ControllerException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ControllerException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ControllerException(Throwable cause) {
		this.rootCause = cause;
		if(rootCause instanceof RuntimeException){
			this.errorCode = CommConstants.INTERNAL_ERROR;
			this.message = CommConstants.INTERNAL_ERROR_MSG;
		}
	}

	public ControllerException(int errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
		this.rootCause = this;
	}	
	
}
