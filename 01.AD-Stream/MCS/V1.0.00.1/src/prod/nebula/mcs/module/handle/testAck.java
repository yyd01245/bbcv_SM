package prod.nebula.mcs.module.handle;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import prod.nebula.mcs.core.common.CommConstants;

public class testAck {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		String ack ="<response id='102' cmd='execute_switch' ack='20001' detail='task not exist'>"
//				+"<target><task>200</task></target>"
//				+"<body/>"
//				+"</response>";
//		System.out.println(CommunicationStandard.getResponseAck(ack));
		String retString ="<response id='898' cmd='execute_switch' ack='2004' detail='same source, don't switch'><target><task>384</task></target><body></body></response>";
		if(retString!=null&&!"".equals(retString)&&retString.indexOf("<response")>=0){
			SAXReader reader = new SAXReader();
			
			try {
				Document doc = reader.read(new ByteArrayInputStream(retString.getBytes("UTF-8")));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//根据返回报文生成document
			
		}

	}

}
