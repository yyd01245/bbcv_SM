package prod.nebula.test;

public class CommandSupport {
	public enum COMMAND {
		USER_AUTH_REQ,
		USER_AUTH_RESP,
		USER_QUERY_REQ,
		USER_QUERY_RESP,
		auth,
		addterm,
		portalset,
		paramset
	}

	public static String HEADER = "XXBB";
	public static String TAIL = "XXEE";
	public static String CHARSET ="UTF-8";
	
	/**配置文件中 termtype*/
	public final static String TERMTYPE = "def.termtype";
	
	
	public static void main(String args[]){
		System.out.println(COMMAND.auth);
	}
}