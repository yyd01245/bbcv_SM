package prod.nebula.mcs.core.enums;

import prod.nebula.mcs.core.common.Enumeration;




public enum UserErrorCode implements Enumeration {
		
		LICENCE_ERROR("-1621", "licence is error"),
		
		AuthCode_HAS_NOT_SET("-1622", "authcode have not set"),
		
		AUTHCODE_ERROR("-1623", "AUTHCODE is error"),
		
		USERINFO_NULL("-1624", "userinfo is null"),
		
		UPDATE_USER_FAIL("-1625", "update user status fail"),
		
		PLAY_VOD_FAIL("-1626", "play vod  fail"),
		
		VOD_IPPORT_NULL("-1627", "vod ip port is  null"),
		
		LOGINCLOUD_FAIL("-1628", "login cloud return_NULL"),
		
		CHANNEL_LOGIN_FAIL("-1629", "can not find dvb channel"),
		//cscs注册的时候失败,创建用户session  
		CSCS_REGISTER_FAIL("-1630", "cscs register fail"),
		//退出cscs 平台  失败
		CSCS_UNREGISTER_FAIL("-1631", "cscs register fail"),
		
		UPDATE_USER_SESSIONID_FAIL("-1632", "update session user status fail"),
		
		GET_HOSTNAME_FAIL("-1633", "get host name from cscs fail "),
		
		GET_CSCS_SESSION_FAIL("-1634", "get session from cscs is null "),
		
		ADD_TERMTYPE_FAIL("-1635", "add_termtype_fail "),
		
		ADD_TERMTYPE_RETURN_NULL("-1636", "add_termtype_from_cums_return_NULL "),
		
		CHANNEL_CHANGE_FAIL("-1637", "channel_change_null "),
		
		CHANNEL_CHANGE_RETURN_NULL("-1638", "channel_change_return_null "),
		
		VOD_LOGIN_RETURN_NULL("-1639", "vod_login_return_null "),
		
		SHOWOSD_ERROR("-1640", "show osd error"),
		
		HEXNUMBER_ERROR("-1641", "HexNumber  change error"),
		
		DVB_RF_SERVICE_ERROR("-1642", "dbv change by rfid is null")
		;
		
		private String code;
		private String desc;
		
		private UserErrorCode(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public String getCode() {
			return code;
		}
		
		public String getDesc() {
			return desc;
		}

}
