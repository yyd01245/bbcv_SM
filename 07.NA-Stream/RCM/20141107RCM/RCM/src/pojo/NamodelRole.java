package pojo;



/**
 * NamodelRole entity. @author MyEclipse Persistence Tools
 */

public class NamodelRole  implements java.io.Serializable {



	private static final long serialVersionUID = 1L;

	 

     private Integer id;
     private Integer modelId;
     private Integer vodId;
     private Integer pageDwellTime;
     private String state;
     private Integer orderId;
     
     private String resolution;
     
     
     
     
     
     
     
     
     

    // Constructors

    public String getResolution() {
		return resolution;
	}


	public void setResolution(String resolution) {
		this.resolution = resolution;
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


	/** default constructor */
    public NamodelRole() {
    }

    
    /** full constructor */
    public NamodelRole(Integer modelId, Integer vodId, Integer pageDwellTime) {
        this.modelId = modelId;
        this.vodId = vodId;
        this.pageDwellTime = pageDwellTime;
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