package pojo;

/**
 * UcsIpqamInfo entity. @author MyEclipse Persistence Tools
 */

public class UcsIpqamInfo implements java.io.Serializable {

	// Fields

	
	private static final long serialVersionUID = 1L;
	private Integer iipqamnum;
	private String strIpqamName;
	private String strIpqamIp;
	private Integer iipqamManagerPort;
	private String strIpqamType;
	private String strIpqamModel;
	private String strIpqamManufacturers;
	private String strIpqamComment;
	private Integer iisSupportR6;

	// Constructors

	/** default constructor */
	public UcsIpqamInfo() {
	}

	/** full constructor */
	public UcsIpqamInfo(String strIpqamName, String strIpqamIp,
			Integer iipqamManagerPort, String strIpqamType,
			String strIpqamModel, String strIpqamManufacturers,
			String strIpqamComment, Integer iisSupportR6) {
		this.strIpqamName = strIpqamName;
		this.strIpqamIp = strIpqamIp;
		this.iipqamManagerPort = iipqamManagerPort;
		this.strIpqamType = strIpqamType;
		this.strIpqamModel = strIpqamModel;
		this.strIpqamManufacturers = strIpqamManufacturers;
		this.strIpqamComment = strIpqamComment;
		this.iisSupportR6 = iisSupportR6;
	}

	// Property accessors

	public Integer getIipqamnum() {
		return this.iipqamnum;
	}

	public void setIipqamnum(Integer iipqamnum) {
		this.iipqamnum = iipqamnum;
	}

	public String getStrIpqamName() {
		return this.strIpqamName;
	}

	public void setStrIpqamName(String strIpqamName) {
		this.strIpqamName = strIpqamName;
	}

	public String getStrIpqamIp() {
		return this.strIpqamIp;
	}

	public void setStrIpqamIp(String strIpqamIp) {
		this.strIpqamIp = strIpqamIp;
	}

	public Integer getIipqamManagerPort() {
		return this.iipqamManagerPort;
	}

	public void setIipqamManagerPort(Integer iipqamManagerPort) {
		this.iipqamManagerPort = iipqamManagerPort;
	}

	public String getStrIpqamType() {
		return this.strIpqamType;
	}

	public void setStrIpqamType(String strIpqamType) {
		this.strIpqamType = strIpqamType;
	}

	public String getStrIpqamModel() {
		return this.strIpqamModel;
	}

	public void setStrIpqamModel(String strIpqamModel) {
		this.strIpqamModel = strIpqamModel;
	}

	public String getStrIpqamManufacturers() {
		return this.strIpqamManufacturers;
	}

	public void setStrIpqamManufacturers(String strIpqamManufacturers) {
		this.strIpqamManufacturers = strIpqamManufacturers;
	}

	public String getStrIpqamComment() {
		return this.strIpqamComment;
	}

	public void setStrIpqamComment(String strIpqamComment) {
		this.strIpqamComment = strIpqamComment;
	}

	public Integer getIisSupportR6() {
		return this.iisSupportR6;
	}

	public void setIisSupportR6(Integer iisSupportR6) {
		this.iisSupportR6 = iisSupportR6;
	}

}