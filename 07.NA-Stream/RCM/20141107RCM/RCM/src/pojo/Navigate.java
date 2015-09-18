package pojo;

/**
 * Navigate entity. @author MyEclipse Persistence Tools
 */

public class Navigate implements java.io.Serializable {

	// Fields

	/**
	 *  @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String navigateCategory;
	private String navigateUrl;
	private String resolution;
	private String remark;
	private String state;

	// Constructors

	/** default constructor */
	public Navigate() {
	}

	/** full constructor */
	public Navigate(String navigateCategory, String navigateUrl,
			String resolution, String remark, String state) {
		this.navigateCategory = navigateCategory;
		this.navigateUrl = navigateUrl;
		this.resolution = resolution;
		this.remark = remark;
		this.state = state;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNavigateCategory() {
		return this.navigateCategory;
	}

	public void setNavigateCategory(String navigateCategory) {
		this.navigateCategory = navigateCategory;
	}

	public String getNavigateUrl() {
		return this.navigateUrl;
	}

	public void setNavigateUrl(String navigateUrl) {
		this.navigateUrl = navigateUrl;
	}

	public String getResolution() {
		return this.resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

}