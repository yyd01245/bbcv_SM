package org.eredlab.g4.rif.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eredlab.g4.arm.service.MonitorService;
import org.eredlab.g4.arm.util.ArmConstants;
import org.eredlab.g4.bmf.base.IDao;
import org.eredlab.g4.bmf.base.IReader;
import org.eredlab.g4.bmf.util.SpringBeanLoader;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.ccl.json.JsonHelper;
import org.eredlab.g4.ccl.properties.PropertiesFactory;
import org.eredlab.g4.ccl.properties.PropertiesFile;
import org.eredlab.g4.ccl.properties.PropertiesHelper;
import org.eredlab.g4.ccl.util.G4Constants;
import org.eredlab.g4.ccl.util.G4Utils;
import org.eredlab.g4.common.util.CacheTempData;
import org.eredlab.g4.common.util.CommandSupport;
import org.eredlab.g4.common.util.MinaTextLineCodecFactory;
import org.eredlab.g4.common.util.SerialnoUtil;
import org.eredlab.g4.common.util.client.IOSocketClient;
import org.springframework.context.ApplicationContext;

import prod.nebula.eabg.service.Server;

/**
 * 系统启动监听器
 * 
 * @author XiongChun
 * @since 2010-04-13
 */
public class SystemInitListener implements ServletContextListener {
	private static Log log = LogFactory.getLog(SystemInitListener.class);
	private boolean success = true;
	private ApplicationContext wac = null;
	
	private PropertiesHelper ph = PropertiesFactory.getPropertiesHelper(PropertiesFile.APP);
	
	
	
//	private String urm_ip = ph.getValue("urm.redisserver.ip");
//	private int urm_port = Integer.parseInt(ph.getValue("urm.redisserver.port"));
//	private String urm_redis_passwd = ph.getValue("urm.redisserver.password");
//	private String cums_ip = ph.getValue("cums.redisserver.ip");
//	private int cums_port = Integer.parseInt(ph.getValue("cums.redisserver.port"));
//	private String cums_redis_passwd = ph.getValue("cums.redisserver.password");
//	private String cscs_ip = ph.getValue("cscs.redisserver.ip");
//	private int cscs_port = Integer.parseInt(ph.getValue("cscs.redisserver.port"));
//	private String cscs_redis_passwd = ph.getValue("cscs.redisserver.password");
//	private MdbDataImpl mdb = (MdbDataImpl) SpringBeanLoader.getSpringBean("urmmdbImpl");
	
	//要加载到缓存的URM键值
	private final String URMIDS = "NETWORKID,RFCODEID,URMIPQAMID,CODEID,";

	public void contextDestroyed(ServletContextEvent sce) {

	}

	public void contextInitialized(ServletContextEvent sce) {
		systemStartup(sce.getServletContext());
	}

	/**
	 * 应用平台启动
	 */
	private void systemStartup(ServletContext servletContext) {
		PropertiesHelper pHelper = PropertiesFactory.getPropertiesHelper(PropertiesFile.G4);
		String forceLoad = pHelper.getValue("forceLoad", ArmConstants.FORCELOAD_N);
		long start = System.currentTimeMillis();
		if (forceLoad.equalsIgnoreCase(ArmConstants.FORCELOAD_N)) {
			log.info("********************************************");
			log.info("G4系统集成与应用开发平台[G4Studio]开始启动...");
			log.info("********************************************");
		}
		try {
			wac = SpringBeanLoader.getApplicationContext();
		} catch (Exception e) {
			success = false;
			e.printStackTrace();
		}
		//检验redis配置是否正确
//		if(success){
//			RedisUseable usable = new RedisUseable() ;
//			Boolean urmredisable = usable.jedisUseable(urm_ip, urm_port,urm_redis_passwd);
//			if(!urmredisable){
//				log.info("【错误】：URM的缓存密码配置错误！") ;
//				success = false;
//			}
//			Boolean cscsredisable = usable.jedisUseable(cscs_ip, cscs_port,cscs_redis_passwd);
//			if(!cscsredisable){
//				log.info("【错误】：CSCS的缓存密码配置错误！") ;
//				success = false;
//			}
//			Boolean cumsredisable = usable.jedisUseable(cums_ip, cums_port,cums_redis_passwd);
//			if(!cumsredisable){
//				log.info("【错误】：CUMS的缓存密码配置错误！") ;
//				success = false;
//			}
//		}
		if (success) {
			MonitorService monitorService = (MonitorService) SpringBeanLoader.getSpringBean("monitorService");
			monitorService.deleteHttpSession(new BaseDto());
			try {
				initDbType();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if (success) {
			log.info("**************ADMIN版本号****************"+pHelper.getValue("admin.self.servion"));
			log.info("系统开始启动字典装载程序...");
			log.info("开始加载字典...");
			IReader g4Reader = (IReader) SpringBeanLoader.getSpringBean("g4Reader");
			List codeList = null;
			try {
				codeList = g4Reader.queryForList("Resource.getCodeViewList");
				log.info("字典加载成功!");
			} catch (Exception e) {
				success = false;
				log.error("字典加载失败!");
				e.printStackTrace();
			}
			servletContext.setAttribute("EACODELIST", codeList);
		}
		if (success) {
			log.info("系统开始启动全局参数表装载程序...");
			log.info("开始加载全局参数表...");
			List paramList = null;
			try {
				IReader g4Reader = (IReader) SpringBeanLoader.getSpringBean("g4Reader");
				paramList = g4Reader.queryForList("Resource.getParamList");
				log.info("全局参数表加载成功!");
			} catch (Exception e) {
				success = false;
				log.error("全局参数表加载失败!");
				e.printStackTrace();
			}
			servletContext.setAttribute("EAPARAMLIST", paramList);
		}
		if(success){
			PropertiesHelper ph = PropertiesFactory.getPropertiesHelper(PropertiesFile.APP);
			String authname = ph.getValue("crsm.auth.authname");
			String authcode = ph.getValue("crsm.auth.authcode");
			String ip = ph.getValue("crsm.tcpserver.ip");
			int port = Integer.valueOf(ph.getValue("crsm.tcpserver.port"));
			SerialnoUtil serialnoUtil = new SerialnoUtil();
			String serialno = serialnoUtil.getRandomString(8);
			Dto queryDto = new BaseDto();
			queryDto.put("cmd", "auth");
			queryDto.put("authname", authname);
			queryDto.put("authcode", authcode);
			queryDto.put("msg", "");
			queryDto.put("serialno", serialno);
			String queryStr = JsonHelper.encodeObject2Json(queryDto);
			String queryString = queryStr + "XXEE";
			IOSocketClient client = new IOSocketClient();
			MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory(
					CommandSupport.CHARSET, CommandSupport.TAIL,
					CommandSupport.TAIL);
			String revStr = client.sendStr(ip, port, 5000, null, queryString,
					codecFactory, null);
//			String revStr = "{\"cmd\":\"auth\",\"token\":\"123456\",\"retcode\":\"0\",\"serialno\":\"1234567789\"}XXEE";
			revStr = revStr.replace("XXEE", "");
			if(revStr!=null && !"".equals(revStr)){
				Dto dto = JsonHelper.parseSingleJson2Dto(revStr);
				int retcode = dto.getAsInteger("retcode");
				if (retcode < 0) {
					String revStrr = client.sendStr(ip, port, 5000, null, queryString,
							codecFactory, null);
					revStrr = revStrr.replace("XXEE", "");
					if(revStrr!=null&&!"".equals(revStrr)){
						Dto dtoo = JsonHelper.parseSingleJson2Dto(revStr);
						int retcodee = dtoo.getAsInteger("retcode");
						if(retcodee<0){
							log.error("【ERROR】与CRSM交互获取token失败，请查看配置文件中认证参数是否一致，确认CRSM地址和端口是否正确！！！！");
						}else{
							CacheTempData.AUTHTOKEN.put("token", dtoo.getAsString("token"));
						}
					}
				} else {
					CacheTempData.AUTHTOKEN.put("token", dto.getAsString("token"));
				}
			}else{
				String revStrr = client.sendStr(ip, port, 5000, null, queryString,
						codecFactory, null);
				revStrr = revStrr.replace("XXEE", "");
				if(revStrr!=null&&!"".equals(revStrr)){
					Dto dtoo = JsonHelper.parseSingleJson2Dto(revStr);
					int retcodee = dtoo.getAsInteger("retcode");
					if(retcodee<0){
						log.error("【ERROR】与CRSM交互获取token失败，请查看二者配置文件中认证参数是否一致，确认CRSM地址和端口是否正确！！！！");
					}else{
						CacheTempData.AUTHTOKEN.put("token", dtoo.getAsString("token"));
					}
				}else{
					log.error("【ERROR】与CRSM交互获取token失败，请查看二者配置文件中认证参数是否一致，确认CRSM地址和端口是否正确！！！！");
				}
			}
		}		
		long timeSec = (System.currentTimeMillis() - start) / 1000;
		log.info("********************************************");
		if (success) {
			log.info("G4系统集成与应用开发平台[G4Studio]启动成功[" + G4Utils.getCurrentTime() + "]");
			log.info("启动总耗时: " + timeSec / 60 + "分 " + timeSec % 60 + "秒 ");
			
			try {
				String[] args = new String[2];
				args[0]="jdbc.properties";
				args[1]="global.app.properties";
			//	Server.main(args);
			} catch (Throwable e) {
				log.info("EABG启动失败："+e.getMessage());
				e.printStackTrace();
			}
		} else {
			log.error("G4系统集成与应用开发平台[G4Studio]启动失败[" + G4Utils.getCurrentTime() + "]");
			log.error("启动总耗时: " + timeSec / 60 + "分" + timeSec % 60 + "秒");
			System.exit(0);
		}
		log.info("********************************************");
	}

	/**
	 * 识别缺省的JDBC驱动类型
	 * 
	 * @throws SQLException
	 */
	private void initDbType() throws SQLException {
		IDao g4Dao = (IDao) SpringBeanLoader.getSpringBean("g4Dao");
		Connection connection = g4Dao.getConnection();
		String dbString =  connection.getMetaData().getDatabaseProductName().toLowerCase();

		try {
			connection.close();
		} catch (Exception e) {
			log.error(G4Constants.Exception_Head + "未正常关闭数据库连接");
			e.printStackTrace();
		}
		if (dbString.indexOf("ora") > -1) {
			System.setProperty("g4.JdbcType", "oracle");
		} else if (dbString.indexOf("mysql") > -1) {
			System.setProperty("g4.JdbcType", "mysql");
		} else {
			if (log.isErrorEnabled()) {
				log.error(G4Constants.Exception_Head + "G4平台目前还不支持你使用的数据库产品.如需获得支持,请和我们联系!");
			}
			System.exit(0);
		}
//		List idList = g4Dao.queryForList("IdGenerator.getSequenceList") ;
//		if(idList!=null && idList.size()>0){
//			try {
//
//				
//				for(int i = 0 ; i < idList.size() ; i ++){
//					Dto d = (Dto)idList.get(i) ;
//					String key = d.getAsString("fieldname");
//					if(URMIDS.indexOf(key+",")!=-1){
//						Long value = d.getAsLong("maxid");
//						String svalue = mdb.getString(key);
//						Long rvalue = null;
//						if(svalue!=null)
//							rvalue = Long.valueOf(svalue) ;
//						if(rvalue==null || rvalue<value ){
//							mdb.putString(key, String.valueOf(value));
//							log.info("加载到缓存数据:"+key+"==>"+value);
//						}
//					}
//				}
//			} catch (Exception e) {
//				success=false;
//				e.printStackTrace();
//			}
//		}
		
		
	}
}
