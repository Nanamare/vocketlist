package com.vocketlist.android.dto;

import java.io.Serializable;

/**
 * Created by kinamare on 2017-02-22.
 */
//봉사 상세페이지
public class VolunteerDetail implements Serializable {

	private String title;
	private String type;
	//format yyyy-MM-dd
	private String start_date;
	private String end_date;

	private String start_time;
	private String end_time;
	//사람 제한
	private int num_by_day;

	private String place;

	//format 월,화,수 쉼표로 스플릿해서 사용하거나 그냥 다 보여주거나
	private String activeDay;

	private String hostName;

	private String first_category;
	private String second_category;

	private String recruit_start_date;
	private String recruit_end_date;

	private String imgUrl;

	private boolean is_action;

	private String content;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public int getNum_by_day() {
		return num_by_day;
	}

	public void setNum_by_day(int num_by_day) {
		this.num_by_day = num_by_day;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getActiveDay() {
		return activeDay;
	}

	public void setActiveDay(String activeDay) {
		this.activeDay = activeDay;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getFirst_category() {
		return first_category;
	}

	public void setFirst_category(String first_category) {
		this.first_category = first_category;
	}

	public String getSecond_category() {
		return second_category;
	}

	public void setSecond_category(String second_category) {
		this.second_category = second_category;
	}

	public String getRecruit_start_date() {
		return recruit_start_date;
	}

	public void setRecruit_start_date(String recruit_start_date) {
		this.recruit_start_date = recruit_start_date;
	}

	public String getRecruit_end_date() {
		return recruit_end_date;
	}

	public void setRecruit_end_date(String recruit_end_date) {
		this.recruit_end_date = recruit_end_date;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public boolean is_action() {
		return is_action;
	}

	public void setIs_action(boolean is_action) {
		this.is_action = is_action;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
