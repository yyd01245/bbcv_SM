package pojo;



/**
 * BannerVodMapping entity. @author MyEclipse Persistence Tools
 */

public class BannerVodMapping  implements java.io.Serializable {


	private static final long serialVersionUID = -6077980748493465667L;
	private Integer id;
     private Integer bannerId;
     private Integer vodId;
     private Integer orderId;
     private String state;
     private String resolution;
     
     private String sliderPoster;
     
     
     
     


    // Constructors

    public String getSliderPoster() {
		return sliderPoster;
	}


	public void setSliderPoster(String sliderPoster) {
		this.sliderPoster = sliderPoster;
	}


	public String getResolution() {
		return resolution;
	}


	public void setResolution(String resolution) {
		this.resolution = resolution;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	/** default constructor */
    public BannerVodMapping() {
    }

    
    /** full constructor */
    public BannerVodMapping(Integer bannerId, Integer vodId, Integer orderId) {
        this.bannerId = bannerId;
        this.vodId = vodId;
        this.orderId = orderId;
    }

   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBannerId() {
        return this.bannerId;
    }
    
    public void setBannerId(Integer bannerId) {
        this.bannerId = bannerId;
    }

    public Integer getVodId() {
        return this.vodId;
    }
    
    public void setVodId(Integer vodId) {
        this.vodId = vodId;
    }

    public Integer getOrderId() {
        return this.orderId;
    }
    
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
   








}