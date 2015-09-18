package pojo.dao;



/**
 * NamodelRole entity. @author MyEclipse Persistence Tools
 */

public class VodRoleView  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;

	 

     private Integer id;
     private Integer modelId;
     private Integer vodId;
     private Integer pageDwellTime;
     private String state;
     private Integer orderId;
     
     private String resolution;

    private String name;
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
    private Integer vodModelId;
    private String vodState;

    
    
    private String type;
    private String area;
    private String star;
    private String other;
    
    
    
    
    
    
    
  public String toString(){
    	
    	return "\r\n"+
    	"       id:\""+this.vodId+"\",\r\n"+""+
    	"       name:\""+this.name+"\",\r\n"+""+
    	"       movieintro:\""+this.description+"\",\r\n"+""+
    	"       videosrc:\""+this.path+"\",\r\n"+""+
    	"       director:\""+this.director+"\",\r\n"+""+
    	"       actor:\""+this.actor+"\",\r\n"+""+
    	"       type:\""+this.type+"\",\r\n"+""+
    	"       ondata:\""+this.years+"\",\r\n"+""+
    	"       rstp:\""+this.rtspUrl+"\",\r\n"+""+
    	"       poster:\""+this.bigPosterPath+"\",\r\n"+""+
    	"       sliderposter:\""+this.littlePosterPath+"\",\r\n"+""+
    	"       area:\""+this.area+"\",\r\n"+""+
    	"       star:\""+this.star+"\",\r\n"+""+
    	"       other:\""+this.other+"\",\r\n"+
    	"       timerule:\""+this.pageDwellTime+"\"\r\n";   	
    	
    	
    }

    // Constructors

    public Integer getOrderId() {
		return orderId;
	}


	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
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


	public Integer getVodModelId() {
		return vodModelId;
	}


	public void setVodModelId(Integer vodModelId) {
		this.vodModelId = vodModelId;
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


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	/** default constructor */
    public VodRoleView() {
    }


   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getModelId() {
        return this.modelId;
    }
    
    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public Integer getVodId() {
        return this.vodId;
    }
    
    public void setVodId(Integer vodId) {
        this.vodId = vodId;
    }

    public Integer getPageDwellTime() {
        return this.pageDwellTime;
    }
    
    public void setPageDwellTime(Integer pageDwellTime) {
        this.pageDwellTime = pageDwellTime;
    }
   








}