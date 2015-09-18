package prod.nebula.vrc.module.http;

import java.util.Properties;

import org.apache.mina.core.session.IoSession;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import prod.nebula.vrc.core.CoreLoader;
import prod.nebula.vrc.core.common.constants.ResConstants;
import prod.nebula.vrc.core.module.ModuleLoader;
import prod.nebula.vrc.module.http.servlet.InvokeServlet;
import prod.nebula.vrc.util.ApplicationContextHelper;
import prod.nebula.vrc.util.PropertiesUtil;

public class HttpModuleLoader extends ModuleLoader {
	public final Logger logger = LoggerFactory.getLogger(getClass());
	private IoSession iosession;
	private ApplicationContext atx;

	public IoSession getIosession() {
		return iosession;
	}

	public ApplicationContext getAtx() {
		return atx;
	}

	public HttpModuleLoader() {
		try {
			ApplicationContext atx = new ClassPathXmlApplicationContext(
					"module/" + ResConstants.HTTP_MODULE_NAME + "/spring.xml");
			this.atx = atx;
			// ¼ÓÔØHTTP·þÎñ
			loadJetty();
			logger.info("[HTTP] StartUp Http Service Over");
		} catch (Exception e) {
			logger.info("[HTTP] Fail to StartUp Http Service");
			logger.error("[HttpModuleLoader]module:"
					+ ResConstants.HTTP_MODULE_NAME + " load failed", e);
			System.exit(0);
		}
	}

	@Override
	public Properties loadContants(String moduleName) {
		Properties properties = null;
		try {
			properties = PropertiesUtil.getInstance().getProperties(moduleName);
			this.properties = properties;
		} catch (Exception e) {
			logger.info("[HttpModuleLoader]loadContants", e.getMessage());
		}
		return properties;
	}
	
	private void loadJetty() {
		try {
			CoreLoader core = (CoreLoader) ApplicationContextHelper
					.getBean("core");
			String port = core.getProperties().getProperty(
					Constants.HTTP_SERVER_PORT, "8888");
			Server server = new Server(Integer.parseInt(port));
			QueuedThreadPool threadPool = new QueuedThreadPool();
			threadPool.setName("Jetty Threads");
			threadPool.setMinThreads(Integer.valueOf(core.getProperties()
					.getProperty(Constants.HTTP_MIN_THREADS, "20")));
			threadPool.setMaxThreads(Integer.valueOf(core.getProperties()
					.getProperty(Constants.HTTP_MAX_THREADS, "50")));
			threadPool.setMaxIdleTimeMs(Integer.valueOf(core.getProperties()
					.getProperty(Constants.HTTP_MAX_IDLETIMEMS, "2000")));
			server.setThreadPool(threadPool);
			ServletContextHandler context = new ServletContextHandler(
					ServletContextHandler.SESSIONS);
			context.setContextPath("/");
			server.setHandler(context);

			context.addServlet(new ServletHolder(new InvokeServlet()),
					"/vodm/*");

			server.start();
		} catch (Exception e) {
			logger.error("Load HTTP Service Error", e);
		}
	}
}
