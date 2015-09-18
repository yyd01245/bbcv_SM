package pojo;



/**
 * TvNavigate entity. @author MyEclipse Persistence Tools
 */

public class TvNavigate  implements java.io.Serializable {


 
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String navigateCategory;
	private String navigateUrl;
	private String resolution;
	private String remark;
	private String state;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNavigateCategory() {
		return navigateCategory;
	}
	public void setNavigateCategory(String navigateCategory) {
		this.navigateCategory = navigateCategory;
	}
	public String getNavigateUrl() {
		return navigateUrl;
	}
	public void setNavigateUrl(String navigateUrl) {
		this.navigateUrl = navigateUrl;
	}
	public String getResolution() {
		return resolution;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
	


}