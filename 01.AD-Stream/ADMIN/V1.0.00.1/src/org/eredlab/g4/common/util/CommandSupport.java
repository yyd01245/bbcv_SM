package org.eredlab.g4.common.util;

public class CommandSupport {
	public enum COMMAND {
		PROGRAM_CTRL_REQ, 
		PROGRAM_CTRL_RESP,
		HAM_HOST_ALIVE_REQ,
		HAM_RECVLOG_RSCS,
		HAM_CMD_REQ,
		HAM_CMD_RESP,
		HAM_APPSTATUS_REQ,
		HAM_TOAPPMSG_REQ,
		HAM_RECVLOG_REQ,
		HAM_QUERYLOG_REQ,
		HAM_QUERYLOG_RESP,
		HAM_RECVRES_REQ,
		bootlog,
		operlog,
		reslog
	}

	public static String HEADER = "XXBB";
	public static String TAIL = "XXEE";
	public static String CHARSET ="GBK";
	
	public static void main(String args[]){
		System.out.println(COMMAND.HAM_QUERYLOG_RESP.toString());
	}
}