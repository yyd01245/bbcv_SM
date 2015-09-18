package pojo;



/**
 * VoddetilModelInfo entity. @author MyEclipse Persistence Tools
 */

public class VoddetilModelInfo  implements java.io.Serializable {


    // Fields    

     /**
	 *  @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	private Integer modelId;
     private String modelUrl;


    // Constructors

    /** default constructor */
    public VoddetilModelInfo() {
    }

    
    /** full constructor */
    public VoddetilModelInfo(String modelUrl) {
        this.modelUrl = modelUrl;
    }

   
    // Property accessors

    public Integer getModelId() {
        return this.modelId;
    }
    
    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public String getModelUrl() {
        return this.modelUrl;
    }
    
    public void setModelUrl(String modelUrl) {
        this.modelUrl = modelUrl;
    }
   








}