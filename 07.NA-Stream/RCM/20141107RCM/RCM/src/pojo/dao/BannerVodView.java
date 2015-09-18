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
    

    
    
    
    private String tvPosterPath1;      
    private String tvPosterPath2;
    private String tvPosterPath3;
    private String tvPosterPath4;
    private String mbPosterPath1;
    private String mbPosterPath2;
    private String mbPosterPath3;
    private String mbPosterPath4;
    
   


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
    	"       tvPosterPath1:\""+this.tvPosterPath1+"\",\r\n"+""+
    	"       tvPosterPath2:\""+this.tvPosterPath2+"\",\r\n"+""+
    	"       tvPosterPath3:\""+this.tvPosterPath3+"\",\r\n"+""+
    	"       tvPosterPath4:\""+this.tvPosterPath4+"\",\r\n"+""+
    	"       mbPosterPath1:\""+this.mbPosterPath1+"\",\r\n"+""+
    	"       mbPosterPath2:\""+this.mbPosterPath2+"\",\r\n"+""+
    	"       mbPosterPath3:\""+this.mbPosterPath3+"\",\r\n"+""+
    	"       mbPosterPath4:\""+this.mbPosterPath4+"\",\r\n"+""+
    	"       area:\""+this.area+"\",\r\n"+""+
    	"       star:\""+this.grade+"\",\r\n"+""+
    	"       other:\""+this.other+"\"\r\n"+"";
    	
    }




	public Integer getId() {
		return id;
	}




	public void setId(Integer id) {
		this.id = id;
	}




	public Integer getBannerId() {
		return bannerId;
	}




	public void setBannerId(Integer bannerId) {
		this.bannerId = bannerId;
	}




	public String getBannerName() {
		return bannerName;
	}




	public void setBannerName(String bannerName) {
		this.bannerName = bannerName;
	}




	public Integer getVodId() {
		return vodId;
	}




	public void setVodId(Integer vodId) {
		this.vodId = vodId;
	}




	public String getVodName() {
		return vodName;
	}




	public void setVodName(String vodName) {
		this.vodName = vodName;
	}




	public Integer getOrderId() {
		return orderId;
	}




	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}




	public String getState() {
		return state;
	}




	public void setState(String state) {
		this.state = state;
	}




	public Integer getGrade() {
		return grade;
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




	public String getSliderPoster() {
		return sliderPoster;
	}




	public void setSliderPoster(String sliderPoster) {
		this.sliderPoster = sliderPoster;
	}




	public String getTvPosterPath1() {
		return tvPosterPath1;
	}




	public void setTvPosterPath1(String tvPosterPath1) {
		this.tvPosterPath1 = tvPosterPath1;
	}




	public String getTvPosterPath2() {
		return tvPosterPath2;
	}




	public void setTvPosterPath2(String tvPosterPath2) {
		this.tvPosterPath2 = tvPosterPath2;
	}




	public String getTvPosterPath3() {
		return tvPosterPath3;
	}




	public void setTvPosterPath3(String tvPosterPath3) {
		this.tvPosterPath3 = tvPosterPath3;
	}




	public String getTvPosterPath4() {
		return tvPosterPath4;
	}




	public void setTvPosterPath4(String tvPosterPath4) {
		this.tvPosterPath4 = tvPosterPath4;
	}




	public String getMbPosterPath1() {
		return mbPosterPath1;
	}




	public void setMbPosterPath1(String mbPosterPath1) {
		this.mbPosterPath1 = mbPosterPath1;
	}




	public String getMbPosterPath2() {
		return mbPosterPath2;
	}




	public void setMbPosterPath2(String mbPosterPath2) {
		this.mbPosterPath2 = mbPosterPath2;
	}




	public String getMbPosterPath3() {
		return mbPosterPath3;
	}




	public void setMbPosterPath3(String mbPosterPath3) {
		this.mbPosterPath3 = mbPosterPath3;
	}




	public String getMbPosterPath4() {
		return mbPosterPath4;
	}




	public void setMbPosterPath4(String mbPosterPath4) {
		this.mbPosterPath4 = mbPosterPath4;
	}
    

 







}