/** 
 * Project: msi-model
 * author : PengSong
 * File Created at 2013-12-2 
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
package prod.nebula.model.msi;

import prod.nebula.model.Model;

/** 
 * TODO Comment of Licence 
 * 
 * @author PengSong 
 */
public class Licence implements Model {

	private static final long serialVersionUID = 4463692000439596555L;
	
	private Integer id;
	
	private String app_name;
	
	private String licence;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getApp_name() {
		return app_name;
	}

	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}

	public String getLicence() {
		return licence;
	}

	public void setLicence(String licence) {
		this.licence = licence;
	}
}
