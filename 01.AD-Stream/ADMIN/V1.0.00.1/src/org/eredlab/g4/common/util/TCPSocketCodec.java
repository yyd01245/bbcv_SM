package org.eredlab.g4.common.util;

import java.util.HashMap;
import java.util.Map;

public class TCPSocketCodec extends MessageCodec{

public Map<String, Object> decoder(String message) {
		
		HashMap<String, Object> retObj = new HashMap<String, Object>();
		if (message.indexOf(CommandSupport.HEADER) >= 0) {
			if (message.lastIndexOf(CommandSupport.TAIL) > 0) {
				message = message.substring(0,
						message.lastIndexOf(CommandSupport.TAIL));
			}
			message = message.substring(CommandSupport.HEADER.length());
			String[] revArr = message.split("\\|");
			if (revArr.length > 0) {
				String template = "";
				CommandSupport.COMMAND cc = CommandSupport.COMMAND
						.valueOf(revArr[0]);
				switch (cc) {
				case PROGRAM_CTRL_RESP:
					template = Constants.RETURNCODE+ Constants.SEPERATOR
					+Constants.RETURNMESSAGE+ Constants.SEPERATOR
					+Constants.PCID+ Constants.SEPERATOR
					+Constants.PCTYPE;
					break;
				case HAM_CMD_RESP:
					template = RPMConstants.RETURNCODE+ Constants.SEPERATOR
					+RPMConstants.RETURNMSG;
					break;
				}
				this.formatObjectPipeDelimiter(template, retObj, revArr);
			}
		}
		return retObj;
	}

	@Override
	public String encoder(Map<String, Object> message) {
		String retStr = "";
		if (message.size() > 0) {
			CommandSupport.COMMAND cc = CommandSupport.COMMAND
					.valueOf((String) message.get("command"));
			String template = "";
			switch (cc) {
			
			case PROGRAM_CTRL_REQ:
				template = Constants.PCID + Constants.SEPERATOR
				+ Constants.PCTYPE+ Constants.SEPERATOR
				+Constants.OPERATORTYPE;
				break;
			case HAM_CMD_REQ:
				template = RPMConstants.APPCODE+ Constants.SEPERATOR
				+ RPMConstants.APPNAME+ Constants.SEPERATOR
				+ RPMConstants.CMDTYPE;
				break;
			
			}
			retStr = this.formatMessagePipeDelimiter(template, message);
			retStr = CommandSupport.HEADER + retStr + CommandSupport.TAIL;
		}
		return retStr;
	}
}
