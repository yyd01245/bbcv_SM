package com.bbcvision.msiAgent.module.client;

import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {
	private String authurl;
	public HttpClient(String url){
		this.authurl = url;
	}
	public HttpURLConnection getHttp(){
		try {
			URL getUrl = new URL(authurl);
			HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
				connection.setConnectTimeout(10000); //设置连接超时时间
			connection.setReadTimeout(10000);//设置读取返回超时时间
			// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在   
			// http正文内，因此需要设为true, 默认情况下是false; 
			connection.setDoOutput(true);
			// 设置是否从httpUrlConnection读入，默认情况下是true;
			connection.setDoInput(true);
			// Post 请求不能使用缓存
			connection.setUseCaches(false);
			//设定传送的内容类型
//			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "keep-alive");
			connection.setRequestProperty("Accept","*/*");
			connection.setRequestProperty("Accept-Language", "zh-CN");
			connection.setRequestProperty("Accept-Encoding", "gzip,deflate,sdcn");
		    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//		    connection.setRequestProperty("Content-type", "text/xml; charset=UTF-8");
		    return connection;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
}
