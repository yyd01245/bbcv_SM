package com.bbcvision.msiAgent.config;

public enum Command {

	ACCESS("user_access_req"), REGIST("user_regist_req"), USERBIND("user_bind_req"),KEYSEND("key_send_req"),
	UPDATENICKNAME("user_updateNickname_req"),LOGIN("user_login_req"),VODPLAY("user_vodplay_req"),SESSIONQUERY("user_sessionquery_req"),
	USERUNBIND("user_unbind_req"),CHOOSERTIME("user_choosetime_req")
	;

	private String value;

	private Command(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	public static Command type(String value) {
		if (ACCESS.value().equals(value)) {
			return ACCESS;
		}else if (REGIST.value().equals(value)) {
			return REGIST;
		}else if (USERBIND.value().equals(value)) {
			return USERBIND;
		}else if (UPDATENICKNAME.value().equals(value)) {
			return UPDATENICKNAME;
		}else if (KEYSEND.value().equals(value)) {
			return KEYSEND;
		}else if (LOGIN.value().equals(value)) {
			return LOGIN;
		}else if (VODPLAY.value().equals(value)) {
			return VODPLAY;
		}else if (SESSIONQUERY.value().equals(value)) {
			return SESSIONQUERY;
		}else if (USERUNBIND.value().equals(value)) {
			return USERUNBIND;
		}else if (CHOOSERTIME.value().equals(value)) {
			return CHOOSERTIME;
		}else{
			return null;
		}
	}
}
