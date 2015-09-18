package prod.nebula.vgw4sida.exception;

/**
 * VOD资源调度异常
 * 
 * @author 严东军
 * 
 */
public class VODException extends Exception {
	private static final long serialVersionUID = 7586448072944978847L;
	public int errorCode;
	public String message;
	
	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public VODException() {
		super();
	}

	public VODException(String message, Throwable cause) {
		super(message, cause);
	}

	public VODException(String message) {
		super(message);
	}

	public VODException(Throwable cause) {
		super(cause);
	}

	public VODException(int errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}

}
