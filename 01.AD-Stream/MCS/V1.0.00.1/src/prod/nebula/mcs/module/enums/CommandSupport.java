package prod.nebula.mcs.module.enums;

public class CommandSupport {
	public enum COMMAND {
		MTAP_AUTHCODE_REQ,MTAP_AUTHCODE_RESP,MTAP_LOGIN_REQ
	}
	
	public enum CSCSCOM {
		channelchange,httpchange,APPLOGIN,logout,vodlogin,login,addvodurl,register,unregister,appchange,screannotices
	}
	
	public enum UDPCOMMAND {
		MTAP_KEYCTRL_REQ,MTAP_KEYCTRL_RESP,KeyDown
	}
	
	public enum MTPCOMMAND {
		registermgw
	}
	
	public enum MSIAGENTCOMMAND {
		get_dev_netcard_info,add_switch_task,execute_switch,query_task_input,get_all_task_info,del_switch_task,reset_device,cloud_switch_task,add_ads_stream,del_ads_stream,check_session
	}
	
	public enum SWITCHMODEL {
		A_B,A_F_B,A_AB_B,A_AF_B
	}
}
