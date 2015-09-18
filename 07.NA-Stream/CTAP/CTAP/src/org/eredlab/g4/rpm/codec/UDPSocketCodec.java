package org.eredlab.g4.rpm.codec;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.ccl.json.JsonHelper;
import org.eredlab.g4.common.util.CommandSupport;
import org.eredlab.g4.common.util.MessageCodec;
import org.eredlab.g4.rpm.common.RPMConstants;

public class UDPSocketCodec extends MessageCodec{
	
	private static Log logger = LogFactory.getLog(UDPSocketCodec.class);
	
	public Dto jsonDecoder(String message){
		Dto out = new BaseDto();
		if (message.indexOf(CommandSupport.HEADER) >= 0) {
			if (message.lastIndexOf(CommandSupport.TAIL) > 0) {
				message = message.substring(0,
						message.lastIndexOf(CommandSupport.TAIL));				
			}
			message = message.substring(CommandSupport.HEADER.length());
			
			logger.info("【RPM】:采集日志接收字符串:" + message);
			
			return JsonHelper.parseSingleJson2Dto(message);
		}
		return out;
	}
	
	@Override
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
				case HAM_HOST_ALIVE_REQ: 
					StringBuffer sb = new StringBuffer();
					sb.append(RPMConstants.HOSTIP+RPMConstants.SEPARATOR);
					sb.append(RPMConstants.HOSTPORT+RPMConstants.SEPARATOR);
					sb.append(RPMConstants.CPUINFO+RPMConstants.SEPARATOR);
					sb.append(RPMConstants.MEMINFO+RPMConstants.SEPARATOR);
					sb.append(RPMConstants.HOSTNAME+RPMConstants.SEPARATOR);
					sb.append(RPMConstants.STATUS);
					template = sb.toString();
					break;
				case HAM_APPSTATUS_REQ: 
					StringBuffer sb2 = new StringBuffer();
					sb2.append(RPMConstants.HOSTIP+RPMConstants.SEPARATOR);
					sb2.append(RPMConstants.APPNAME+RPMConstants.SEPARATOR);
					sb2.append(RPMConstants.STATUS);
					template = sb2.toString();
					break;
				case HAM_TOAPPMSG_REQ: 
					StringBuffer sb3 = new StringBuffer();
					sb3.append(RPMConstants.HOSTIP+RPMConstants.SEPARATOR);
					sb3.append(RPMConstants.APPNAME+RPMConstants.SEPARATOR);
					template = sb3.toString();
					break;
				case HAM_RECVLOG_REQ: 
					StringBuffer sb4 = new StringBuffer();
					sb4.append(RPMConstants.HOSTIP+RPMConstants.SEPARATOR);
					sb4.append(RPMConstants.APPNAME+RPMConstants.SEPARATOR);
					template = sb4.toString();
					break;
				case HAM_RECVLOG_RSCS: 
					StringBuffer sb6 = new StringBuffer();
					sb6.append(RPMConstants.APPNAME+RPMConstants.SEPARATOR);
					template = sb6.toString();
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
			CommandSupport.COMMAND cc = CommandSupport.COMMAND.valueOf((String)message
					.get("cmd"));
			String template = "";
			switch (cc) {
			case HAM_CMD_RESP: 
				template = RPMConstants.RETURNCODE+RPMConstants.SEPARATOR+RPMConstants.RETURNMSG;
				break;
			}
			retStr = this.formatMessagePipeDelimiter(template, message);
			retStr = CommandSupport.HEADER + retStr + CommandSupport.TAIL;
		}
		return retStr;
	}
}
