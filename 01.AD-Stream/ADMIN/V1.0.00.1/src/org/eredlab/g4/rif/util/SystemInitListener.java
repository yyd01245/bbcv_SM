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
import org.eredlab.g4.ccl.properties.PropertiesFactory;
import org.eredlab.g4.ccl.properties.PropertiesFile;
import org.eredlab.g4.ccl.properties.PropertiesHelper;
import org.eredlab.g4.ccl.util.G4Constants;
import org.eredlab.g4.ccl.util.G4Utils;
import org.eredlab.g4.common.util.RedisUseable;
import org.springframework.context.ApplicationContext;

import com.bbcv.mdb.redis.MDBInterface;

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
			log.info("云视频服务平台-承载管理开始启动...");
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
		long timeSec = (System.currentTimeMillis() - start) / 1000;
		log.info("********************************************");
		if (success) {
			log.info("云视频服务平台-承载管理启动成功[" + G4Utils.getCurrentTime() + "]");
			log.info("启动总耗时: " + timeSec / 60 + "分 " + timeSec % 60 + "秒 ");
			
//			try {
//				String[] args = new String[2];
//				args[0]="jdbc.properties";
//				args[1]="global.app.properties";
//				Server.main(args);
//			} catch (Throwable e) {
//				log.info("EABG启动失败："+e.getMessage());
//				e.printStackTrace();
//			}
		} else {
			log.error("云视频服务平台-承载管理启动失败[" + G4Utils.getCurrentTime() + "]");
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
	}
}
