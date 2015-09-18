package prod.nebula.service.dto;

import net.sf.json.JSONArray;



/**
 * VodResourceInfo entity. @author MyEclipse Persistence Tools
 */

public class VodResourceInfo  implements java.io.Serializable {


    // Fields    

     /**
	 *  @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
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
     private Integer modelId;
     private String state;

     
     
     private String type;
     private String area;
     private String star;
     private String other;
     
     
     

     
    // Constructors

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


	/** default constructor */
    public VodResourceInfo() {
    }

    
    /** full constructor */
    public VodResourceInfo(String name, Integer grade, String director, String actor, String years, String runtime, String description, String path, String bigPosterPath, String littlePosterPath, String rtspUrl, Integer modelId) {
        this.name = name;
        this.grade = grade;
        this.director = director;
        this.actor = actor;
        this.years = years;
        this.runtime = runtime;
        this.description = description;
        this.path = path;
        this.bigPosterPath = bigPosterPath;
        this.littlePosterPath = littlePosterPath;
        this.rtspUrl = rtspUrl;
        this.modelId = modelId;
    }

   
    // Property accessors

    public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public Integer getGrade() {
        return this.grade;
    }
    
    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getDirector() {
        return this.director;
    }
    
    public void setDirector(String director) {
        this.director = director;
    }

    public String getActor() {
        return this.actor;
    }
    
    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getYears() {
        return this.years;
    }
    
    public void setYears(String years) {
        this.years = years;
    }

    public String getRuntime() {
        return this.runtime;
    }
    
    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return this.path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }

    public String getBigPosterPath() {
        return this.bigPosterPath;
    }
    
    public void setBigPosterPath(String bigPosterPath) {
        this.bigPosterPath = bigPosterPath;
    }

    public String getLittlePosterPath() {
        return this.littlePosterPath;
    }
    
    public void setLittlePosterPath(String littlePosterPath) {
        this.littlePosterPath = littlePosterPath;
    }

    public String getRtspUrl() {
        return this.rtspUrl;
    }
    
    public void setRtspUrl(String rtspUrl) {
        this.rtspUrl = rtspUrl;
    }

    public Integer getModelId() {
        return this.modelId;
    }
    
    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }
   








}