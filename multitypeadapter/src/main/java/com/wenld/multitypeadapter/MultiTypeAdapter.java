package com.wenld.multitypeadapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wenld.multitypeadapter.base.ICoustomAdapter;
import com.wenld.multitypeadapter.base.MultiItemView;
import com.wenld.multitypeadapter.base.OnItemClickListener;
import com.wenld.multitypeadapter.base.TypePool;
import com.wenld.multitypeadapter.base.ViewHolder;

import java.util.List;

/**
 * <p/>
 * Author: 温利东 on 2017/6/14 10:26.
 * http://www.jianshu.com/u/99f514ea81b3
 * github: https://github.com/LidongWen
 */

public class MultiTypeAdapter extends RecyclerView.Adapter<ViewHolder> implements ICoustomAdapter {
    List<?> items;
    TypePool typePool;
    protected @Nullable
    LayoutInflater inflater;

    private OnItemClickListener onItemClickListener;

    public MultiTypeAdapter() {
        typePool = new TypePool();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        return typePool.getItemViewType(item);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        MultiItemView multiItemView = typePool.getMultiItemView(viewType);
        return new ViewHolder(inflater.getContext(), inflater.inflate(multiItemView.getLayoutId(), parent, false));
    }

    public <T> MultiTypeAdapter register(@NonNull Class<? extends T> clazz, @NonNull MultiItemView<T> multiItemView) {
        typePool.register(clazz, multiItemView);
        return this;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Object item = items.get(position);
        MultiItemView binder = typePool.getMultiItemView(holder.getItemViewType());
        binder.onBindViewHolder(holder, item, position);

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, holder, item, position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return onItemClickListener.onItemLongClick(v, holder, item, position);
                }
            });
        }
    }

    @NonNull
    public List<?> getItems() {
        return items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public MultiTypeAdapter setItems(List<?> items) {
        this.items = items;
        return this;
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        onViewAttachedToWindow(holder, holder.getLayoutPosition());
    }

    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder, int postion) {
        if (postion < getItemCount()) {
            typePool.getMultiItemView(typePool.getItemViewType(items.get(postion))).onViewAttachedToWindow(holder);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
