/**  
 * 类名称：Info 
 * 类描述： 
 * 创建人：PengFei   
 * 创建时间：2014-9-23 下午05:50:50 
 */
package prod.nebula.update;

import prod.nebula.service.util.PropertiesUtil;

/**    
 * 类名称：Info   
 * 类描述：  保存影片信息 
 * 创建人：PengFei   
 * 创建时间：2014-9-23 下午05:50:50    
 * 备注：  将所有的信息保存在info中一个字段中
 * @version    
 *    
 */
public class Info {
   
	
	 private String name_id ;
	 private String vod_page_url = PropertiesUtil.readValue("vod_page_url")+"?name="+name_id;
	 private String info;
	 
	 
	public String getName_id() {
		return name_id;
	}
	public void setName_id(String nameId) {
		name_id = nameId;
	}
	public String getVod_page_url() {
		return vod_page_url;
	}
	public void setVod_page_url(String vodPageUrl) {
		vod_page_url = vodPageUrl;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	 
	 
	 
}
