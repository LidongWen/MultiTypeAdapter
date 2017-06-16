package com.wenld.multitypeadapter.base;

import android.support.v7.widget.RecyclerView;

/**
 * <p/>
 * Author: 温利东 on 2017/6/16 12:27.
 * blog: http://blog.csdn.net/sinat_15877283
 * github: https://github.com/LidongWen
 */

public interface ICoustomAdapter {
    void onViewAttachedToWindow(RecyclerView.ViewHolder holder, int postion);
}
