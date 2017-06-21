package com.wenld.multitypeadapter.sticky;

import android.support.annotation.NonNull;
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
        ArrayList<RecyclerView.ItemDecoration> property = getItemDecorationsAndClearOld(recyclerView);
        findMyListernerAndClear(recyclerView);

        recyclerView.addItemDecoration(decor, property.size());
        recyclerView.addOnItemTouchListener(new MyOnItemToucherListerner() {
            boolean b = false;

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        View view = decor.findHeaderView((int) event.getX(), (int) event.getY());
                        if (view != null) {
                            b = true;
                            return true;
                        }
                    case MotionEvent.ACTION_UP:
                        View view2 = decor.findHeaderView((int) event.getX(), (int) event.getY());
                        if (b && view2 != null) {
                            b = false;
                            view2.performClick();
                            return true;
                        }
                        b = false;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_UP:
//                        View view2 = decor.findHeaderView((int) event.getX(), (int) event.getY());
//                        if (view2 != null) {
//                            view2.performClick();
//                        }
//                }
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    @NonNull
    public ArrayList<RecyclerView.ItemDecoration> getItemDecorationsAndClearOld(RecyclerView recyclerView) {
        ArrayList<RecyclerView.ItemDecoration> property = null;
        try {
            property = (ArrayList) getField(recyclerView, "mItemDecorations");
            for (Object o : property) {
                if (o instanceof StickyHeaderDecoration) {
                    recyclerView.removeItemDecoration((StickyHeaderDecoration) o);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return property;
    }

    private void findMyListernerAndClear(RecyclerView recyclerView) {

        ArrayList<?> property = null;
        try {
            property = (ArrayList) getField(recyclerView, "mOnItemTouchListeners");
            for (Object o : property) {
                if (o instanceof MyOnItemToucherListerner) {
                    recyclerView.removeOnItemTouchListener((MyOnItemToucherListerner) o);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void notifyDataSetChanged(RecyclerView recyclerView) {
        ArrayList<?> property = null;
        try {
            property = (ArrayList) getField(recyclerView, "mItemDecorations");
            for (Object o : property) {
                if (o instanceof StickyHeaderDecoration) {
                    ((StickyHeaderDecoration) o).clearHeaderCache();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object getField(Object owner, String fieldName) throws Exception {

        Class<?> ownerClass = owner.getClass();
        Field field = null;
        Object obj = new Object();
        try {
            Field[] fields = ownerClass.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                field = fields[i];
                if (field.getName().equals(fieldName)) {
                    field.setAccessible(true);
                    obj = field.get(owner);
                    break;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return obj;
    }

    private interface MyOnItemToucherListerner extends RecyclerView.OnItemTouchListener {

    }
}
