package com.vocketlist.android.listener;

import android.view.View;

/**
 * 리사이클뷰 : 클릭 리스너
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 3.
 */
public interface RecyclerViewItemClickListener {
    void onItemClick(View v, int position);
}
