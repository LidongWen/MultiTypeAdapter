package com.wenld.multitypeadapter;

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

public abstract class MultiItemView<T, VH extends RecyclerView.ViewHolder> {
    private final List<MultiItemView<T, ? extends RecyclerView.ViewHolder>> list;

    public MultiItemView() {
        list = new ArrayList<>();
    }

    @NonNull
    protected abstract VH onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent);

    protected abstract void onBindViewHolder(@NonNull VH holder, @NonNull T item, int position);

    public boolean isForViewType(T item, int postion) {
        return true;
    }

    public MultiItemView<T, ? extends RecyclerView.ViewHolder> addChildeItemView(MultiItemView<T, ? extends RecyclerView.ViewHolder> multiItemView) {
        list.add(multiItemView);
        return (MultiItemView<T, ? extends RecyclerView.ViewHolder>) this;
    }

    public boolean haveChild() {
        if (list.isEmpty())
            return false;
        else
            return true;
    }

    public List<MultiItemView<T, ? extends RecyclerView.ViewHolder>> getChildList() {
        return list;
    }

    public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder){}
}
