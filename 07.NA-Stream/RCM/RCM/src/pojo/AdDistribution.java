/**  
 * 类名称：Ad 
 * 类描述： 
 * 创建人：PengFei   
 * 创建时间：2014-10-26 下午11:47:03 
 */
package pojo;

/**   
 * 类名称：Ad   
 * 类描述：  广告资源分配
 * 创建人：PengFei   
 * 创建时间：2014-10-26 下午11:47:03   
 * 备注：   
 * @version    
 *    
 */
public class AdDistribution {
	
	
	     private Integer id;
	     private Integer adId;	     
	   
	    private String waitUsed;    //等待页是否使用，0未使用，1当前使用
	    private String pauseUsed;  //暂停页是否使用，0未使用，1当前使用
	    
	    private String resolution;   //高标清识别，0高清，1标清
	    private String state;  //状态，0可以使用，1下架
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public Integer getAdId() {
			return adId;
		}
		public void setAdId(Integer adId) {
			this.adId = adId;
		}
		public String getWaitUsed() {
			return waitUsed;
		}
		public void setWaitUsed(String waitUsed) {
			this.waitUsed = waitUsed;
		}
		public String getPauseUsed() {
			return pauseUsed;
		}
		public void setPauseUsed(String pauseUsed) {
			this.pauseUsed = pauseUsed;
		}
		public String getResolution() {
			return resolution;
		}
		public void setResolution(String resolution) {
			this.resolution = resolution;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
	    
	
	     
	     
}
