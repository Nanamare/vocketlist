package com.vocketlist.android.dto;

import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 데이터 : 봉사활동 : 더미
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class Volunteer implements Serializable {

	public static Type getListType() {
		return new TypeToken<List<Volunteer>>() {
		}.getType();
	}

}
