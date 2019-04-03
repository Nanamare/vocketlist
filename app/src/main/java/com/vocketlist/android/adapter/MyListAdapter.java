package com.vocketlist.android.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.vocketlist.android.R;
import com.vocketlist.android.adapter.viewholder.MyListViewHolder;
import com.vocketlist.android.api.my.MyListModel;
import com.vocketlist.android.listener.RecyclerViewItemClickListener;

import java.util.List;

/**
 * 어댑터 : 도움말
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 4. 22.
 */
public class MyListAdapter extends BaseAdapter<MyListModel.MyList, MyListViewHolder> {

    private RecyclerViewItemClickListener mListener;

    /**
     * 생성자
     *
     * @param data
     */
    public MyListAdapter(List<MyListModel.MyList> data, RecyclerViewItemClickListener listener) {
        super(data);
        mListener = listener;
    }

    @Override
    public MyListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_list, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(MyListViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    /**
     * 삭제
     *
     * @param id
     */
    public void removeWithId(int id) {
        int position = find(id);
        if (position != -1) remove(position);
    }

    /**
     * 수정
     *
     * @param data
     */
    public void change(MyListModel.MyList data) {
        int position = find(data);
        if (position != -1) change(position, data);
    }

    /**
     * 검색
     *
     * @param data
     * @return
     */
    public int find(MyListModel.MyList data) {
        return find(data.mId);
    }

    /**
     * 검색
     *
     * @param id
     * @return
     */
    public int find(int id) {
        int position = 1;
        for (MyListModel.MyList myList : mData) {
            if (myList.mId == id) {
                position = mData.indexOf(myList);
                break;
            }
        }

        return position;
    }
}
