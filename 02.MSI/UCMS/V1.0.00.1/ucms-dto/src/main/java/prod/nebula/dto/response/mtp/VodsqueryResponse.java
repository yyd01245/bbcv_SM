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
package prod.nebula.dto.response.mtp;

import java.util.List;

/** 
 * 根据地域和类别查询点播数据响应dto
 * 
 * @author PengSong 
 */
public class VodsqueryResponse extends BaseMtpResponse {
	
	private static final long serialVersionUID = 8186781311808832287L;


	public VodsqueryResponse() {
	}
	
	public VodsqueryResponse(String cmd,String sequence) {
		super.setCmd(cmd);
		super.setSequence(sequence);
	}
	/**
	 * 点播数据集合
	 */
	private List<VodsqueryResponseData> message;
	
	/**
	 * 记录总数
	 */
	private Integer total;
	
	
	public List<VodsqueryResponseData> getMessage() {
		return message;
	}

	public void setMessage(List<VodsqueryResponseData> message) {
		this.message = message;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
}
