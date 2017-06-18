package com.wenld.multitypeadapter.base;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * Author: 温利东 on 2017/6/14 11:05.
 * blog: http://blog.csdn.net/sinat_15877283
 * github: https://github.com/LidongWen
 */

public abstract class MultiItemView<T> {
    private final List<MultiItemView<T>> list;

    public MultiItemView() {
        list = new ArrayList<>();
    }

    @NonNull
    public abstract @LayoutRes int getLayoutId();

    public abstract void onBindViewHolder(@NonNull ViewHolder holder, @NonNull T item, int position);

    public boolean isForViewType(T item, int postion) {
        return true;
    }

    public MultiItemView<T> addChildeItemView(MultiItemView<T> multiItemView) {
        list.add(multiItemView);
        return this;
    }

    public boolean haveChild() {
        if (list.isEmpty())
            return false;
        else
            return true;
    }

    public List<MultiItemView<T>> getChildList() {
        return list;
    }

    public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder){}
}
