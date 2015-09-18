package pojo;



/**
 * MobileMainPage entity. @author MyEclipse Persistence Tools
 */

public class MobileMainPage  implements java.io.Serializable {


    // Fields    

     private Integer id;
     private String url;
     private String useState;
     private String state;


    // Constructors

    /** default constructor */
    public MobileMainPage() {
    }

    
    /** full constructor */
    public MobileMainPage(String url, String useState, String state) {
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