package com.vocketlist.android.dto;

import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 데이터 : 봉사활동리스트 : 더미
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class Volunteer implements Serializable {

	private String type; //관리자가 넣은것인지, 1365데이터인지
	private String title; //봉사활동이름
	private String start_date; // yyyy-MM-dd

	public String getRester_office() {
		return rester_office;
	}

	public void setRester_office(String rester_office) {
		this.rester_office = rester_office;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	private String rester_office; // 서울특별시 강남구
	private String imgUrl;

	public static Type getListType() {
		return new TypeToken<List<Volunteer>>() {
		}.getType();
	}

}
