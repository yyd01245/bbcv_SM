package prod.nebula.mcs.core.enums;

import java.util.HashMap;

import prod.nebula.mcs.core.common.Enumeration;



public class ExecResult {
	
	public static final String CODE = "return_code";
	
	public static HashMap<String, Object> getSuccess(){
		HashMap<String, Object> message = new HashMap<String, Object>();
		message.put(CODE, 0);
		return message;
	}
	
	/**
	 * 根据指定枚举信息构建Result
	 * @return
	 */
	public static HashMap<String, Object> construct(Enumeration eum) {
		HashMap<String, Object> message = new HashMap<String, Object>();		
		message.put(CODE, eum.getCode());
		return message;
	}
	
}
