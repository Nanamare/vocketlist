package com.vocketlist.android.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.vocketlist.android.R;
import com.vocketlist.android.adapter.viewholder.HelpHeaderViewHolder;
import com.vocketlist.android.adapter.viewholder.HelpViewHolder;
import com.vocketlist.android.adapter.viewholder.ListItemHelp;

import java.util.List;


/**
 * 어댑터 : 도움말
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 4. 8.
 */
public class HelpAdapter extends ExpandableRecyclerAdapter<ListItemHelp, String, HelpHeaderViewHolder, HelpViewHolder> {

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
    public HelpAdapter(@NonNull List<ListItemHelp> parentList) {
        super(parentList);
    }

    @NonNull
    @Override
    public HelpHeaderViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        return new HelpHeaderViewHolder(LayoutInflater.from(parentViewGroup.getContext()).inflate(R.layout.item_notice_header, parentViewGroup, false));
    }

    @NonNull
    @Override
    public HelpViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        return new HelpViewHolder(LayoutInflater.from(childViewGroup.getContext()).inflate(R.layout.item_help, childViewGroup, false));
    }

    @Override
    public void onBindParentViewHolder(@NonNull HelpHeaderViewHolder parentViewHolder, int parentPosition, @NonNull ListItemHelp parent) {
        parentViewHolder.bind(parent);
    }

    @Override
    public void onBindChildViewHolder(@NonNull HelpViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull String child) {
        childViewHolder.bind(child);
    }
}
