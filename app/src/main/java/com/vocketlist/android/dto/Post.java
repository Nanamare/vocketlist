package com.vocketlist.android.dto;

import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 데이터 : 커뮤니티 : 더미
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class Post implements Serializable {

	private int commnityIdx;
	private User user;
	private String title;
	private String content;
	private String voketType;
	private String shareToFbUrl;
	private String date;
	private String imgUrl;
	private List<Comment> comment;
	private int heartCnt;

	public int getCommnityIdx() {
		return commnityIdx;
	}

	public void setCommnityIdx(int commnityIdx) {
		this.commnityIdx = commnityIdx;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getVoketType() {
		return voketType;
	}

	public void setVoketType(String voketType) {
		this.voketType = voketType;
	}

	public String getShareToFbUrl() {
		return shareToFbUrl;
	}

	public void setShareToFbUrl(String shareToFbUrl) {
		this.shareToFbUrl = shareToFbUrl;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public List<Comment> getComment() {
		return comment;
	}

	public void setComment(List<Comment> comment) {
		this.comment = comment;
	}

	public int getHeartCnt() {
		return heartCnt;
	}

	public void setHeartCnt(int heartCnt) {
		this.heartCnt = heartCnt;
	}

	public static Type getListType() {
		return new TypeToken<List<Post>>() {
		}.getType();
	}
}
