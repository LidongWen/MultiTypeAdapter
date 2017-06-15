package com.wenld.multitypeadapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p/>
 * Author: 温利东 on 2017/6/14 11:09.
 * blog: http://blog.csdn.net/sinat_15877283
 * github: https://github.com/LidongWen
 * 帮助管理类型
 */

public class TypePool {
    private @NonNull
//    final List<Class<?>> classes;
    final Map<Class<?>, CopyOnWriteArrayList<MultiItemView>> calss2ItemViewMap;
    final Map<Integer, MultiItemView> itemViewType2itemViewMap;
    final Map<MultiItemView, Integer> itemViewMap2itemViewType;
//    private @NonNull
//    final List<MultiItemView<?, ?>> itemViews;

    public TypePool() {
        calss2ItemViewMap = new ConcurrentHashMap<>();
        itemViewType2itemViewMap = new ConcurrentHashMap<>();
        itemViewMap2itemViewType = new ConcurrentHashMap<>();
    }

    public <T> void register(@NonNull Class<? extends T> clazz, @NonNull MultiItemView<T, ?> multiItemView) {
        CopyOnWriteArrayList<MultiItemView> list = calss2ItemViewMap.get(clazz);
        if (list == null) {
            list = new CopyOnWriteArrayList<>();
        }
        int size = itemViewType2itemViewMap.size();
        if (multiItemView.haveChild()) {
            list.addAll(multiItemView.getChildList());

            for (MultiItemView<T, ? extends RecyclerView.ViewHolder> tMultiItemView : multiItemView.getChildList()) {
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
//        classes.add(clazz);
//        itemViews.add(multiItemView);
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
//        int index = classes.indexOf(clazz);
//        if (index != -1) {
//            return index;
//        }
//        for (int i = 0; i < classes.size(); i++) {
//            if (classes.get(i).isAssignableFrom(clazz)) {
//                return i;
//            }
//        }
//        return -1;
    }

    public MultiItemView getMultiItemView(int itemViewType) {
        return itemViewType2itemViewMap.get(itemViewType);
    }
//
//    @NonNull
//    public List<MultiItemView<?, ?>> getItemViews() {
//        return itemViews;
//    }
}
