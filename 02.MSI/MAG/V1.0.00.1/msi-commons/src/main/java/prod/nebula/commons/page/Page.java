/** 
 * Project: bbcvision3-commons
 * author : PengSong
 * File Created at 2013-11-5 
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
package prod.nebula.commons.page;

/** 
 * TODO Comment of Page 
 * 
 * @author PengSong 
 */
public class Page {
	/**
	 * 分页,默认每页记录数
	 */
	public static final Integer defaultPageSize = 100;
	
	/**
	 * 分页,每页最多记录数
	 */
	public static final Integer maxPageSize = 500;
	
	/**
	 * 记录总条数
	 */
	private int totalCount;
	
	/**
	 * 每页记录数
	 */
	private int pageSize = defaultPageSize;
	
	/**
	 * 第几页
	 */
	private int pageNumber = 1;
	
	/**
	 * 总页数
	 */
	private int totalPage;
	
	/**
	 * 开始查询记录的位置
	 */
	private int firstIndex;
	
	public Page() {
	}

	public Page(int pageSize,int pageNumber) {
		this.pageSize = pageSize;
		this.pageNumber = pageNumber;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		totalPage = getTotalPage();
	}

	public int getPageNumber() {
		if (pageNumber<=0)
			return 1;
		else if (pageNumber > getTotalPage()){
			return getTotalPage() <= 0?1:getTotalPage();
		}
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getTotalPage() {
		return getTotalCount() % getPageSize() > 0 ? getTotalCount() / getPageSize() + 1:getTotalCount() / getPageSize();
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getPageSize() {
		return (pageSize <= 0 || pageSize > maxPageSize) ?defaultPageSize : pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	/**
	 * 获取起始位置索引编号
	 * @return
	 */
	public int getFirstIndex() {
		firstIndex = (firstIndex <= 0) ? (getPageNumber()-1)*getPageSize() : firstIndex;
		return firstIndex;
	}

	public void setFirstIndex(int firstIndex) {
		this.firstIndex = firstIndex;
	}
	
	/**
	 * 获取结束位置索引编号
	 * @return
	 */
	public int getEndIndex(){
		return getFirstIndex() + getPageSize() - 1;
	}
}
