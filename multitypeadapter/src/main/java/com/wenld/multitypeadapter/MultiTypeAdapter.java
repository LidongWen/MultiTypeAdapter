package com.wenld.multitypeadapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * <p/>
 * Author: 温利东 on 2017/6/14 10:26.
 * blog: http://blog.csdn.net/sinat_15877283
 * github: https://github.com/LidongWen
 */

public class MultiTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<?> items;
    TypePool typePool;
    protected @Nullable
    LayoutInflater inflater;

    public MultiTypeAdapter() {
        typePool = new TypePool();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        // 分不同数据类型  在得到不同的view类型
        return typePool.getItemViewType(item);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        // 分不同类型  创建不同类型viewHodler
        MultiItemView multiItemView = typePool.getMultiItemView(viewType);
        return multiItemView.onCreateViewHolder(inflater, parent);
    }

    public <T> void register(@NonNull Class<? extends T> clazz, @NonNull MultiItemView<T, ?> multiItemView) {
        typePool.register(clazz, multiItemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // 分布同类型 装配 ui
        Object item = items.get(position);
        MultiItemView binder = typePool.getMultiItemView(holder.getItemViewType());
        binder.onBindViewHolder(holder, item, position);
    }

    @NonNull
    public List<?> getItems() {
        return items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<?> items) {
        this.items = items;
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        typePool.getMultiItemView(typePool.getItemViewType(items.get(holder.getLayoutPosition()))).onViewAttachedToWindow(holder);
    }
}
