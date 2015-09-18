package pojo;



/**
 * BannerInfo entity. @author MyEclipse Persistence Tools
 */

public class BannerInfo  implements java.io.Serializable {


    // Fields    

	private static final long serialVersionUID = -5716472441808304847L;
	private Integer bannerId;
     private String bannerName;
     private Integer itemNum;
     private String state;

    // Constructors

    /** default constructor */
    public BannerInfo() {
    }

    
    /** full constructor */
    public BannerInfo(String bannerName, Integer itemNum) {
        this.bannerName = bannerName;
        this.itemNum = itemNum;
    }

   
    // Property accessors

    public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public Integer getBannerId() {
        return this.bannerId;
    }
    
    public void setBannerId(Integer bannerId) {
        this.bannerId = bannerId;
    }

    public String getBannerName() {
        return this.bannerName;
    }
    
    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }

    public Integer getItemNum() {
        return this.itemNum;
    }
    
    public void setItemNum(Integer itemNum) {
        this.itemNum = itemNum;
    }
   








}