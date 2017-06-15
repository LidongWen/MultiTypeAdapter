package com.wenld.app_multitypeadapter.manyData.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wenld.app_multitypeadapter.R;
import com.wenld.multitypeadapter.base.MultiItemView;
import com.wenld.multitypeadapter.base.ViewHolder;

/**
 * <p/>
 * Author: 温利东 on 2017/6/14 11:52.
 * http://www.jianshu.com/u/99f514ea81b3
 * github: https://github.com/LidongWen
 */

public class ItemVIewNormal extends MultiItemView<String, ViewHolder> {
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_go, parent, false);
        return new ViewHolder(inflater.getContext(), view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @NonNull String item, int position) {
        holder.setText(R.id.tv, item);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
        ViewGroup.LayoutParams lp = viewHolder.itemView.getLayoutParams();
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }
}
