package com.vocketlist.android.api.comment.model;

import com.google.gson.annotations.SerializedName;
import com.vocketlist.android.dto.Link;

import java.util.List;

/**
 * Created by SeungTaek.Lim on 2017. 4. 8..
 */

public class CommentListModel {
    @SerializedName("data") public List<Comment> mCommentList;
    @SerializedName("links") public Link mLink;
    @SerializedName("page_size") public int mPageSize;
    @SerializedName("count") public int mCount;
    @SerializedName("page_current") public int mPageIndex;
    @SerializedName("page_count") public int mPageCount;

    public static class Comment {
        // todo 내용 채워야 함
    }
}
