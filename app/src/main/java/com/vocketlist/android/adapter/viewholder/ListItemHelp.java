package com.vocketlist.android.adapter.viewholder;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.Arrays;
import java.util.List;


/**
 * 도우말
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 4. 8.
 */
public class ListItemHelp implements Parent<String> {

    private String title;
    private List<String> contents;

    /**
     * 생성자
     *
     * @param title
     * @param content
     */
    public ListItemHelp(String title, String content) {
        this.title = title;
        contents = Arrays.asList(content);
    }

    @Override
    public List<String> getChildList() {
        return contents;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public String getTitle() {
        return title;
    }
}
