package pojo;



/**
 * TvNavigate entity. @author MyEclipse Persistence Tools
 */

public class TvNavigate  implements java.io.Serializable {


    /**
	 *  @Fields serialVersionUID : TODO(��һ�仰�������������ʾʲô)
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	* @Fields ${field} : ${todo}(��һ�仰�������������ʾʲô)
	*/
     private Integer id;
     private String url;       //ģ���ַ
     private String useState;  //ʹ��״̬ ��1 ����ʹ�ã�0��û��ʹ��
 	/**
 	* @Fields ${field} : sdf${todo}(��һ�仰�������������ʾʲô)sdfsd
 	*/
     private String state;     //״̬��0���ԣ�1��������


    // Constructors

    /** default constructor */
    public TvNavigate() {
    }

    
    /** full constructor */
    public TvNavigate(String url, String useState, String state) {
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
    /**
     * @Description:  ģ���ַ���ɷ��ʵģ�
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public String getUseState() {
        return this.useState;
    }
    /**
     * @Description: ʹ��״̬��0δʹ�ã�1 ����ʹ����
     */
    public void setUseState(String useState) {
        this.useState = useState;
    }

    public String getState() {
        return this.state;
    }
    /**
    * @Description: ��Ч״̬��0��Ч״̬��1��Ч������ʹ�õģ�
    */
    public void setState(String state) {
        this.state = state;
    }
   








}