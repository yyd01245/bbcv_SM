package prod.nebula.mcs.core.cotorller;

import java.util.Map;

public interface Controller {
	public Map<String, Object> commandHandler(Map<String, Object> message,
			CommandSupport.COMMAND command) throws ControllerException;
}
