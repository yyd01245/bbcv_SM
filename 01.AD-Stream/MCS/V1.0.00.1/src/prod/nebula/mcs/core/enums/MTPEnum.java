package prod.nebula.mcs.core.enums;

import prod.nebula.mcs.core.common.Enumeration;



	public enum MTPEnum implements Enumeration {
		SEQUENCE("sequence"),
		APP_NAME("app_name"),
		LICENCE("licence"),
		STB_ID("stb_id"),
		RETURN_CODE("return_code"),
		AUTH_CODE("auth_code"),
		REGION_ID("region_id"),
		STB_VERSION("stb_version"),
		STB_TYPE("stb_type"),
		STB_ABILITY("stb_ability"),
		AREA_ID("area_id"),
		ACCT_ID("acct_id"),
		SWITCH_TYPE("switch_type"),
		SWITCH_ID("switch_id"),
		VOD_TYPE("vod_type"),
		
		KEY_TYPE("key_type"),
		KEY_VALUE("key_value"),
		
		VERSION("version"),
		APPNAME("appname"),
		HOSTIP("hostip"),
		URL("url"),
		BOOTTIME("boottime"),
		ACTIVE_URL("active_url"),
		STB_INFO("stb_info"),
		OSD_MES("osd_mes"),
		
		//register
		MGWIP("mgwip"),
		MGWTCPPORT("mgwtcpport"),
		MGWUDPPORT("mgwudpport"),
		APPID("appid"),
		MESS("mess"),
		SERIALNO("serialno"),
		;
		private String desc;
		
		private MTPEnum(String desc) {
			this.desc = desc;
		}


		public String getCode() {
			return null;
		}


		public String getDesc() {
			return this.desc;
		}
	}

