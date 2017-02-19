package com.vocketlist.android.adapter;

import android.support.v7.widget.RecyclerView;

import com.vocketlist.android.adapter.viewholder.BaseViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 어댑터 : 기본
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 14.
 */
public abstract class BaseAdapter<VH extends BaseViewHolder> extends RecyclerView.Adapter<VH>{
    private List<Serializable> mData;

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        if(mData != null) return mData.size();
        else return 0;
    }

    /**
     * 생성자
     * @param data
     */
    public <T extends Serializable> BaseAdapter(List<T> data) {
        this.mData = new ArrayList<>();
        addAll(data);
    }

    /**
     * 목록추가
     * @param data
     */
    public <T extends Serializable> void add(T data) {
        insert(data, mData.size());
    }

    /**
     * 단일추가
     * @param data
     */
    public <T extends Serializable> void addAll(List<T> data) {
        int startIndex = mData.size();
        mData.addAll(startIndex, data);
        notifyItemRangeInserted(startIndex, data.size());
    }

    /**
     * 단일삽입
     * @param data
     * @param position
     */
    public <T extends Serializable> void insert(T data, int position) {
        mData.add(position, data);
        notifyItemInserted(position);
    }

    /**
     * 일부삭제
     * @param position
     */
    public void remove(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 전체삭제
     */
    public void clear() {
        int size = mData.size();
        mData.clear();
        notifyItemRangeRemoved(0, size);
    }
}
