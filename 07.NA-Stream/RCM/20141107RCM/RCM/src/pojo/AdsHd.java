package pojo;

/**
 * AdsHd entity. @author MyEclipse Persistence Tools
 */

public class AdsHd implements java.io.Serializable {

	// Fields

	/**
	 *  @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String state;
	private String pauseUsed;
	private String waitUsed;
	private String adUrl;
	private String name;

	// Constructors

	/** default constructor */
	public AdsHd() {
	}

	/** full constructor */
	public AdsHd(String state, String pauseUsed, String waitUsed, String adUrl,
			String name) {
		this.state = state;
		this.pauseUsed = pauseUsed;
		this.waitUsed = waitUsed;
		this.adUrl = adUrl;
		this.name = name;
	}

	// Property accessors

	
//	public String toString(){
//		
//		return 
//	}
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPauseUsed() {
		return this.pauseUsed;
	}

	public void setPauseUsed(String pauseUsed) {
		this.pauseUsed = pauseUsed;
	}

	public String getWaitUsed() {
		return this.waitUsed;
	}

	public void setWaitUsed(String waitUsed) {
		this.waitUsed = waitUsed;
	}

	public String getAdUrl() {
		return this.adUrl;
	}

	public void setAdUrl(String adUrl) {
		this.adUrl = adUrl;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}