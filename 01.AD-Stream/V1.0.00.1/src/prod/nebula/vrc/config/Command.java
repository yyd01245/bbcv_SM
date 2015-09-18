package prod.nebula.vrc.config;

public enum Command {

	LOGIN("login"), LOGOUT("logout"), GOBACK("goback"),TIMEOUT("timeout"),
	PLAY("play")
	,RESUME("resume")
	,PAUSE("pause")
	,FORWARD("forward")
	,BACKWARD("backward")
	,CHOOSETIME("choosetime"),
	GETTIME("gettime"),
	APP_PLATFORM_EXIST_REQ("APP_PLATFORM_EXIST_REQ"),
	SESSION_CHECK("session_check"),
	
	VGW_LOCALADDRESS("vgw.localipaddress"),
	VGW_PORT("vgw.port"),
	BGW_HEARTBEAT_INTERVAL("bgw.heartbeat.interval"),
	;

	private String value;

	private Command(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	public static Command type(String value) {
		if (LOGIN.value().equals(value)) {
			return LOGIN;
		} else if (LOGOUT.value().equals(value)) {
			return LOGOUT;
		} else if (GOBACK.value().equals(value)) {
			return GOBACK;
			
		} else if (PLAY.value().equals(value)) {
			return PLAY;
		} else if (RESUME.value().equals(value)) {
			return RESUME;
		}else if (PAUSE.value().equals(value)) {
			return PAUSE;
		}else if (FORWARD.value().equals(value)) {
			return FORWARD;
		}else if (BACKWARD.value().equals(value)) {
			return BACKWARD;
		}else if (CHOOSETIME.value().equals(value)) {
			return CHOOSETIME;
		}else if (TIMEOUT.value().equals(value)) {
			return TIMEOUT;
		}else if (APP_PLATFORM_EXIST_REQ.value().equals(value)) {
			return APP_PLATFORM_EXIST_REQ;
		}else if (SESSION_CHECK.value().equals(value)) {
			return SESSION_CHECK;
		}else if (GETTIME.value().equals(value)) {
			return GETTIME;
		}
		else {
			return null;
		}
	}
}
