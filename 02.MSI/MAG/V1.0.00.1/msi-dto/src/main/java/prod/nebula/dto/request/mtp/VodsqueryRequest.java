/** 
 * Project: mtp-model
 * author : PengSong
 * File Created at 2013-10-31 
 *
 * Copyright bbcvision Corporation Limited. 
 * All rights reserved. 
 *
 * This software is the confidential and proprietary information of 
 * bbcvision company. ("Confidential Information"). You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with bbcvision.com. 
 */
package prod.nebula.dto.request.mtp;



/** 
 * 根据地域和类别查询点播数据请求dto
 * 
 * @author PengSong 
 */
public class VodsqueryRequest extends BaseMtpRequest {
	
	private static final long serialVersionUID = 617503950171527377L;

	/**
	 * 区域ID
	 */
	private Integer area_id;
	
	/**
	 * 栏目的id
	 */
	private Integer column_id;
	
	/**
	 * 当前页
	 */
	private Integer page_no;
	
	/**
	 * 页面大小
	 */
	private Integer page_size;

	public Integer getArea_id() {
		return area_id;
	}

	public void setArea_id(Integer area_id) {
		this.area_id = area_id;
	}

	public Integer getColumn_id() {
		return column_id;
	}

	public void setColumn_id(Integer column_id) {
		this.column_id = column_id;
	}

	public Integer getPage_no() {
		return page_no;
	}

	public void setPage_no(Integer page_no) {
		this.page_no = page_no;
	}

	public Integer getPage_size() {
		return page_size;
	}

	public void setPage_size(Integer page_size) {
		this.page_size = page_size;
	}
}
