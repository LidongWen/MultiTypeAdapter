package com.wenld.multitypeadapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.wenld.multitypeadapter.base.MultiItemView;
import com.wenld.multitypeadapter.base.ViewHolder;

/**
 * <p/>
 * Author: 温利东 on 2017/6/14 10:26.
 * http://www.jianshu.com/u/99f514ea81b3
 * github: https://github.com/LidongWen
 */

public abstract class CommonAdapter<T> extends MultiTypeAdapter {
    Context mContext;
    @LayoutRes
    int layoutId;


    public CommonAdapter(Context context, Class<? extends T> clazz, @LayoutRes final int layoutId) {
        super();
        mContext = context;
        this.layoutId = layoutId;
        register(clazz, new MultiItemView<T>() {

            @NonNull
            @Override
            public int getLayoutId() {
                return layoutId;
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, @NonNull T item, int position) {
                convert(holder, item, position);
            }
        });
    }


    protected abstract void convert(ViewHolder holder, T t, int position);

    @Override
    public int getItemCount() {
        return items.size();
    }

}
