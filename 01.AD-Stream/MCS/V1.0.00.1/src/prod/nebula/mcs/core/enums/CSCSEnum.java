package prod.nebula.mcs.core.enums;

import prod.nebula.mcs.core.common.Enumeration;



	public enum CSCSEnum implements Enumeration {
		CMD("cmd"),
		TERMID("termid"),
		TERMTYPE("termtype"),
		FRID("rfid"),
		PMTID("pid"),
		SERVICEID("serviceid"),
		SPID("spId"),
		TERMNET("termnet"),
		REGIONID("regionid"),
		PORTALID("portalid"),
		TERMVERSION("termversion"),
		GWTYPE("gwtype"),
		RETCODE("retcode"),
		SERIALNO("serialno"),
		URL("url"),
		SWITCHTYPE("switchtype"),
		OUTPUTID("outputid"),
		KEYDEV("keydev"),
		SIP("sip"),
		SPORT("sport"),
		DVBINFO("dvbinfo"),
		DONECODE("doneCode"),
		DONEDATE("doneDate"),
		OSD_DISPLAY_TYPE("osd_display_type"),
		OSD_TITLE("osd_title"),
		OSD_TEXT("osd_text"),
		OSD_TIME_OUT("osd_time_out"),
		MSG("msg"),
		GOBACKURL("gobackurl"),
		SESSIONID("sessionid"),
		SID("sid"),
		CTYPE("ctype"),
		NOTICEMSG("noticemsg"),
		
		;
		private String desc;
		
		private CSCSEnum(String desc) {
			this.desc = desc;
		}


		public String getCode() {
			return null;
		}


		public String getDesc() {
			return this.desc;
		}
	}

