package org.eredlab.g4.caas.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.eredlab.g4.arm.util.idgenerator.IDHelper;
import org.eredlab.g4.bmf.base.BaseServiceImpl;
import org.eredlab.g4.caas.service.R30aService;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.ccl.properties.PropertiesFactory;
import org.eredlab.g4.ccl.properties.PropertiesFile;
import org.eredlab.g4.ccl.properties.PropertiesHelper;

public class R30aServiceImpl extends BaseServiceImpl  implements R30aService{
	
	private PropertiesHelper ph = PropertiesFactory.getPropertiesHelper(PropertiesFile.APP);
	private String baseUrl = ph.getValue("caas.r30server.url");

	public Dto deleteItems(Dto pDto) {
		// TODO Auto-generated method stub
		return null;
	}

	public Dto queryItems(Dto pDto) {
		// TODO Auto-generated method stub
		return null;
	}

	public Dto saveItem(Dto pDto) {
		Dto out = new BaseDto();
		pDto.put("id", IDHelper.getYunAppID());
		g4Dao.insert("R30a.saveR30aItem", pDto);
		String ip = pDto.getAsString("api_ip");
		String port = pDto.getAsString("api_port");
		boolean b = false;
		if(b){
			String path = "http://" + ip + ":" + port;
			String strUrl = baseUrl+"?ip="+ip+"&path="+path ;
			System.out.println(strUrl);
			URL url;
			HttpURLConnection urlcon = null;
			InputStream is = null;
			String str = "";
			StringBuffer sb = new StringBuffer(str);
			try {
				url = new java.net.URL(strUrl);
				urlcon = (HttpURLConnection) url.openConnection();
				urlcon.connect(); // 连接
				is = urlcon.getInputStream(); //得到响应
				BufferedReader buffer = new BufferedReader(new InputStreamReader(is));		
				while((str=buffer.readLine())!=null){
					sb.append(str);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					is.close();
					is=null;
				} catch (IOException e) {
					e.printStackTrace();
				}
				urlcon.disconnect();
			}
			out.put("result", sb.toString());
		}
		out.put("success", new Boolean(true));
		return out;
	}

	public Dto updateItem(Dto pDto) {
		// TODO Auto-generated method stub
		return null;
	}



}
