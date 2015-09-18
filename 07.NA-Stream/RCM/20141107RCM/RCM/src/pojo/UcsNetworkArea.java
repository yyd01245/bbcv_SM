package pojo;

/**
 * UcsNetworkArea entity. @author MyEclipse Persistence Tools
 */

public class UcsNetworkArea implements java.io.Serializable {

	// Fields

	
	private static final long serialVersionUID = 1L;
	private String strNetRegionNum;
	private String strNetRegionName;
	private Integer inavgationStreamNum;
	private Integer sdiNavgationStreamNum;
	private Integer iadvertisementStreamNum;
	private Integer sdiAdvertisementStreamNum;
	private String strNetworkComment;

	// Constructors

	/** default constructor */
	public UcsNetworkArea() {
	}

	/** full constructor */
	public UcsNetworkArea(String strNetRegionName, Integer inavgationStreamNum,
			Integer sdiNavgationStreamNum, Integer iadvertisementStreamNum,
			Integer sdiAdvertisementStreamNum, String strNetworkComment) {
		this.strNetRegionName = strNetRegionName;
		this.inavgationStreamNum = inavgationStreamNum;
		this.sdiNavgationStreamNum = sdiNavgationStreamNum;
		this.iadvertisementStreamNum = iadvertisementStreamNum;
		this.sdiAdvertisementStreamNum = sdiAdvertisementStreamNum;
		this.strNetworkComment = strNetworkComment;
	}

	// Property accessors

	public String getStrNetRegionNum() {
		return this.strNetRegionNum;
	}

	public void setStrNetRegionNum(String strNetRegionNum) {
		this.strNetRegionNum = strNetRegionNum;
	}

	public String getStrNetRegionName() {
		return this.strNetRegionName;
	}

	public void setStrNetRegionName(String strNetRegionName) {
		this.strNetRegionName = strNetRegionName;
	}

	public Integer getInavgationStreamNum() {
		return this.inavgationStreamNum;
	}

	public void setInavgationStreamNum(Integer inavgationStreamNum) {
		this.inavgationStreamNum = inavgationStreamNum;
	}

	public Integer getSdiNavgationStreamNum() {
		return this.sdiNavgationStreamNum;
	}

	public void setSdiNavgationStreamNum(Integer sdiNavgationStreamNum) {
		this.sdiNavgationStreamNum = sdiNavgationStreamNum;
	}

	public Integer getIadvertisementStreamNum() {
		return this.iadvertisementStreamNum;
	}

	public void setIadvertisementStreamNum(Integer iadvertisementStreamNum) {
		this.iadvertisementStreamNum = iadvertisementStreamNum;
	}

	public Integer getSdiAdvertisementStreamNum() {
		return this.sdiAdvertisementStreamNum;
	}

	public void setSdiAdvertisementStreamNum(Integer sdiAdvertisementStreamNum) {
		this.sdiAdvertisementStreamNum = sdiAdvertisementStreamNum;
	}

	public String getStrNetworkComment() {
		return this.strNetworkComment;
	}

	public void setStrNetworkComment(String strNetworkComment) {
		this.strNetworkComment = strNetworkComment;
	}

}