package com.wenld.app_multitypeadapter.one2many.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wenld.app_multitypeadapter.R;
import com.wenld.app_multitypeadapter.one2many.Bean04;
import com.wenld.multitypeadapter.base.MultiItemView;
import com.wenld.multitypeadapter.base.ViewHolder;
import com.wenld.multitypeadapter.utils.WrapperUtils;

/**
 * <p/>
 * Author: 温利东 on 2017/6/14 11:52.
 * http://www.jianshu.com/u/99f514ea81b3
 * github: https://github.com/LidongWen
 */

public class ItemVIew05 extends MultiItemView<Bean04> {

    @NonNull
    @Override
    public int getLayoutId() {
        return R.layout.item_five;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Bean04 item, int position) {
        holder.setText(R.id.tv_item05, item.title);
    }

    @Override
    public boolean isForViewType(Bean04 item, int postion) {
        if (Bean04.TYPE_TWO.equals(item.type)) {
            return true;
        }
        return false;
    }
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
        WrapperUtils.setFullSpan(viewHolder);
    }
}
