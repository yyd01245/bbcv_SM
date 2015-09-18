/**
 * 
 */
package com.bbcvision.msiAgent.module.client;

import java.io.InputStream;
import java.io.StringBufferInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * @author RobinLau
 *
 */
public class XMLParseUtil {

	public static String[] getDataFromXML(String xmlstr) throws Exception{
		String[] datas = new String[2];
		String retcode="-1",authToken="",indexUrl="";
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream is = new StringBufferInputStream(xmlstr);
        Document document = builder.parse(is);
        Element element = document.getDocumentElement();  
        NodeList nodes = element.getElementsByTagName("AUTHMsgWriteResp");  
        for(int i=0;i<nodes.getLength();i++){
        	Element resElement = (Element) nodes.item(i);
        	retcode = resElement.getAttribute("result");
        	authToken = resElement.getAttribute("auth-token");
        //	indexUrl = resElement.getAttribute("indexurl");
        }
		datas[0]=retcode;
		datas[1]=authToken;
	
		return datas;
	}

}
