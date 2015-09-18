package pojo;

/**
 * UcsAdvInfo entity. @author MyEclipse Persistence Tools
 */

public class UcsAdvInfo implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String advname;
	private String advip;
	private String advport;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAdvname() {
		return advname;
	}
	public void setAdvname(String advname) {
		this.advname = advname;
	}
	public String getAdvip() {
		return advip;
	}
	public void setAdvip(String advip) {
		this.advip = advip;
	}
	public String getAdvport() {
		return advport;
	}
	public void setAdvport(String advport) {
		this.advport = advport;
	}
	
	
	
}