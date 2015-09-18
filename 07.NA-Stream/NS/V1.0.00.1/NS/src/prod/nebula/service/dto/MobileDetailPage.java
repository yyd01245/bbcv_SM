package prod.nebula.service.dto;



/**
 * MobileDetailPage entity. @author MyEclipse Persistence Tools
 */

public class MobileDetailPage  implements java.io.Serializable {


    // Fields    

     /**
	 *  @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = -337120789112064255L;
	private Integer id;
     private String url;
     private String useState;
     private String state;


    // Constructors

    /** default constructor */
    public MobileDetailPage() {
    }

    
    /** full constructor */
    public MobileDetailPage(String url, String useState, String state) {
        this.url = url;
        this.useState = useState;
        this.state = state;
    }

   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }

    public String getUseState() {
        return this.useState;
    }
    
    public void setUseState(String useState) {
        this.useState = useState;
    }

    public String getState() {
        return this.state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
   








}