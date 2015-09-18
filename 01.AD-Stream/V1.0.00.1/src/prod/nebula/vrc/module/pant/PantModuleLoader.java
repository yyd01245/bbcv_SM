package prod.nebula.vrc.module.pant;

import java.util.Timer;

import prod.nebula.vrc.config.Command;
import prod.nebula.vrc.core.CoreLoader;
import prod.nebula.vrc.module.pant.client.RegBwClient;
import prod.nebula.vrc.util.ApplicationContextHelper;

public class PantModuleLoader {

	public PantModuleLoader()
	{
		CoreLoader core = (CoreLoader) ApplicationContextHelper
		.getBean("core");
		String ip=core.getProperties().getProperty(Command.VGW_LOCALADDRESS.value(),"").toString();
		int port=Integer.valueOf(core.getProperties().getProperty(Command.VGW_PORT.value(), "0"));
		int interval=Integer.valueOf(core.getProperties().getProperty(Command.BGW_HEARTBEAT_INTERVAL.value(), "6000"));
		
		Timer timer = new Timer();
		//调度任务，每隔多长时间加载一次配置
		timer.schedule(new RegBwClient(ip,port), 1000L, interval);
	}
	
	
}
