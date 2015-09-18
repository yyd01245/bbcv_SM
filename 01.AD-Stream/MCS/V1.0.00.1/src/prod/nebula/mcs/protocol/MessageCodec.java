package prod.nebula.mcs.protocol;

import java.util.Map;

public abstract class MessageCodec {
	public abstract Map<String,Object> decoder(String message);
	public abstract String encoder(Map<String,Object> message);
	
	protected String formatMessagePipeDelimiter(String template,Map<String,Object> message){
		StringBuffer sb = new StringBuffer();
		String[] revArr = template.split("\\|");
		if(revArr.length>0){
			sb.append(message.get("command")+"|");
			for(String key : revArr){
				sb.append(message.get(key).toString()+"|");
			}
			int len = sb.length();
			if(len >0){
				sb.deleteCharAt(len-1);
			}
		}
		return sb.toString();
	}
	protected void formatObjectPipeDelimiter(String template,Map<String,Object> message,String[] revArr){
		if(revArr.length>0){
			String[] keyArr = template.split("\\|");
			if(keyArr.length==revArr.length-1){
				message.put("command",revArr[0]);
				for(int i=0;i<keyArr.length;i++){
					//if(StringUtil.assertNotNull(keyArr[i]))
						message.put(keyArr[i], revArr[i+1]);
				}
			}
		}
	}
}
