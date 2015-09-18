package prod.nebula.mcs.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import prod.nebula.mcs.util.Commons;



public class Server {
	public final Logger logger = Logger.getLogger(getClass());

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Long time1 = Calendar.getInstance().getTimeInMillis();

		Server server = new Server();
		try {
			server.start();

			new ClassPathXmlApplicationContext("spring/applicationContext.xml");

		} catch (Exception e) {
			server.logger.error("\r\nServer服务启动时发生异常\r\n" + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		server.logger.info("\r\nServer服务已经启动\r\n");
		Long time2 = Calendar.getInstance().getTimeInMillis();
		System.out.println("花销 时间为:" + (time2 - time1));
	}

	/**
	 * @throws IOException
	 */
	public void start() throws IOException {
		try {
			initLog4j();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initLog4j() throws IOException {
		String configFilePath;
		Properties p = null;
		configFilePath = System.getenv("MCS_LOG_FILE");
		if (configFilePath != null && !configFilePath.equals("")) {
			p = Commons.loadFile(configFilePath);
			if (p != null) {
				PropertyConfigurator.configure(configFilePath);
				return;
			}
		}

		configFilePath = System.getProperty("MCS_LOG_FILE");
		if (configFilePath != null && !configFilePath.equals("")) {
			p = Commons.loadFile(configFilePath);
			if (p != null) {
				PropertyConfigurator.configure(configFilePath);
				return;
			}
		}
	}
}
