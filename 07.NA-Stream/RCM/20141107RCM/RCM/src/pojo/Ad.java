

package pojo;

/**   
 * 类名称：Ad   
 * 类描述：  广告资源
 * 创建人：PengFei   
 * 创建时间：2014-10-26 下午11:47:03   
 * 备注：   
 * @version    
 *    
 */
public class Ad {
	
	
	     private Integer id;
	     private String name;   //上传的广告资源的名称
	     private String adVideoUrl;  //播放的广告资源地址

	     private String state;  //状态，0可以使用，1下架
	     private String upData ; 
	     
	     
	     
	     //资源上传时间

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAdVideoUrl() {
			return adVideoUrl;
		}

		public void setAdVideoUrl(String adVideoUrl) {
			this.adVideoUrl = adVideoUrl;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getUpData() {
			return upData;
		}

		public void setUpData(String upData) {
			this.upData = upData;
		}
	     
	     
	     
	     
	         
	     
	     
}
