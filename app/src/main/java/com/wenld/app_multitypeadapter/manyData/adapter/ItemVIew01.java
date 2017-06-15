package com.wenld.app_multitypeadapter.manyData.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wenld.app_multitypeadapter.R;
import com.wenld.app_multitypeadapter.manyData.bean.Bean01;
import com.wenld.multitypeadapter.MultiItemView;
import com.wenld.multitypeadapter.ViewHolder;

/**
 * <p/>
 * Author: 温利东 on 2017/6/14 11:52.
 * blog: http://blog.csdn.net/sinat_15877283
 * github: https://github.com/LidongWen
 */

public class ItemVIew01 extends MultiItemView<Bean01,ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_one, parent, false);
        return new ViewHolder(inflater.getContext(),view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Bean01 item, int position) {
        holder.setText(R.id.tv_item01,item.title);
    }
//    @Override
//    public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
//        ViewGroup.LayoutParams lp = viewHolder.itemView.getLayoutParams();
//        if (lp != null
//                && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
//            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
//            p.setFullSpan(true);
//        }
//    }
}
