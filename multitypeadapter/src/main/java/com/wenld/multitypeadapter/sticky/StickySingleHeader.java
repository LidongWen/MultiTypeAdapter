package com.wenld.multitypeadapter.sticky;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Author: 温利东 on 2017/6/17 14:24.
 * http://www.jianshu.com/u/99f514ea81b3
 * github: https://github.com/LidongWen
 */
public class StickySingleHeader {
    private StickyHeaderAdapter adapter;
    private RecyclerView recyclerView;
    private boolean isImmersion = false;
    private StickyHeaderDecoration decor;
    private int gravity = Gravity.LEFT;

    public StickySingleHeader() {
    }

    public StickySingleHeader adapter(StickyHeaderAdapter adapter) {
        this.adapter = adapter;
        return this;
    }

    public StickySingleHeader setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        return this;
    }

    public StickySingleHeader immersion() {
        isImmersion = true;
        return this;
    }

    /**
     * @param gravity Gravity.Left  Gravity.Right   Gravity.center
     * @return
     */
    private StickySingleHeader gravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    public void togo() {
        goSticky(adapter, recyclerView);
    }

    private void goSticky(StickyHeaderAdapter adapter, RecyclerView recyclerView) {
        if (adapter == null || recyclerView == null) {
            throw new NullPointerException("parameter is Null !  class "
                    + this.getClass().getName() + " methon" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }
        decor = new StickyHeaderDecoration(adapter, isImmersion);
        Class<?> ownerClass = recyclerView.getClass();
        Field field = null;
        ArrayList<RecyclerView.ItemDecoration> property = new ArrayList();
        try {
            Field[] fields = ownerClass.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                field = fields[i];
                if (field.getName().equals("mItemDecorations")) {
                    field.setAccessible(true);
                    break;
                }
            }
            if (property != null) {
                property = (ArrayList) field.get(recyclerView);
                for (RecyclerView.ItemDecoration o : property) {
                    if (o instanceof StickyHeaderDecoration) {
                        recyclerView.removeItemDecoration(o);
                        break;
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        recyclerView.addItemDecoration(decor, property.size());
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        View view = decor.findHeaderView((int) event.getX(), (int) event.getY());
                        if (view != null)
                            return true;
                    case MotionEvent.ACTION_UP:
                        View view2 = decor.findHeaderView((int) event.getX(), (int) event.getY());
                        if (view2 != null) {
                            view2.performClick();
                            return true;
                        }
                }
                return false;
            }
        });
    }

    public void notifyDataSetChanged(RecyclerView recyclerView) {
        Class<?> ownerClass = recyclerView.getClass();
        Field field = null;
        ArrayList<RecyclerView.ItemDecoration> property;
        try {
            Field[] fields = ownerClass.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                field = fields[i];
                if (field.getName().equals("mItemDecorations")) {
                    field.setAccessible(true);
                    break;
                }
            }
            property = (ArrayList) field.get(recyclerView);
            for (RecyclerView.ItemDecoration o : property) {
                if (o instanceof StickyHeaderDecoration) {
                    ((StickyHeaderDecoration) o).clearHeaderCache();
                    break;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
