package pojo;

/**
 * UcsStreamResource entity. @author MyEclipse Persistence Tools
 */

public class UcsStreamResource implements java.io.Serializable {

	// Fields

	/**
	 *  @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	private Integer istreamId;
	private Integer ioutPutPort;
	private Integer ipidMaping;
	private Integer iprogramNum;
	private Integer ibitRate;
	private Integer iipqamnum;
	private String strNetRegionNum;
	private Integer ifreqPoint;
	private String strComment;
	private String strNavUrl;
	private Integer iisNeedKey;
	private String strMobileUrl;
	private String vodPageUrl;
	private Integer bindStatus;
	private Integer ichannelId;
	private Integer strWhetherHd;

	// Constructors

	/** default constructor */
	public UcsStreamResource() {
	}

	/** minimal constructor */
	public UcsStreamResource(Integer ioutPutPort, Integer ifreqPoint) {
		this.ioutPutPort = ioutPutPort;
		this.ifreqPoint = ifreqPoint;
	}

	/** full constructor */
	public UcsStreamResource(Integer ioutPutPort, Integer ipidMaping,
			Integer iprogramNum, Integer ibitRate, Integer iipqamnum,
			String strNetRegionNum, Integer ifreqPoint, String strComment,
			String strNavUrl, Integer iisNeedKey, String strMobileUrl,
			String vodPageUrl, Integer bindStatus, Integer ichannelId,
			Integer strWhetherHd) {
		this.ioutPutPort = ioutPutPort;
		this.ipidMaping = ipidMaping;
		this.iprogramNum = iprogramNum;
		this.ibitRate = ibitRate;
		this.iipqamnum = iipqamnum;
		this.strNetRegionNum = strNetRegionNum;
		this.ifreqPoint = ifreqPoint;
		this.strComment = strComment;
		this.strNavUrl = strNavUrl;
		this.iisNeedKey = iisNeedKey;
		this.strMobileUrl = strMobileUrl;
		this.vodPageUrl = vodPageUrl;
		this.bindStatus = bindStatus;
		this.ichannelId = ichannelId;
		this.strWhetherHd = strWhetherHd;
	}

	// Property accessors

	public Integer getIstreamId() {
		return this.istreamId;
	}

	public void setIstreamId(Integer istreamId) {
		this.istreamId = istreamId;
	}

	public Integer getIoutPutPort() {
		return this.ioutPutPort;
	}

	public void setIoutPutPort(Integer ioutPutPort) {
		this.ioutPutPort = ioutPutPort;
	}

	public Integer getIpidMaping() {
		return this.ipidMaping;
	}

	public void setIpidMaping(Integer ipidMaping) {
		this.ipidMaping = ipidMaping;
	}

	public Integer getIprogramNum() {
		return this.iprogramNum;
	}

	public void setIprogramNum(Integer iprogramNum) {
		this.iprogramNum = iprogramNum;
	}

	public Integer getIbitRate() {
		return this.ibitRate;
	}

	public void setIbitRate(Integer ibitRate) {
		this.ibitRate = ibitRate;
	}

	public Integer getIipqamnum() {
		return this.iipqamnum;
	}

	public void setIipqamnum(Integer iipqamnum) {
		this.iipqamnum = iipqamnum;
	}

	public String getStrNetRegionNum() {
		return this.strNetRegionNum;
	}

	public void setStrNetRegionNum(String strNetRegionNum) {
		this.strNetRegionNum = strNetRegionNum;
	}

	public Integer getIfreqPoint() {
		return this.ifreqPoint;
	}

	public void setIfreqPoint(Integer ifreqPoint) {
		this.ifreqPoint = ifreqPoint;
	}

	public String getStrComment() {
		return this.strComment;
	}

	public void setStrComment(String strComment) {
		this.strComment = strComment;
	}

	public String getStrNavUrl() {
		return this.strNavUrl;
	}

	public void setStrNavUrl(String strNavUrl) {
		this.strNavUrl = strNavUrl;
	}

	public Integer getIisNeedKey() {
		return this.iisNeedKey;
	}

	public void setIisNeedKey(Integer iisNeedKey) {
		this.iisNeedKey = iisNeedKey;
	}

	public String getStrMobileUrl() {
		return this.strMobileUrl;
	}

	public void setStrMobileUrl(String strMobileUrl) {
		this.strMobileUrl = strMobileUrl;
	}

	public String getVodPageUrl() {
		return this.vodPageUrl;
	}

	public void setVodPageUrl(String vodPageUrl) {
		this.vodPageUrl = vodPageUrl;
	}

	public Integer getBindStatus() {
		return this.bindStatus;
	}

	public void setBindStatus(Integer bindStatus) {
		this.bindStatus = bindStatus;
	}

	public Integer getIchannelId() {
		return this.ichannelId;
	}

	public void setIchannelId(Integer ichannelId) {
		this.ichannelId = ichannelId;
	}

	public Integer getStrWhetherHd() {
		return this.strWhetherHd;
	}

	public void setStrWhetherHd(Integer strWhetherHd) {
		this.strWhetherHd = strWhetherHd;
	}

}