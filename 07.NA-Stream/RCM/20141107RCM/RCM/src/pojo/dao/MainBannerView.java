package pojo.dao;



/**
 * MainpageInfo entity. @author MyEclipse Persistence Tools
 */

public class MainBannerView  implements java.io.Serializable {


    // Fields    

   
	private static final long serialVersionUID = 6324608961920961404L;
	private Integer mainPage;
     private String mainpageUrl;
     private Integer modelId;
     private Integer orderId;
     private String state;
     private String bannerName;

     
     private String resolution; // 分辨率 0，高清，1标清
     
     
     
     
     
    // Constructors

    public String getResolution() {
		return resolution;
	}


	public void setResolution(String resolution) {
		this.resolution = resolution;
	}


	/** default constructor */
    public MainBannerView() {
    }

    
    public String getBannerName() {
		return bannerName;
	}


	public void setBannerName(String bannerName) {
		this.bannerName = bannerName;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	/** full constructor */
    public MainBannerView(String mainpageUrl, Integer modelId, Integer orderId) {
        this.mainpageUrl = mainpageUrl;
        this.modelId = modelId;
        this.orderId = orderId;
    }

   
    // Property accessors

    public Integer getMainPage() {
        return this.mainPage;
    }
    
    public void setMainPage(Integer mainPage) {
        this.mainPage = mainPage;
    }

    public String getMainpageUrl() {
        return this.mainpageUrl;
    }
    
    public void setMainpageUrl(String mainpageUrl) {
        this.mainpageUrl = mainpageUrl;
    }

    public Integer getModelId() {
        return this.modelId;
    }
    
    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public Integer getOrderId() {
        return this.orderId;
    }
    
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
   








}