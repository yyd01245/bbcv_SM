package prod.nebula.service.dto;

import java.util.List;

public interface UserManager {
        
	
	public UserInfo getUser(String keyword);
	
	public List<UserInfo> getUserList(String keyword);
	
	
	
	/*
	 * 
	 * 从stream_resource 表 中读取 vod_page_url 同步到   user_info表中 的 vod_page     目的：为手机二次登陆访问（详情页）
	 */
	public boolean updateVodPage(String username,String streamid,String vodpage);
}
