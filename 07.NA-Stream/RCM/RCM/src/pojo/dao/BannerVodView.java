package pojo.dao;



/**
 * BannerVodMapping entity. @author MyEclipse Persistence Tools
 */

public class BannerVodView  implements java.io.Serializable {


    // Fields    

	private static final long serialVersionUID = -6077980748493465667L;
	private Integer id;
     private Integer bannerId;
     private String bannerName;
     private Integer vodId;
     private String vodName;
     private Integer orderId;
     private String state;

    private Integer grade;
    private String director;
    private String actor;
    private String years;
    private String runtime;
    private String description;
    private String path;
    private String bigPosterPath;
    private String littlePosterPath;
    private String rtspUrl;
    private Integer modelId;
    private String vodState;
     
    private String type;
    private String area;
    private String star;
    private String other;
    
    
    private String bannerResolution;
    private String vodResolution;
    
    private String sliderPoster;
    
    

    public String getSliderPoster() {
		return sliderPoster;
	}


	public void setSliderPoster(String sliderPoster) {
		this.sliderPoster = sliderPoster;
	}

    
    
    
    public String getBannerResolution() {
		return bannerResolution;
	}


	public void setBannerResolution(String bannerResolution) {
		this.bannerResolution = bannerResolution;
	}


	public String getVodResolution() {
		return vodResolution;
	}


	public void setVodResolution(String vodResolution) {
		this.vodResolution = vodResolution;
	}


	public String toString(){
    	
    	return "\r\n"+
    	"       id:\""+this.vodId+"\",\r\n"+""+
    	"       name:\""+this.vodName+"\",\r\n"+""+
    	"       movieintro:\""+this.description+"\",\r\n"+""+
    	"       videosrc:\""+this.path+"\",\r\n"+""+
    	"       director:\""+this.director+"\",\r\n"+""+
    	"       actor:\""+this.actor+"\",\r\n"+""+
    	"       type:\""+this.type+"\",\r\n"+""+
    	"       ondata:\""+this.years+"\",\r\n"+""+
    	"       rstp:\""+this.rtspUrl+"\",\r\n"+""+
    	"       poster:\""+this.littlePosterPath+"\",\r\n"+""+
    	"       sliderposter:\""+this.bigPosterPath+"\",\r\n"+""+
    	"       area:\""+this.area+"\",\r\n"+""+
    	"       star:\""+this.grade+"\",\r\n"+""+
    	"       other:\""+this.other+"\"\r\n"+"";
    	
    }
    

    // Constructors



	public Integer getGrade() {
		return grade;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getArea() {
		return area;
	}


	public void setArea(String area) {
		this.area = area;
	}


	public String getStar() {
		return star;
	}


	public void setStar(String star) {
		this.star = star;
	}


	public String getOther() {
		return other;
	}


	public void setOther(String other) {
		this.other = other;
	}


	public void setGrade(Integer grade) {
		this.grade = grade;
	}


	public String getDirector() {
		return director;
	}


	public void setDirector(String director) {
		this.director = director;
	}


	public String getActor() {
		return actor;
	}


	public void setActor(String actor) {
		this.actor = actor;
	}


	public String getYears() {
		return years;
	}


	public void setYears(String years) {
		this.years = years;
	}


	public String getRuntime() {
		return runtime;
	}


	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public String getBigPosterPath() {
		return bigPosterPath;
	}


	public void setBigPosterPath(String bigPosterPath) {
		this.bigPosterPath = bigPosterPath;
	}


	public String getLittlePosterPath() {
		return littlePosterPath;
	}


	public void setLittlePosterPath(String littlePosterPath) {
		this.littlePosterPath = littlePosterPath;
	}


	public String getRtspUrl() {
		return rtspUrl;
	}


	public void setRtspUrl(String rtspUrl) {
		this.rtspUrl = rtspUrl;
	}


	public Integer getModelId() {
		return modelId;
	}


	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}




	public String getVodState() {
		return vodState;
	}


	public void setVodState(String vodState) {
		this.vodState = vodState;
	}


	public String getBannerName() {
		return bannerName;
	}


	public void setBannerName(String bannerName) {
		this.bannerName = bannerName;
	}


	public String getVodName() {
		return vodName;
	}


	public void setVodName(String vodName) {
		this.vodName = vodName;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	/** default constructor */
    public BannerVodView() {
    }

    
    /** full constructor */
    public BannerVodView(Integer bannerId, Integer vodId, Integer orderId) {
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