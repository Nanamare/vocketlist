package com.vocketlist.android.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.vocketlist.android.R;
import com.vocketlist.android.adapter.viewholder.ListItemNotice;
import com.vocketlist.android.adapter.viewholder.NoticeHeaderViewHolder;
import com.vocketlist.android.adapter.viewholder.NoticeViewHolder;
import com.vocketlist.android.api.notice.NoticeModel;

import java.util.List;


/**
 * 어댑터 : 공지사항
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 4. 8.
 */
public class NoticeAdapter extends ExpandableRecyclerAdapter<ListItemNotice, NoticeModel.Notice, NoticeHeaderViewHolder, NoticeViewHolder> {

    /**
     * Primary constructor. Sets up {@link #mParentList} and {@link #mFlatItemList}.
     * <p>
     * Any changes to {@link #mParentList} should be made on the original instance, and notified via
     * {@link #notifyParentInserted(int)}
     * {@link #notifyParentRemoved(int)}
     * {@link #notifyParentChanged(int)}
     * {@link #notifyParentRangeInserted(int, int)}
     * {@link #notifyChildInserted(int, int)}
     * {@link #notifyChildRemoved(int, int)}
     * {@link #notifyChildChanged(int, int)}
     * methods and not the notify methods of RecyclerView.Adapter.
     *
     * @param parentList List of all parents to be displayed in the RecyclerView that this
     *                   adapter is linked to
     */
    public NoticeAdapter(@NonNull List<ListItemNotice> parentList) {
        super(parentList);
    }

    @NonNull
    @Override
    public NoticeHeaderViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        return new NoticeHeaderViewHolder(LayoutInflater.from(parentViewGroup.getContext()).inflate(R.layout.item_notice_header, parentViewGroup, false));
    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        return new NoticeViewHolder(LayoutInflater.from(childViewGroup.getContext()).inflate(R.layout.item_notice, childViewGroup, false));
    }

    @Override
    public void onBindParentViewHolder(@NonNull NoticeHeaderViewHolder parentViewHolder, int parentPosition, @NonNull ListItemNotice parent) {
        parentViewHolder.bind(parent);
    }

    @Override
    public void onBindChildViewHolder(@NonNull NoticeViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull NoticeModel.Notice child) {
        childViewHolder.bind(child);
    }
}
