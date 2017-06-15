package com.wenld.app_multitypeadapter.manyData.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wenld.app_multitypeadapter.R;
import com.wenld.app_multitypeadapter.manyData.bean.Bean01;
import com.wenld.multitypeadapter.base.MultiItemView;
import com.wenld.multitypeadapter.base.ViewHolder;

/**
 * <p/>
 * Author: 温利东 on 2017/6/14 11:52.
 * http://www.jianshu.com/u/99f514ea81b3
 * github: https://github.com/LidongWen
 */

public class ItemVIew01 extends MultiItemView<Bean01,ViewHolder> {
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_one, parent, false);
        return new ViewHolder(inflater.getContext(),view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Bean01 item, int position) {
        holder.setText(R.id.tv_item01,item.title);
    }
}
