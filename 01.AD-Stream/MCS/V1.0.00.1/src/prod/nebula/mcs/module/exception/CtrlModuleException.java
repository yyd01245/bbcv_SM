package prod.nebula.mcs.module.exception;

import prod.nebula.mcs.core.common.CommConstants;
import prod.nebula.mcs.core.cotorller.ControllerException;

 

public class CtrlModuleException extends ControllerException{
	public static final long serialVersionUID = 1L;
	public Throwable rootCause = null;
	public String identifier;
	public int errorCode;
	public String message;

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public Throwable getRootCause() {
		return rootCause;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public int getErrorCode() {
		return errorCode;
	}

	public CtrlModuleException() {
		
	}

	public CtrlModuleException(Throwable rootCause) {
		this.rootCause = rootCause;
		if(rootCause instanceof RuntimeException){
			this.errorCode = CommConstants.INTERNAL_ERROR;
			this.message = CommConstants.INTERNAL_ERROR_MSG;
		}
	}

	public CtrlModuleException(int errorCode,String message) {
		this.errorCode = errorCode;
		this.message = message;
		this.rootCause = this;
	}
}
