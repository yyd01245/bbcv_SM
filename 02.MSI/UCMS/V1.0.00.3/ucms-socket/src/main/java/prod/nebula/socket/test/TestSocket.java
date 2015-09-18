/** 
 * Project: bbcvision3-socket
 * author : PengSong
 * File Created at 2013-11-19 
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/** 
 * TODO Comment of TestSocket 
 * 
 * @author PengSong 
 */
public class TestSocket {
	public static final String serverIp = "192.168.100.31";
	public static final int serverPort = 7577;
	public static final int timeout = 2000;
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		testCase1();
	}
	
	public static void testCase1() throws InterruptedException, ExecutionException{
		ExecutorService exec = Executors.newCachedThreadPool();
		int oneMillionTh = 10;//每毫秒线程数
		int threadCount = oneMillionTh * 100;//每秒线程总数
		List<Future<Result>> fs = new ArrayList<Future<Result>>();
		long s = System.currentTimeMillis();
		int th = 0;
		System.out.println("==>开始创建线程:"+s);
		for(int i = oneMillionTh;i <= threadCount;i+=oneMillionTh) {
			for(int j = 0 ;j< oneMillionTh;j++) {
				fs.add(exec.submit(new TestSocketThread(i - j)));
				th++;
			}
			//Thread.sleep(1);
		}
		System.out.println("==>创建线程结束,线程数："+th+",用时："+(System.currentTimeMillis() - s)+"ms");
		long total = 0,errorCount = 0,tth = 0,notEqual = 0;
		for(Future<Result> f : fs) {
			tth++;
			Result r = f.get();
			total += r.getTime();
			if(r.isError()) errorCount++;
			if(r.isNotEqual()) notEqual++;
		}
		long evTime = total / threadCount;
		System.out.println("每秒请求数："+tth+",\t socket超时时间:"+timeout+"ms,\t 总消耗时间:"+total+"ms,\t 没有收到响应总数:"+errorCount+",\t 收发报文长度不同次数："+notEqual+",\t 平均每次耗时："+evTime+"ms");
		exec.shutdownNow();
	}
	
	public static void testCase2() throws InterruptedException, ExecutionException{
		ExecutorService exec = Executors.newCachedThreadPool();
		int oneMillionTh = 10;//每毫秒线程数
		int threadCount = oneMillionTh * 100;//每秒线程总数
		List<Future<Result>> fs = new ArrayList<Future<Result>>();
		long s = System.currentTimeMillis();
		int th = 0;
		System.out.println("==>开始创建线程:"+s);
		for(int i = oneMillionTh;i <= threadCount;i+=oneMillionTh) {
			for(int j = 0 ;j< oneMillionTh;j++) {
				new Thread(new TestSocketRunnable(i - j)).start();
				th++;
			}
		}
		System.out.println("==>创建线程结束,线程数："+th+",用时："+(System.currentTimeMillis() - s)+"ms");
		long total = 0,errorCount = 0,tth = 0,notEqual = 0;
		for(Future<Result> f : fs) {
			tth++;
			Result r = f.get();
			total += r.getTime();
			if(r.isError()) errorCount++;
			if(r.isNotEqual()) notEqual++;
		}
		long evTime = total / threadCount;
		System.out.println("每秒请求数："+tth+",\t socket超时时间:"+timeout+"ms,\t 总消耗时间:"+total+"ms,\t 没有收到响应总数:"+errorCount+",\t 收发报文长度不同次数："+notEqual+",\t 平均每次耗时："+evTime+"ms");
		exec.shutdownNow();
	}
}
