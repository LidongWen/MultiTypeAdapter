package com.wenld.app_multitypeadapter.manyData.adapter;

import android.support.annotation.NonNull;
import android.util.Log;
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

public class ItemVIew01 extends MultiItemView<Bean01> {

    @NonNull
    @Override
    public int getLayoutId() {
        return R.layout.item_one;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Bean01 item, int position) {
        Log.e("ItemVIew01","onBindViewHolder  "+position);
        holder.setText(R.id.tv_item01,item.title);
    }
}