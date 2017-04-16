package com.vocketlist.android.adapter;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.vocketlist.android.R;
import com.vocketlist.android.adapter.viewholder.CategoryViewHolder;
import com.vocketlist.android.listener.RecyclerViewItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 어댑터 : 관심분야
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 4. 16.
 */
public class CategoryGridAdapter extends BaseAdapter<CategoryViewHolder> {
    private RecyclerViewItemClickListener mListener;

    private SparseBooleanArray selectedItems;
    private List<String> mData;

    /**
     * 생성자
     *
     * @param data
     */
    public CategoryGridAdapter(List<String> data, RecyclerViewItemClickListener listener) {
        super(data);
        mData = data;
        mListener = listener;

        selectedItems = new SparseBooleanArray();
    }

    /* -----------------------------------------------------------------------------------
     * 선택항목
     * -------------------------------------------------------------------------------- */

    /**
     * 선택항목 초기화
     */
    public void clearSelections(){
        selectedItems.clear();
        notifyItemRangeChanged(0, mData.size());
    }

    /**
     * 선택항목 토글
     *
     * @param position
     */
    public void toggleSelection(int position) {
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
        notifyItemChanged(position);
    }

    /**
     * 선택항목
     * @param label
     * @param isSelected
     */
    public void setSelection(String label, boolean isSelected) {

        int position = -1;
        for(int i = 0; i < mData.size(); i++) {
            String s = mData.get(i);
            if(s.equals(label)) {
                position = i;
                break;
            }
        }

        if (isSelected) {
            selectedItems.put(position, true);
        }
        else {
            if (selectedItems.get(position, false)) selectedItems.delete(position);
        }

        notifyItemChanged(position);
    }

    /**
     * 선택항목들
     * @param labels
     * @param isSelected
     */
    public void setSelections(List<String> labels, boolean isSelected) {
        for(String label : labels) setSelection(label, isSelected);
    }


    /**
     * 선택항목들 얻기
     *
     * @return
     */
    public List<String> getSelectedItems() {
        List<String> items = new ArrayList(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(getItem(selectedItems.keyAt(i)));
        }
        return items;
    }

    /**
     * 선택여부
     *
     * @param position
     * @return
     */
    public boolean getSelectedItem(int position) {
        return selectedItems.get(position, false);
    }


    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.bind(getItem(position));
        holder.setChecked(getSelectedItem(position));
    }
}
