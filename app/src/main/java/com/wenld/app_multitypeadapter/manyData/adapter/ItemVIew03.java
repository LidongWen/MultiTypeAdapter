package com.wenld.app_multitypeadapter.manyData.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wenld.app_multitypeadapter.R;
import com.wenld.app_multitypeadapter.manyData.bean.Bean03;
import com.wenld.multitypeadapter.base.MultiItemView;
import com.wenld.multitypeadapter.base.ViewHolder;
import com.wenld.multitypeadapter.utils.WrapperUtils;

/**
 * <p/>
 * Author: 温利东 on 2017/6/14 11:52.
 * http://www.jianshu.com/u/99f514ea81b3
 * github: https://github.com/LidongWen
 */

public class ItemVIew03 extends MultiItemView<Bean03> {

    @NonNull
    @Override
    public int getLayoutId() {
        return R.layout.item_three;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Bean03 item, int position) {
        holder.setText(R.id.tv_item03, item.title);
        ImageView iv = holder.getView(R.id.iv_item03);


        Glide.with(iv.getContext())
                .load(item.imgUrl)
//                .placeholder(R.mipmap.ic_launcher) // can also be a drawable
//                .error(R.mipmap.cheese) // will be displayed if the image cannot be loaded
                .centerCrop()
                .into(iv);

    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
        WrapperUtils.setFullSpan(viewHolder);
    }
}
