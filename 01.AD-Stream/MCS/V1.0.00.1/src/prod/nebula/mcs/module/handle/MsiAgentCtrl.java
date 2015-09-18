package prod.nebula.mcs.module.handle;

import prod.nebula.mcs.module.exception.CtrlModuleException;



public interface MsiAgentCtrl {
	public String getAction(String req)throws CtrlModuleException;

}
