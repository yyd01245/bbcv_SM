package prod.nebula.service.dto;

/*
 * 用户信息实体类
 */
public class UserInfo {
   
	 private int userId;
	 private String username;
	 private String passwd;
	 
	 private String vodPage;
	 private String token;
	 private int userStatus;
	 
	 
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getVodPage() {
		return vodPage;
	}
	public void setVodPage(String vodPage) {
		this.vodPage = vodPage;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}
	 
	 
	 
  public String toString(){

	  
	  return this.userId+" "+this.username+" "+this.passwd+" "+this.vodPage+" "+this.token+" "+this.userStatus;
  }
	 
	 
}
