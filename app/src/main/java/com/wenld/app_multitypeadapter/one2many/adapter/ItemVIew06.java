package com.wenld.app_multitypeadapter.one2many.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.wenld.app_multitypeadapter.one2many.Bean04;
import com.wenld.multitypeadapter.base.MultiItemView;
import com.wenld.multitypeadapter.base.ViewHolder;

/**
 * <p/>
 * Author: 温利东 on 2017/6/14 11:52.
 * http://www.jianshu.com/u/99f514ea81b3
 * github: https://github.com/LidongWen
 */

public class ItemVIew06 extends MultiItemView<Bean04> {
    public ItemVIew06() {
        super();
        addChildeItemView(new ItemVIew04());
        addChildeItemView(new ItemVIew05());
        addChildeItemView(new ItemVIew07());
    }

    @NonNull
    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Bean04 item, int position) {
//        holder.setText(R.id.tv_item05, item.title);
    }

    @Override
    public boolean isForViewType(Bean04 item, int postion) {
        if (Bean04.TYPE_TWO.equals(item.type)) {
            return true;
        }
        return false;
    }
}
