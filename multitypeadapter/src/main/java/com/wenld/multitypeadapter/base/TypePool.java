package com.wenld.multitypeadapter.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.wenld.multitypeadapter.base.MultiItemView;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p/>
 * Author: 温利东 on 2017/6/14 11:09.
 * http://www.jianshu.com/u/99f514ea81b3
 * github: https://github.com/LidongWen
 * 帮助管理类型
 */

public class TypePool {
    private @NonNull
    final Map<Class<?>, CopyOnWriteArrayList<MultiItemView>> calss2ItemViewMap;
    final Map<Integer, MultiItemView> itemViewType2itemViewMap;
    final Map<MultiItemView, Integer> itemViewMap2itemViewType;

    public TypePool() {
        calss2ItemViewMap = new ConcurrentHashMap<>();
        itemViewType2itemViewMap = new ConcurrentHashMap<>();
        itemViewMap2itemViewType = new ConcurrentHashMap<>();
    }

    public <T> void register(@NonNull Class<? extends T> clazz, @NonNull MultiItemView<T> multiItemView) {
        CopyOnWriteArrayList<MultiItemView> list = calss2ItemViewMap.get(clazz);
        if (list == null) {
            list = new CopyOnWriteArrayList<>();
        }
        int size = itemViewType2itemViewMap.size();
        if (multiItemView.haveChild()) {
            list.addAll(multiItemView.getChildList());

            for (MultiItemView<T> tMultiItemView : multiItemView.getChildList()) {
                itemViewType2itemViewMap.put(size, tMultiItemView);
                itemViewMap2itemViewType.put(tMultiItemView, size);
                size++;
            }
        } else {
            list.add(multiItemView);
            itemViewType2itemViewMap.put(size, multiItemView);
            itemViewMap2itemViewType.put(multiItemView, size);
        }
        calss2ItemViewMap.put(clazz, list);
    }

    public <T> int getItemViewType(@NonNull T item) {

        Class<?> clazz = item.getClass();
        CopyOnWriteArrayList<MultiItemView> list = calss2ItemViewMap.get(clazz);
        for (MultiItemView multiItemView : list) {
            if (multiItemView.isForViewType(item, 0)) {
                return itemViewMap2itemViewType.get(multiItemView);
            }
        }
        return -1;
    }

    public MultiItemView getMultiItemView(int itemViewType) {
        return itemViewType2itemViewMap.get(itemViewType);
    }
}
