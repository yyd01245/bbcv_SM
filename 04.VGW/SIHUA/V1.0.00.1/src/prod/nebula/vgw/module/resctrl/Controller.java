package prod.nebula.vgw.module.resctrl;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import net.sf.json.JSONObject;

import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

import prod.nebula.vgw.config.Command;
import prod.nebula.vgw.config.JSONConstants;
import prod.nebula.vgw.handler.RTSPClient;
import prod.nebula.vgw.handler.UDPHandler;
import prod.nebula.vgw.module.resdis.VODResDis;
import prod.nebula.vgw.service.TCPServer;
import prod.nebula.vgw.util.Commons;
import prod.nebula.vgw.util.client.IOSocketClient;

public class Controller implements Serializable, Runnable {
	private static final long serialVersionUID = 1L;
	private int port;
	public static final String END_MARK = "<EOF>";
	private Commons.Status status;
	private RTSPClient client;
	private String stbid;
	private String regionId;
	private String serialNo;
	private String cmd;
	private String doneDate;
	private String sessionId;
	private String appUserId;
	private HashMap<Integer, Commons.Status> operHis;
	private long runtime;
	private boolean runtimeThread = true;

	private String areaid;
	
	// VOD选时间播放开始时间
	private String beginTime;
	//
	private String currentTime = "";
	//
	private String totalTime = "";
	
	//播放影片名
	private String vodname="";
	//播放影片海报
	private String posterurl="";
	
	
	public String getVodname() {
		return vodname;
	}

	public void setVodname(String vodname) {
		this.vodname = vodname;
	}

	public String getPosterurl() {
		return posterurl;
	}

	public void setPosterurl(String posterurl) {
		this.posterurl = posterurl;
	}

	public String getAreaid() {
		return areaid;
	}

	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}

	public void setRuntimeThread(boolean runtimeThread) {
		this.runtimeThread = runtimeThread;
	}

	public NioDatagramAcceptor getAcceptor() {
		return acceptor;
	}

	public void setAcceptor(NioDatagramAcceptor acceptor) {
		this.acceptor = acceptor;
	}
	
	private NioDatagramAcceptor acceptor;

	public long getRuntime() {
		return runtime;
	}

	public void setRuntime(long runtime) {
		this.runtime = runtime;
	}

	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}

	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getStbid() {
		return stbid;
	}

	public void setStbid(String stbid) {
		this.stbid = stbid;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	/**
	 * @return the serialNo
	 */
	public String getSerialNo() {
		return serialNo;
	}

	/**
	 * @param serialNo
	 *            the serialNo to set
	 */
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public HashMap<Integer, Commons.Status> getOperHis() {
		return operHis;
	}

	public void setOperHis(HashMap<Integer, Commons.Status> operHis) {
		this.operHis = operHis;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Commons.Status getStatus() {
		return status;
	}

	public void setStatus(Commons.Status status) {
		this.status = status;
	}

	public RTSPClient getClient() {
		return client;
	}

	public void setClient(RTSPClient client) {
		this.client = client;
	}

	/**
	 * @return the cmd
	 */
	public String getCmd() {
		return cmd;
	}

	/**
	 * @param cmd
	 *            the cmd to set
	 */
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	/**
	 * @return the doneDate
	 */
	public String getDoneDate() {
		return doneDate;
	}

	/**
	 * @param doneDate
	 *            the doneDate to set
	 */
	public void setDoneDate(String doneDate) {
		this.doneDate = doneDate;
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId
	 *            the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the appUserId
	 */
	public String getAppUserId() {
		return appUserId;
	}

	/**
	 * @param appUserId
	 *            the appUserId to set
	 */
	public void setAppUserId(String appUserId) {
		this.appUserId = appUserId;
	}

	public Controller(int port) {
		super();
		this.port = port;
		HashMap<Integer, Commons.Status> operHis = new HashMap<Integer, Commons.Status>();
		this.setOperHis(operHis);
	}

	public void start() {
		try {
			acceptor = new NioDatagramAcceptor();// 创建一个UDP的接收器

			acceptor.setHandler(new UDPHandler(this));// 设置接收器的处理程序

			Executor threadPool = Executors.newFixedThreadPool(TCPServer
					.getConfig().getClientPoolSize());// 建立线程池
			acceptor.getFilterChain().addLast("exector",
					new ExecutorFilter(threadPool));
			acceptor.getFilterChain().addLast("logger", new LoggingFilter());

			DatagramSessionConfig dcfg = acceptor.getSessionConfig();// 建立连接的配置文件
			dcfg.setReadBufferSize(TCPServer.getConfig().getReadBufferSize());// 设置接收最大字节默认2048
			dcfg.setReceiveBufferSize(TCPServer.getConfig()
					.getReceiveBufferSize());// 设置输入缓冲区的大小
			dcfg.setSendBufferSize(TCPServer.getConfig().getSendBufferSize());// 设置输出缓冲区的大小
			dcfg.setReuseAddress(true);// 设置每一个非主监听连接的端口可以重用

			acceptor.bind(new InetSocketAddress(port));// 绑定端口
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 启动超时装置
		new Thread(this).start();
	}

	@Override
	public void run() {
		while (this.runtimeThread) {
			long time = System.currentTimeMillis() / 1000 - this.runtime;
			if (time >= TCPServer.getConfig().getVodTimeout()) {
				// 表示应用超时发送超时
				IOSocketClient client = new IOSocketClient();
				String ip = TCPServer.getConfig().getCSCGAddress();
				int port = TCPServer.getConfig().getCSCGPort();

				JSONObject sendJson = new JSONObject();
				sendJson.put(JSONConstants.CMD, Command.TIMEOUT.value());
				sendJson.put(JSONConstants.SESSIONID, sessionId);
				sendJson.put(JSONConstants.SERIALNO, serialNo);
				sendJson.put(JSONConstants.MSG, "");
				client.sendStr(ip, port, 1000, null, sendJson.toString()
						+ "XXEE", null);
				// 发送一次后就退出
				VODResDis.logout(sessionId);
				this.runtimeThread = false;
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// 不做处理
			}
		}
	}
}
