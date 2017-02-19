package com.vocketlist.android.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.vocketlist.android.R;
import com.vocketlist.android.adapter.viewholder.CommentViewHolder;
import com.vocketlist.android.dto.Comment;

import java.util.List;

/**
 * 어댑터 : 커뮤니티 : 댓글
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class CommentAdapter extends BaseAdapter<CommentViewHolder> {
    /**
     * 생성자
     * @param data
     */
    public CommentAdapter(List<Comment> data) {
        super(data);
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false));
    }
}
