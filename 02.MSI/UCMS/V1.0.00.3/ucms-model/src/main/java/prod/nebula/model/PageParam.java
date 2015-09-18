/** 
 * Project: bbcvision3-model
 * author : PengSong
 * File Created at 2013-11-12 
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
package prod.nebula.model;

import java.io.Serializable;

import prod.nebula.commons.page.Page;



/** 
 * TODO Comment of PageParam 
 * 
 * @author PengSong 
 */
public class PageParam<T> implements Serializable {

	private static final long serialVersionUID = -6235950274570348862L;
	
	private T model;
	
	private Page page;
	
	private String orderBy;
	
	public PageParam(T model,Page page,String orderBy) {
		this.model = model;
		this.page = page;
		this.orderBy = orderBy;
	}

	public T getModel() {
		return model;
	}

	public void setModel(T model) {
		this.model = model;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
}
