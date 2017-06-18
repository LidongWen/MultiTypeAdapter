package com.wenld.app_multitypeadapter.one2many.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wenld.app_multitypeadapter.R;
import com.wenld.app_multitypeadapter.one2many.Bean04;
import com.wenld.multitypeadapter.base.MultiItemView;
import com.wenld.multitypeadapter.base.ViewHolder;

/**
 * <p/>
 * Author: 温利东 on 2017/6/14 11:52.
 * http://www.jianshu.com/u/99f514ea81b3
 * github: https://github.com/LidongWen
 */

public class ItemVIew04 extends MultiItemView<Bean04> {


    @NonNull
    @Override
    public int getLayoutId() {
        return R.layout.item_four;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Bean04 item, int position) {
        holder.setText(R.id.tv_item04, item.title);
    }

    @Override
    public boolean isForViewType(Bean04 item, int postion) {
        if (Bean04.TYPE_ONE.equals(item.type)) {
            return true;
        }
        return false;
    }
}
