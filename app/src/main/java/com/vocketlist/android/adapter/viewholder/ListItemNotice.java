package com.vocketlist.android.adapter.viewholder;

import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.vocketlist.android.api.notice.NoticeModel;

import java.util.Arrays;
import java.util.List;


/**
 * 공지사항
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 4. 8.
 */
public class ListItemNotice implements Parent<NoticeModel.Notice> {

    private String title;
    private List<NoticeModel.Notice> notices;

    /**
     * 생성자
     *
     * @param notice
     */
    public ListItemNotice(NoticeModel.Notice notice) {
        title = notice.mTitle;
        notices = Arrays.asList(notice);
    }

    @Override
    public List<NoticeModel.Notice> getChildList() {
        return notices;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public String getTitle() {
        return title;
    }
}
