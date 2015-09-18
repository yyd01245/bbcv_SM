/** 
 * Project: bbcvision3-socket
 * author : PengSong
 * File Created at 2013-11-21 
 *
 * Copyright bbcvision Corporation Limited. 
 * All rights reserved. 
 *
 * This software is the confidential and proprietary information of 
 * bbcvision company. ("Confidential Information"). You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with bbcvision.com. 
 */
package prod.nebula.socket.test;

import java.util.UUID;
import java.util.concurrent.Callable;

import prod.nebula.socket.client.IOSocketClient;


/** 
 * TODO Comment of TestSocketThread1 
 * 
 * @author PengSong 
 */
class TestSocketThread implements Callable<Result>{
	private int index;
	public TestSocketThread(int index) {
		this.index = index;
	}
	
	public Result call() throws Exception {
		Result res = new Result();
		//System.out.println(new Date()+"==>线程已经启动:"+index);
		IOSocketClient tcpclient = new IOSocketClient(TestSocket.serverIp,TestSocket.serverPort,TestSocket.timeout);
		
		String reqStr = "{\"cmd\":\"socketTest\",\"serialno\":\""+UUID.randomUUID()+"\"}XXEE";
		long start = System.currentTimeMillis();
		String str = tcpclient.sendStr(reqStr);
		//System.out.println(new Date()+"==>线程:"+index+",已接收到socket响应:"+str);
		if("".equals(str)) {
			res.setError(true);
		} else {
			if(reqStr.getBytes().length != str.getBytes().length){
				res.setNotEqual(true);
			}
		}
		res.setTime(System.currentTimeMillis() - start);
		return res;
	}
}