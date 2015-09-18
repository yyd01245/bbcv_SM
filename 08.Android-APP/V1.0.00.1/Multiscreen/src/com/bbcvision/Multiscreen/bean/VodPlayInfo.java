package com.bbcvision.Multiscreen.bean;

public class VodPlayInfo {
	private String cmd;
	private String msg;
	private String vodname;
	private String return_code;
	private String message;
	private String new_token;
	private String sequence;
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		cmd = cmd;
	}

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNew_token() {
		return new_token;
	}

	public void setNew_token(String new_token) {
		this.new_token = new_token;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}


	public String getVodname() {
		return vodname;
	}

	public void setVodname(String vodname) {
		this.vodname = vodname;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
