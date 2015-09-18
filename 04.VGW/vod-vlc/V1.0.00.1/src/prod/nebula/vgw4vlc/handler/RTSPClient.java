package prod.nebula.vgw4vlc.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.vgw4vlc.config.TelnetClientServer;
import prod.nebula.vgw4vlc.config.VODConst;
import prod.nebula.vgw4vlc.exception.IEvent;
import prod.nebula.vgw4vlc.module.resctrl.Controller;
import prod.nebula.vgw4vlc.module.resctrl.VODResCtrl;
import prod.nebula.vgw4vlc.service.TCPServer;
import prod.nebula.vgw4vlc.util.Commons;
import prod.nebula.vgw4vlc.util.client.IOSocketClient;

public class RTSPClient extends Thread  {

	public final Logger logger = LoggerFactory.getLogger(RTSPClient.class);

	private static final String VERSION = " RTSP/1.0\r\n";
	private static final String RTSP_OK = "200 OK";// RTSP/1.0
	private static final String RTSP_MOVED = "RTSP/1.0 302 Moved Temporarily";
	private static final String RTSP_CLOSE = "SET_PARAMETER";
	private static final String CLOSE_REASON_END = "x-Reason: \"END\"";
	private static final TelnetClientServer telnet = TCPServer.getTelnet();


	/** * 连接通道 */
	public SocketChannel socketChannel;

	/** 发送缓冲区 */
	private final ByteBuffer sendBuf;

	/** 接收缓冲区 */
	private final ByteBuffer receiveBuf;

	private static final int BUFFER_SIZE = 8192;

	/** 端口选择器 */
	private Selector selector;


	public Status sysStatus;

	public String sessionid;

	/** 线程是否结束的标志 */
	public AtomicBoolean shutdown;

	public int seq = 1;

	public boolean isSended;

	public String trackInfo;

	public String frequencyInfo;

	public String pidInfo;

	public String messageInfo = "";

//	public boolean playFlag = false;

	private Controller ctrl;

	private int scale = 1;
	
	private String totalTimeStr = "";
	
	private String beginTimeStr = "";
	private String endTimeStr ="";
	
	private String input;
	private String output;
	
	private int id;

	private enum Status {
		init, options, describe, setup, play, pause, scale, teardown, move
	}

	/**
	 * 终端标识
	 */
	public String stbid;

	/**
	 * 区域标识
	 */
	public String regionId;

	/**
	 * 流水号
	 */
	public String serialNo;

	public String cmd;

	public String doneDate;

	public String spId;

	/**
	 * 会话信息
	 */
	public IoSession session;
	
	public Controller sender;
	
	public String listenIp;
	public int listenPort;

	public String getSpId() {
		return spId;
	}

	public void setSpId(String spId) {
		this.spId = spId;
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
	 * @return the listenIp
	 */
	public String getListenIp() {
		return listenIp;
	}

	/**
	 * @param listenIp
	 *            the listenIp to set
	 */
	public void setListenIp(String listenIp) {
		this.listenIp = listenIp;
	}

	/**
	 * @return the listenPort
	 */
	public int getListenPort() {
		return listenPort;
	}

	/**
	 * @param listenPort
	 *            the listenPort to set
	 */
	public void setListenPort(int listenPort) {
		this.listenPort = listenPort;
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

	/**
	 * @return the scale
	 */
	public int getScale() {
		return scale;
	}

	/**
	 * @param scale
	 *            the scale to set
	 */
	public void setScale(int scale) {
		this.scale = scale;
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

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	
	

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}
	
	

	public Controller getSender() {
		return sender;
	}

	public void setSender(Controller sender) {
		this.sender = sender;
	}

	public RTSPClient(Controller sender,
			IoSession session) {
		this.session = session;
		this.sender = sender;

		// 初始化缓冲区
		sendBuf = ByteBuffer.allocateDirect(BUFFER_SIZE);
		receiveBuf = ByteBuffer.allocateDirect(BUFFER_SIZE);
		if (selector == null) {
			// 创建新的Selector
			try {
				selector = Selector.open();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		startup();
		sysStatus = Status.init;
		shutdown = new AtomicBoolean(false);
		isSended = false;
		ctrl = sender;
	}

	public void startup() {
		id = sender.getId();			//VLC唯一标识
		input = sender.getInput();	//输入参数
		output = sender.getOutput();	//输出参数
		telnet.sendCommand("new "+id+" broadcast enabled");
	}




	public boolean isConnected() {
		return socketChannel != null && socketChannel.isConnected();
	}

	private void select() {
		int n = 0;
		try {
			if (selector == null) {
				return;
			}
			n = selector.select(60000);

		} catch (final Exception e) {
			logger.error("选择端口失败", e);
		}

		// 如果select返回大于0，处理事件
		if (n > 0) {
			for (final Iterator<SelectionKey> i = selector.selectedKeys()
					.iterator(); i.hasNext();) {
				// 得到下一个Key
				final SelectionKey sk = i.next();
				i.remove();
				// 检查其是否还有效
				if (!sk.isValid()) {
					continue;
				}

				// 处理事件
				final IEvent handler = (IEvent) sk.attachment();
				try {
					if (sk.isConnectable()) {
						handler.connect(sk);
					} else if (sk.isReadable()) {
						handler.read(sk);
					} else {
						// System.err.println("Ooops");
					}
				} catch (final Exception e) {
					handler.error(e);
					sk.cancel();
				}
			}
		}
	}


	//发送报文到CDN
	@Override
	public void run() {
		// 启动主循环流程
		while (!shutdown.get()) {
			try {
				if ((!isSended)) {
					switch (sysStatus) {
					case init:
						doOption();
						break;
					case options:
						doSetup();
						break;
					case setup:
						doPlay();
						break;
					case play:
//						playFlag = true;
						isSended =true;
						break;
					case scale:
						break;
					case pause:
						break;
					default:
						break;
					}
				}
				// do select
//				select();
//				try {
//					if (playFlag) {
//						logger.info("playFlag:====="+playFlag);
//						//定时检查
//						Thread.sleep(TCPServer.getConfig().getThreadTime()*1000);
//						doOption1();
//					} else {
//						Thread.sleep(100);
//					}
//				} catch (final Exception e) {
//				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}
	

	public void error(Exception e) {
		e.printStackTrace();
	}


	
	public void doTeardown() {
		telnet.sendCommand("del "+id);
//		playFlag = false;
		shutdown.set(true);
//		IOSocketClient client = new IOSocketClient();
//		String ip = TCPServer.getConfig().getCSCGAddress();
//		int port = TCPServer.getConfig().getCSCGPort();
//		client.sendStr(
//				ip,
//				port,
//				1000,
//				null,
//				VODResCtrl.getInstance().getGoBackResp("goback", doneDate,
//						serialNo, stbid, spId, messageInfo,ctrl.getStbid()), null);
	}

	public void doPlay() {
		if (ctrl.getOperHis() != null)
			ctrl.getOperHis().put(seq - 1, Commons.Status.play);
		telnet.sendCommand("control "+id+" play");
		this.sysStatus = Status.play;
		ctrl.setStatus(Commons.Status.play);
		VODResCtrl.getInstance().returnMessage(
				session,
				VODResCtrl.getInstance().getResp("login",
						doneDate, serialNo, listenIp,
						sessionid, listenPort,
						"", VODConst.VOD_OK, ctrl.getTotalTime()));
	}
	
	public void doResume() {
//		StringBuilder sb = new StringBuilder();
//		sb.append("PLAY ");
//		sb.append(VERSION);
//		sb.append("Session: ");
//		sb.append(sessionid);
//		sb.append("CSeq: ");
//		sb.append(seq++);
//		// sb.append("Scale:");
//		// sb.append("1");
//		sb.append("\r\n");
//		sb.append("\r\n");
//		if (ctrl.getOperHis() != null)
//			ctrl.getOperHis().put(seq - 1, Commons.Status.resume);
//		logger.debug("【VOD网关】doResume：" + sb.toString());
		if (ctrl.getOperHis() != null)
			ctrl.getOperHis().put(seq - 1, Commons.Status.play);
		telnet.sendCommand("control "+id+" play");
		this.sysStatus = Status.play;
		ctrl.setStatus(Commons.Status.play);
	}
	
	//选时间播放
	public void doChooseTime(String currtime) {
		if (ctrl.getOperHis() != null)
			ctrl.getOperHis().put(seq - 1, Commons.Status.play);
		String play = telnet.sendCommand("control "+id+" seek "+currtime);
		ctrl.setStatus(Commons.Status.play);
	}
	
	public void doSetup() {
		if (ctrl.getOperHis() != null)
			ctrl.getOperHis().put(seq - 1, Commons.Status.setup);
		telnet.sendCommand("setup "+id+" input "+input);
		telnet.sendCommand("setup "+id+" output "+output);
		this.sysStatus = Status.setup;
		ctrl.setStatus(Commons.Status.setup);
//		try {
//			Thread.sleep(500);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}

	public void doOption() {
		if (ctrl.getOperHis() != null)
			ctrl.getOperHis().put(seq - 1, Commons.Status.options);
		telnet.sendCommand("show "+id);
		this.sysStatus = Status.options;
		ctrl.setStatus(Commons.Status.options);
//		try {
//			Thread.sleep(500);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}
	
	public String doOption1() {
		if (ctrl.getOperHis() != null)
			ctrl.getOperHis().put(seq - 1, Commons.Status.options);
		String show = telnet.sendCommand("show "+id);
		if((!show.contains("state : playing")&&!show.contains("state : paused")&&!"".equals(show)&&show!=null)||"".equals(show)||show==null){
			VODResCtrl.getInstance().backToApp();
		}else{
//			String rtime1 = getValueByKey(show, "position : ").trim();
			String rtime1 = show.substring(show.indexOf("position :")+10, show.indexOf("time")).trim();
			if(rtime1!=null&&!"".equals(rtime1)){
				double seek = new Double(rtime1);
				double totalTime = new Double(ctrl.getTotalTime());
				double currentTime = (totalTime*seek);
				ctrl.setCurrentTime(String.valueOf(currentTime));
			}
		}
		return show;
	}
	
	
	public String doOption2() {
		if (ctrl.getOperHis() != null)
			ctrl.getOperHis().put(seq - 1, Commons.Status.options);
		String show = telnet.sendCommand("show "+id);
		if((!show.contains("state : playing")&&!show.contains("state : paused"))||"".equals(show)||show==null){
//			VODResCtrl.getInstance().backToApp();
		}else{
//			String rtime1 = getValueByKey(show, "position : ").trim();
			String rtime1 = show.substring(show.indexOf("position :")+10, show.indexOf("time")).trim();
			double seek = new Double(rtime1);
			double totalTime = new Double(ctrl.getTotalTime());
			double currentTime = (totalTime*seek);
			ctrl.setCurrentTime(String.valueOf(currentTime));
		}
		return show;
	}

	public void doPause() {
		if (ctrl.getOperHis() != null)
			ctrl.getOperHis().put(seq - 1, Commons.Status.pause);
		telnet.sendCommand("control "+id+" pause");
		ctrl.setStatus(Commons.Status.pause);
	}

	//快进、快退
	public void doScale() {
		StringBuilder sb = new StringBuilder();
		sb.append("PLAY ");
		sb.append(VERSION);
		sb.append("CSeq:");
		sb.append(seq++);
		sb.append("\r\n");
		sb.append("Session:");
		sb.append(sessionid);
		sb.append("Scale:");
		sb.append(scale);
		sb.append("\r\n");
		sb.append("User-Agent:Cloud VOD Player");
		sb.append("\r\n");
		sb.append("\r\n");
		if (ctrl.getOperHis() != null)
			ctrl.getOperHis().put(seq - 1, Commons.Status.scale);
		logger.debug("【VOD网关】doScale：\r\n" + sb.toString());
	}
	
	
	private String getValueByKey(String input,String key){
		String ret = "";
		if(input.indexOf(key)!=-1){
			String temp = input.substring(input.indexOf(key) + key.length());
			ret = temp.substring(0, temp.indexOf("\r\n"));
		}
		return ret;
	}
}
