package pojo;

/**
 * RcmDictionary entity. @author MyEclipse Persistence Tools
 */

public class RcmDictionary implements java.io.Serializable {

	// Fields

	/**
	 *  @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String keytype;
	private Integer key;
	private String value;
	private String remark;
	private String state;

	// Constructors

	/** default constructor */
	public RcmDictionary() {
	}

	/** full constructor */
	public RcmDictionary(String keytype, Integer key, String value,
			String remark, String state) {
		this.keytype = keytype;
		this.key = key;
		this.value = value;
		this.remark = remark;
		this.state = state;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKeytype() {
		return this.keytype;
	}

	public void setKeytype(String keytype) {
		this.keytype = keytype;
	}

	public Integer getKey() {
		return this.key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

}