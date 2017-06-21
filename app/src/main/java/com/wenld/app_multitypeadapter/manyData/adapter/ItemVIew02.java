package com.wenld.app_multitypeadapter.manyData.adapter;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.wenld.app_multitypeadapter.R;
import com.wenld.app_multitypeadapter.manyData.bean.Bean02;
import com.wenld.multitypeadapter.base.MultiItemView;
import com.wenld.multitypeadapter.base.ViewHolder;

/**
 * <p/>
 * Author: 温利东 on 2017/6/14 11:52.
 * http://www.jianshu.com/u/99f514ea81b3
 * github: https://github.com/LidongWen
 */

public class ItemVIew02 extends MultiItemView<Bean02> {
    @NonNull
    @Override
    public int getLayoutId() {
        return R.layout.item_two;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Bean02 item, int position) {
        holder.setText(R.id.tv_item02, item.title);
        ImageView iv = holder.getView(R.id.iv_item02);
        if (position  == 0 || position == 8 || position == 15|| position == 22 || position == 28|| position == 35){
            holder.setImageResource(R.id.iv_item02,R.mipmap.fairy);
        }else{
            holder.setImageResource(R.id.iv_item02,R.mipmap.she);
        }

//        Glide.with(iv.getContext())
//                .load(item.imgUrl)
////                .placeholder(R.mipmap.ic_launcher) // can also be a drawable
////                .error(R.mipmap.cheese) // will be displayed if the image cannot be loaded
//                .centerCrop()
//                .into(iv);

    }

}
