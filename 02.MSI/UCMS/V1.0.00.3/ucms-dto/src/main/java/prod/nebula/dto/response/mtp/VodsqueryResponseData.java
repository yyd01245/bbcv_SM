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

import prod.nebula.dto.response.base.Response;

/** 
 * TODO Comment of VodsqueryResponseData 
 * 
 * @author PengSong 
 */
public class VodsqueryResponseData implements Response {
	
	private static final long serialVersionUID = -5851399073779776283L;

	/**
	 * 图标的地址
	 */
	private String icon1URI;
	
	/**
	 * 大图的地址
	 */
	private String icon2URI;
	
	/**
	 * 名称
	 */
	private String itemName;
	
	/**
	 * 栏目名称
	 */
	private String catalogName;
	
	/**
	 * 导演
	 */
	private String director;
	
	/**
	 * 主演
	 */
	private String starring;
	
	/**
	 * 时长
	 */
	private Integer length;
	
	/**
	 * 简介
	 */
	private String introduction;
	
	/**
	 * 地区
	 */
	private Integer area;
	
	/**
	 * 类型
	 */
	private String type;
	
	/**
	 * 上映年份
	 */
	private String year;
	
	/**
	 * 评分
	 */
	private String grade;
	
	/**
	 * 资源标识
	 */
	private Integer id;

	public String getIcon1URI() {
		return icon1URI;
	}

	public void setIcon1URI(String icon1uri) {
		icon1URI = icon1uri;
	}

	public String getIcon2URI() {
		return icon2URI;
	}

	public void setIcon2URI(String icon2uri) {
		icon2URI = icon2uri;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getCatalogName() {
		return catalogName;
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getStarring() {
		return starring;
	}

	public void setStarring(String starring) {
		this.starring = starring;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public Integer getArea() {
		return area;
	}

	public void setArea(Integer area) {
		this.area = area;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
