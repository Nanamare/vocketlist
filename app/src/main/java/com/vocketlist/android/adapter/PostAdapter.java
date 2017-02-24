package com.vocketlist.android.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.vocketlist.android.R;
import com.vocketlist.android.adapter.viewholder.PostViewHolder;
import com.vocketlist.android.dto.Post;

import java.util.List;

/**
 * 어댑터 : 커뮤니티 : 글
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public class PostAdapter extends BaseAdapter<PostViewHolder> {

    /**
     * 생성자
     * @param data
     */
    public PostAdapter(List<Post> data) {
        super(data);
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false));
    }
}