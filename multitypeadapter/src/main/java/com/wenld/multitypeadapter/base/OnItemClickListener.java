package com.wenld.multitypeadapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * <p/>
 * Author: 温利东 on 2017/6/15 13:47.
 * http://www.jianshu.com/u/99f514ea81b3
 * github: https://github.com/LidongWen
 */


public interface OnItemClickListener<T> {
    void onItemClick(View view, RecyclerView.ViewHolder holder,T t, int position);

    boolean onItemLongClick(View view,  RecyclerView.ViewHolder holder,T t,  int position);
}
