package com.bbcvision.msiAgent.module.socket;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URLEncoder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONObject;

import com.bbcvision.msiAgent.config.Command;
import com.bbcvision.msiAgent.config.JSONConstants;
import com.bbcvision.msiAgent.module.client.HttpClient;
import com.bbcvision.msiAgent.module.client.XMLParseUtil;
import com.bbcvision.msiAgent.service.TCPServer;
import com.bbcvision.msiAgent.util.StringUtil;

/** * Socket长连接 客户端 */
public class SocketClient {
	private Log logger = LogFactory.getLog(SocketClient.class);
	private String host = TCPServer.getConfig().getWec_server_ip();
	private int port = TCPServer.getConfig().getWec_server_port();
//	private String host = "192.168.90.133";
//	private int port = 60000;
	private String maghost = TCPServer.getConfig().getMag_server_ip();
	private int magpost = TCPServer.getConfig().getMag_server_port();
//	private String maghost = "192.168.100.11";
//	private int magpost = 18080;
	private String ucmshost = TCPServer.getConfig().getUcms_server_ip();
	private int ucmspost = TCPServer.getConfig().getUcms_server_port();
	
//	private HttpClient httpConnect = null;
	private DataOutputStream  out = null;
	private String sequence =null;
	private String type ="weixin";
	private boolean flag = true;
	private boolean reconnect = true;
	
	StringUtil stringUtil = new StringUtil();
	
	class KeepLiveThread implements Runnable {
		private Socket socket;

		public KeepLiveThread(Socket socket) {
			this.socket = socket;
		}

		public void run() { 
			while(reconnect){
				try {
					PrintWriter pw = new PrintWriter(new OutputStreamWriter(
							socket.getOutputStream()));
					pw.write("{\"cmd\": \"keeplive\", \"msg\":\"socket client keeplives\"}XXEE");
					pw.flush();
					Thread.sleep(10000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/** * 数据发送线程 */
	class SendThread implements Runnable {
		private Socket socket;

		public SendThread(Socket socket) {
			this.socket = socket;
		}

		public void run() { 
			while (flag) {
				try {
					PrintWriter pw = new PrintWriter(new OutputStreamWriter(
							socket.getOutputStream()));
					pw.write("{\"cmd\": \"login\", \"area\":\""+TCPServer.getConfig().getArea()+"\",\"mainpage\":\""+TCPServer.getConfig().getMainPage()+"\",\"detailpage\":\""+TCPServer.getConfig().getDetailPage()+"\",\"authpage\":\""+TCPServer.getConfig().getAuthPage()+"\"}XXEE");
					pw.flush();
					flag=false;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * * 数据接收线程 
	 */
	class ReceiveThread implements Runnable {
		private Socket socket;

		public ReceiveThread(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			while (reconnect) {
				
				try {
					PrintWriter pw = new PrintWriter(new OutputStreamWriter(
							socket.getOutputStream()));
//					Reader reader = new InputStreamReader(
//							socket.getInputStream(),"UTF-8");
//					CharBuffer charBuffer = CharBuffer.allocate(4096);
					DataInputStream dis = new DataInputStream(socket.getInputStream());
					@SuppressWarnings("unused")
					int charIndex = -1;
					 byte[] receiveBuffer = new byte[4096];
					while ((charIndex = dis.read(receiveBuffer)) != -1) {
						
						 ByteArrayOutputStream baos = new ByteArrayOutputStream();
						 baos.write(receiveBuffer, 0, charIndex); 
						 if((new String(receiveBuffer)).trim().lastIndexOf("XXEE")>=0)
					  		  break;
					}
						String reviceString = new String(receiveBuffer,"UTF-8");
						reviceString = reviceString.replace("XXEE", "").trim();
//						reviceString = reviceString.replace("&", "\\&").trim();
						logger.info("收到微信后台发出指令："+reviceString);
						if(reviceString==null||"".equals(reviceString)){
							logger.info("微信后台可能重启了，重新发起连接。。。");
							socket.close();
							reconnect= false;
							SocketClient socketClient = new SocketClient();
							socketClient.start();
						}else{
							JSONObject json = JSONObject.fromObject(reviceString);
							sequence = json.getString("sequence");
							String cmd = StringUtil.ToBeString(json.get(JSONConstants.CMD));
							logger.info("请求cmd："+cmd);
							Command command = Command.type(cmd);
							System.out.println("command:========"+command.value());
							switch (command) {
							case REGIST:
								String url = getURL(maghost,magpost,cmd);
								HttpClient registhttpConnect = new HttpClient(url);
								HttpURLConnection registconnection = registhttpConnect.getHttp();
								registconnection.connect();
								out = new DataOutputStream(registconnection.getOutputStream());
								StringBuffer sbsend = new StringBuffer();
								sbsend.append("type=");
								sbsend.append(type);
								sbsend.append("&sequence=");
								sbsend.append(sequence);
								sbsend.append("&username=");
								sbsend.append(json.getString("username"));
								sbsend.append("&passwd=");
								sbsend.append(json.getString("passwd"));
								out.writeBytes(sbsend.toString());
								out.flush();
								out.close();
								//设置编码,否则中文乱码
								BufferedReader bfreader = new BufferedReader(new InputStreamReader(registconnection.getInputStream(),"UTF-8"));
								//读取返回信息
								String lines;
								StringBuffer sb = new StringBuffer();
								while ((lines = bfreader.readLine()) != null) {
									lines = new String(lines.getBytes(), "UTF-8");
									sb.append(lines);
								}
								bfreader.close();
								// 断开连接
								registconnection.disconnect();
								
								logger.info("返回处理报文为微信后台："+sb.toString());
								pw.write(sb.toString()+"XXEE");
								pw.flush();
								break;
							case UPDATENICKNAME:
								String nicknameurl = getURL(maghost,magpost,cmd);
								HttpClient updatehttpConnect = new HttpClient(nicknameurl);
								HttpURLConnection updateconnection = updatehttpConnect.getHttp();
								updateconnection.connect();
								out = new DataOutputStream(updateconnection.getOutputStream());
								StringBuffer nicksend = new StringBuffer();
								nicksend.append("&type=");
								nicksend.append(type);
								nicksend.append("&sequence=");
								nicksend.append(sequence);
								nicksend.append("&username=");
								nicksend.append(json.getString("username"));
								nicksend.append("&passwd=");
								nicksend.append(json.getString("passwd"));
								nicksend.append("&nickname=");
								nicksend.append(json.getString("nickname"));
								logger.info("发送指令："+nicksend.toString());
								out.writeUTF(nicksend.toString());
								out.flush();
								out.close();
								//设置编码,否则中文乱码
								BufferedReader nickreader = new BufferedReader(new InputStreamReader(updateconnection.getInputStream(),"UTF-8"));
								//读取返回信息
								String nicklines;
								StringBuffer nicksb = new StringBuffer();
								while ((nicklines = nickreader.readLine()) != null) {
									nicklines = new String(nicklines.getBytes(), "UTF-8");
									nicksb.append(nicklines);
								}
								nickreader.close();
								// 断开连接
								updateconnection.disconnect();
								logger.info("返回处理报文为微信后台："+nicksb.toString());
								pw.write(nicksb.toString()+"XXEE");
								pw.flush();
								break;
							case ACCESS:
								String accessurl = getURL(maghost,magpost,cmd);
								HttpClient accesshttpConnect = new HttpClient(accessurl);
								HttpURLConnection accessconnection = accesshttpConnect.getHttp();
								accessconnection.connect();
								out = new DataOutputStream(accessconnection.getOutputStream());
								StringBuffer accesssend = new StringBuffer();
								accesssend.append("type=");
								accesssend.append(type);
								accesssend.append("&sequence=");
								accesssend.append(sequence);
								accesssend.append("&username=");
								accesssend.append(json.getString("username"));
								accesssend.append("&passwd=");
								accesssend.append(json.getString("passwd"));
								accesssend.append("&version=");
								accesssend.append(json.getString("version"));
								accesssend.append("&appname=");
								accesssend.append(json.getString("appname"));
								accesssend.append("&licence=");
								accesssend.append(json.getString("licence"));
								out.writeBytes(accesssend.toString());
								out.flush();
								out.close();
								//设置编码,否则中文乱码
								BufferedReader accessreader = new BufferedReader(new InputStreamReader(accessconnection.getInputStream(),"UTF-8"));
								//读取返回信息
								String accesslines;
								StringBuffer accesssb = new StringBuffer();
								while ((accesslines = accessreader.readLine()) != null) {
									accesslines = new String(accesslines.getBytes(), "UTF-8");
									logger.info("用户接入：=============="+accesslines);
									accesssb.append(accesslines);
								}
								accessreader.close();
								// 断开连接
								accessconnection.disconnect();
								System.out.println("后台返回数据："+accesssb.toString());
								JSONObject accessJson = JSONObject.fromObject(accesssb.toString());
								String server_accr = accessJson.getString("service_url"); 
								String[] server_accrs = server_accr.split(":");
								TCPServer.getConfig().setUcms_server_ip(server_accrs[0]);
								TCPServer.getConfig().setUcms_server_port(Integer.valueOf(server_accrs[1]));
								ucmshost = server_accrs[0];
								ucmspost = Integer.valueOf(server_accrs[1]);
								logger.info("返回处理报文为微信后台："+accesssb.toString());
								pw.write(accesssb.toString()+"XXEE");
								pw.flush();
								break;
							case LOGIN:
								String loginurl = getURL(ucmshost,ucmspost,cmd);
								HttpClient loginhttpConnect = new HttpClient(loginurl);
								HttpURLConnection loginconnection = loginhttpConnect.getHttp();
								loginconnection.connect();
								out = new DataOutputStream(loginconnection.getOutputStream());
								StringBuffer loginsend = new StringBuffer();
								loginsend.append("type=");
								loginsend.append(type);
								loginsend.append("&sequence=");
								loginsend.append(sequence);
								loginsend.append("&username=");
								loginsend.append(json.getString("username"));
								loginsend.append("&token=");
								loginsend.append(json.getString("token"));
								out.writeBytes(loginsend.toString());
								out.flush();
								out.close();
								//设置编码,否则中文乱码
								BufferedReader loginreader = new BufferedReader(new InputStreamReader(loginconnection.getInputStream(),"UTF-8"));
								//读取返回信息
								String loginlines;
								StringBuffer loginsb = new StringBuffer();
								while ((loginlines = loginreader.readLine()) != null) {
									loginlines = new String(loginlines.getBytes(), "UTF-8");
									loginsb.append(loginlines);
								}
								loginreader.close();
								// 断开连接
								loginconnection.disconnect();
								logger.info("返回处理报文为微信后台："+loginsb.toString());
								pw.write(loginsb.toString()+"XXEE");
								pw.flush();
								break;
							case USERBIND:
								String bindurl = getURL(ucmshost,ucmspost,cmd);
								HttpClient bindhttpConnect = new HttpClient(bindurl);
								HttpURLConnection bindconnection = bindhttpConnect.getHttp();
								bindconnection.connect();
								out = new DataOutputStream(bindconnection.getOutputStream());
								StringBuffer bindsend = new StringBuffer();
								bindsend.append("type=");
								bindsend.append(type);
								bindsend.append("&sequence=");
								bindsend.append(sequence);
								bindsend.append("&username=");
								bindsend.append(json.getString("username"));
								bindsend.append("&token=");
								bindsend.append(json.getString("token"));
								bindsend.append("&vod_page=");
								bindsend.append(json.getString("vod_page"));
								bindsend.append("&stream_id=");
								bindsend.append(json.getString("stream_id"));
								out.writeBytes(bindsend.toString());
								out.flush();
								out.close();
								//设置编码,否则中文乱码
								BufferedReader bindreader = new BufferedReader(new InputStreamReader(bindconnection.getInputStream(),"UTF-8"));
								//读取返回信息
								String bindlines;
								StringBuffer bindsb = new StringBuffer();
								while ((bindlines = bindreader.readLine()) != null) {
									bindlines = new String(bindlines.getBytes(), "UTF-8");
									bindsb.append(bindlines);
								}
								bindreader.close();
								// 断开连接
								bindconnection.disconnect();
								logger.info("返回处理报文为微信后台："+bindsb.toString());
								pw.write(bindsb.toString()+"XXEE");
								pw.flush();
								break;
							case SESSIONQUERY:
								String sessionqueryurl = getURL(ucmshost,ucmspost,cmd);
								HttpClient sessionqueryhttpConnect = new HttpClient(sessionqueryurl);
								HttpURLConnection queryconnection = sessionqueryhttpConnect.getHttp();
								queryconnection.connect();
								out = new DataOutputStream(queryconnection.getOutputStream());
								StringBuffer sessionquerysend = new StringBuffer();
								sessionquerysend.append("type=");
								sessionquerysend.append(type);
								sessionquerysend.append("&sequence=");
								sessionquerysend.append(sequence);
								sessionquerysend.append("&username=");
								sessionquerysend.append(json.getString("username"));
								sessionquerysend.append("&token=");
								sessionquerysend.append(json.getString("token"));
								out.writeBytes(sessionquerysend.toString());
								out.flush();
								out.close();
								//设置编码,否则中文乱码
								BufferedReader sessionqueryreader = new BufferedReader(new InputStreamReader(queryconnection.getInputStream(),"UTF-8"));
								//读取返回信息
								String sessionquerylines;
								StringBuffer sessionquerysb = new StringBuffer();
								while ((sessionquerylines = sessionqueryreader.readLine()) != null) {
									sessionquerylines = new String(sessionquerylines.getBytes(), "UTF-8");
									sessionquerysb.append(sessionquerylines);
								}
								sessionqueryreader.close();
								// 断开连接
								queryconnection.disconnect();
								logger.info("返回处理报文为微信后台："+sessionquerysb.toString());
								pw.write(sessionquerysb.toString()+"XXEE");
								pw.flush();
								break;
							case VODPLAY:
								String vodplayurl = getURL(ucmshost,ucmspost,cmd);
								HttpClient vodplayhttpConnect = new HttpClient(vodplayurl);
								HttpURLConnection playconnection = vodplayhttpConnect.getHttp();
								playconnection.connect();
								out = new DataOutputStream(playconnection.getOutputStream());
								StringBuffer vodplaysend = new StringBuffer();
								vodplaysend.append("&type=");
								vodplaysend.append(type);
								vodplaysend.append("&sequence=");
								vodplaysend.append(sequence);
								vodplaysend.append("&username=");
								vodplaysend.append(json.getString("username"));
								vodplaysend.append("&token=");
								vodplaysend.append(json.getString("token"));
								vodplaysend.append("&url=");
								String playurl = json.getString("url");
								playurl = playurl.replace("&", "$");
//								vodplaysend.append(json.getString("url"));
								vodplaysend.append(playurl);
								vodplaysend.append("&vodname=");
								vodplaysend.append(json.getString("vodname"));
								vodplaysend.append("&posterurl=");
								vodplaysend.append(json.getString("posterurl"));
								String vodsend = vodplaysend.toString();
								logger.info("发送报文：==============="+vodsend);
								out.writeUTF(vodsend);
//								out.writeBytes(vodsend);
								out.flush();
								out.close();
								//设置编码,否则中文乱码
								BufferedReader vodplayreader = new BufferedReader(new InputStreamReader(playconnection.getInputStream(),"UTF-8"));
								//读取返回信息
								String vodplaylines;
								StringBuffer vodplaysb = new StringBuffer();
								while ((vodplaylines = vodplayreader.readLine()) != null) {
									vodplaylines = new String(vodplaylines.getBytes(), "UTF-8");
									vodplaysb.append(vodplaylines);
								}
								vodplayreader.close();
								// 断开连接
								playconnection.disconnect();
								logger.info("返回处理报文为微信后台："+new String(vodplaysb.toString().getBytes(), "UTF-8"));
								pw.write(vodplaysb.toString()+"XXEE");
								pw.flush();
								break;
							case CHOOSERTIME:
								String choosetimeurl = getURL(ucmshost,ucmspost,cmd);
								HttpClient choosethttpConnect = new HttpClient(choosetimeurl);
								HttpURLConnection chooseconnection = choosethttpConnect.getHttp();
								chooseconnection.connect();
								out = new DataOutputStream(chooseconnection.getOutputStream());
								StringBuffer choosetimesend = new StringBuffer();
								choosetimesend.append("type=");
								choosetimesend.append(type);
								choosetimesend.append("&sequence=");
								choosetimesend.append(sequence);
								choosetimesend.append("&username=");
								choosetimesend.append(json.getString("username"));
								choosetimesend.append("&token=");
								choosetimesend.append(json.getString("token"));
								choosetimesend.append("&stream_id=");
								choosetimesend.append(json.getString("stream_id"));
								choosetimesend.append("&begintime=");
								choosetimesend.append(json.getString("begintime"));
								out.writeBytes(choosetimesend.toString());
								out.flush();
								out.close();
								//设置编码,否则中文乱码
								BufferedReader choosetimereader = new BufferedReader(new InputStreamReader(chooseconnection.getInputStream(),"UTF-8"));
								//读取返回信息
								String choosetimelines;
								StringBuffer choosetimesb = new StringBuffer();
								while ((choosetimelines = choosetimereader.readLine()) != null) {
									choosetimelines = new String(choosetimelines.getBytes(), "UTF-8");
									choosetimesb.append(choosetimelines);
								}
								choosetimereader.close();
								// 断开连接
								chooseconnection.disconnect();
								logger.info("返回处理报文为微信后台："+choosetimesb.toString());
								pw.write(choosetimesb.toString()+"XXEE");
								pw.flush();
								break;
							case KEYSEND:
								String keysendurl = getURL(ucmshost,ucmspost,cmd);
								HttpClient keysenthttpConnect = new HttpClient(keysendurl);
								HttpURLConnection keysendconnection = keysenthttpConnect.getHttp();
								keysendconnection.connect();
								out = new DataOutputStream(keysendconnection.getOutputStream());
								StringBuffer keysendsend = new StringBuffer();
								keysendsend.append("type=");
								keysendsend.append(type);
								keysendsend.append("&sequence=");
								keysendsend.append(sequence);
								keysendsend.append("&username=");
								keysendsend.append(json.getString("username"));
								keysendsend.append("&token=");
								keysendsend.append(json.getString("token"));
								keysendsend.append("&key_type=");
								keysendsend.append(json.getString("key_type"));
								keysendsend.append("&key_value=");
								keysendsend.append(json.getString("key_value"));
								out.writeBytes(keysendsend.toString());
								out.flush();
								out.close();
								//设置编码,否则中文乱码
								BufferedReader keysendreader = new BufferedReader(new InputStreamReader(keysendconnection.getInputStream(),"UTF-8"));
								//读取返回信息
								String keysendlines;
								StringBuffer keysendsb = new StringBuffer();
								while ((keysendlines = keysendreader.readLine()) != null) {
									keysendlines = new String(keysendlines.getBytes(), "UTF-8");
									keysendsb.append(keysendlines);
								}
								keysendreader.close();
								// 断开连接
								keysendconnection.disconnect();
								logger.info("返回处理报文为微信后台："+keysendsb.toString());
								pw.write(keysendsb.toString()+"XXEE");
								pw.flush();
								break;
							case USERUNBIND:
								String unbindurl = getURL(ucmshost,ucmspost,cmd);
								HttpClient unbindhttpConnect = new HttpClient(unbindurl);
								HttpURLConnection unbindconnection = unbindhttpConnect.getHttp();
								unbindconnection.connect();
								out = new DataOutputStream(unbindconnection.getOutputStream());
								StringBuffer unbindsend = new StringBuffer();
								unbindsend.append("type=");
								unbindsend.append(type);
								unbindsend.append("&sequence=");
								unbindsend.append(sequence);
								unbindsend.append("&username=");
								unbindsend.append(json.getString("username"));
								unbindsend.append("&token=");
								unbindsend.append(json.getString("token"));
								out.writeBytes(unbindsend.toString());
								out.flush();
								out.close();
								//设置编码,否则中文乱码
								BufferedReader unbindreader = new BufferedReader(new InputStreamReader(unbindconnection.getInputStream(),"UTF-8"));
								//读取返回信息
								String unbindlines;
								StringBuffer unbindsb = new StringBuffer();
								while ((unbindlines = unbindreader.readLine()) != null) {
									unbindlines = new String(unbindlines.getBytes(), "UTF-8");
									unbindsb.append(unbindlines);
								}
								unbindreader.close();
								// 断开连接
								unbindconnection.disconnect();
								logger.info("返回处理报文为微信后台："+unbindsb.toString());
								pw.write(unbindsb.toString()+"XXEE");
								pw.flush();
								break;
							default:
								logger.info("没有cmd");
							}
						}
				} catch (Exception e) {
					e.printStackTrace();
					try {
						PrintWriter pw = new PrintWriter(new OutputStreamWriter(
								socket.getOutputStream()));
						pw.write("{\"return_code\":\"-1\""+",\"sequence\":\""+sequence+"\"}XXEE");
						pw.flush();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
				} 
			}
		}
	}

	public void start() {
		try {
			// 创建Socket
			Socket socket = new Socket(host, port);
			// 启动读线程
			new Thread(new SendThread(socket)).start();
			// 启动收线程
			new Thread(new ReceiveThread(socket)).start();
			// 启动心跳保持线程
			Thread.sleep(5000);
			new Thread(new KeepLiveThread(socket)).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getURL(String ip,int port,String cmd){
		String url = "http://"+ip+":"+port+"/msi/"+cmd+".do";
		logger.info("http请求后台地址："+url);
		return url;
	}
}
