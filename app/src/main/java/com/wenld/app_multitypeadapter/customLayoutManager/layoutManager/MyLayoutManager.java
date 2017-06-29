package com.wenld.app_multitypeadapter.customLayoutManager.layoutManager;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wenld on 2017/6/25.
 */

public class MyLayoutManager extends RecyclerView.LayoutManager {
    LayoutState mLayoutState;

    public MyLayoutManager() {
        mLayoutState = new LayoutState();
    }

    //保存所有的Item的上下左右的偏移量信息
    private SparseArray<Rect> allItemFrames = new SparseArray<>();

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.getItemCount() == 0) {
            removeAndRecycleAllViews(recycler);
            return;
        }
        mLayoutState.reset();


        // 取到 最低点，
        // 取到第一点
        int height = getHeight() - getPaddingBottom();
        int first = getPaddingTop();
        //可用高度
        int remainingSpace = height - first;
        //  布局填满
        int viewHeight = 0;
        while (remainingSpace > 0 && mLayoutState.mCurrentPosition < state.getItemCount() - 1) {
            View view = mLayoutState.next(recycler);
            if (view == null) {
                break;
            }

            addView(view);
            measureChildWithMargins(view, 0, 0);
            viewHeight = measureViewHeight(view);

            int left, top, right, bottom;
            right = getWidth() - getPaddingRight();
            left = getPaddingLeft();
            top = first;
            bottom = top + viewHeight;
            Log.d("onLayoutChildren", "position:" + (mLayoutState.mCurrentPosition - 1) + " l: " + left + "  t: " + top + "  r: " + right + "  b: " + bottom);
            layoutDecoratedWithMargins(view, left, top, right, bottom);
            first += viewHeight;
            remainingSpace -= viewHeight;
        }
    }


    @Override
    public boolean canScrollVertically() {
        return true;
    }


    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getChildCount() == 0 || dy == 0) {
            return 0;
        }

        mLayoutState.reset();

        //实际要滑动的距离
        // 判断方向
        mLayoutState.mItemDirection = dy > 0 ? LayoutState.LAYOUT_END : LayoutState.LAYOUT_START;
        final int absDy = Math.abs(dy);
        //消耗
        int consumed = 0;
        //如果向上滑
        if (mLayoutState.mItemDirection == LayoutState.LAYOUT_END) {
            // 判断回收第一个
            recycleViewsFromStart(recycler, absDy);
            // 判断添加一个  判断最后一个 最低点坐标 是否大于高度
            final View child = getChildClosestToEnd();
            int endViewBotttom = getDecoratedEnd(child);
            mLayoutState.mCurrentPosition = getPosition(child) + mLayoutState.mItemDirection;

            int recyclerViewEndBound = getHeight() - getPaddingBottom();
            //可用高度
            int remainingSpace = absDy - (endViewBotttom - recyclerViewEndBound);

            if (remainingSpace < 0) {
                consumed = absDy;
            }

            int viewHeight = 0;
            while (remainingSpace > 0 && mLayoutState.mCurrentPosition < state.getItemCount() - 1) {
                // the direction in which we are traversing children
                View view = mLayoutState.next(recycler);
                if (view == null) {
                    break;
                }
                addView(view);
                measureChildWithMargins(view, 0, 0);
                viewHeight = measureViewHeight(view);

                int left, top, right, bottom;
                right = getWidth() - getPaddingRight();
                left = getPaddingLeft();
                top = endViewBotttom;
                bottom = top + viewHeight;
                Log.d("addView", "l: " + left + "  t: " + top + "  r: " + right + "  b: " + bottom);
                //最后坐标改变
                endViewBotttom += viewHeight;

                layoutDecoratedWithMargins(view, left, top, right, bottom);

                consumed += remainingSpace > remainingSpace ? remainingSpace : remainingSpace;
                remainingSpace -= viewHeight;
            }
        } else if (mLayoutState.mItemDirection == LayoutState.LAYOUT_START) {
            //如果向下滑
            // 判断回收最后一个
            recycleViewsFromEnd(recycler, absDy);
            // 判断添加到第一个

            final View child = getChildClosestToEnd();
            int endViewBotttom = getDecoratedStart(child);
            int recyclerViewEndBound = getPaddingTop();
            mLayoutState.mCurrentPosition = getPosition(child) + mLayoutState.mItemDirection;
            //可用高度
            int remainingSpace = absDy - recyclerViewEndBound - endViewBotttom;
            while (remainingSpace > 0) {
                int viewHeight;
                while (remainingSpace > 0 && mLayoutState.mCurrentPosition > -1) {

                    // the direction in which we are traversing children

                    View view = mLayoutState.next(recycler);
                    if (view == null) {
                        break;
                    }
                    addView(view, 0);
                    measureChildWithMargins(view, 0, 0);
                    viewHeight = measureViewHeight(view);

                    int left, top, right, bottom;
                    right = getWidth() - getPaddingRight();
                    left = getPaddingLeft();
                    top = endViewBotttom;
                    bottom = top + viewHeight;

                    //最后坐标改变
                    endViewBotttom += viewHeight;

                    layoutDecoratedWithMargins(view, left, top, right, bottom);

                    consumed += remainingSpace > remainingSpace ? remainingSpace : remainingSpace;
                    remainingSpace -= viewHeight;
                }
            }
        }
        final int scrolled = absDy > consumed ? mLayoutState.mItemDirection * consumed : dy;

        offsetChildrenVertical(-scrolled);
        Log.d("viewCount", getChildCount() + "");
        return scrolled;
    }

    private View getChildClosestToEnd() {
        return getChildAt(mLayoutState.mItemDirection == LayoutState.LAYOUT_START ? 0 : getChildCount() - 1);
    }

    private void recycleViewsFromEnd(RecyclerView.Recycler recycler, int dt) {
        if (dt < 0) {
            return;
        }
        final int limit = dt;
        final int childCount = getChildCount();

        for (int i = childCount - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (getDecoratedStart(child) < limit/*
                        || mOrientationHelper.getTransformedStartWithDecoration(child) < limit*/) {
                // stop here
                recycleChildren(recycler, childCount - 1, i);
                return;
            }
        }

    }

    private void recycleViewsFromStart(RecyclerView.Recycler recycler, int dt) {
        if (dt < 0) {
            return;
        }
        // ignore padding, ViewGroup may not clip children.
        final int limit = dt;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (getDecoratedEnd(child) > limit/*
                    || mOrientationHelper.getTransformedEndWithDecoration(child) > limit*/) {
                // stop here
                recycleChildren(recycler, 0, i);
                return;
            }
        }
    }

    /**
     * Recycles children between given indices.
     *
     * @param startIndex inclusive
     * @param endIndex   exclusive
     */
    private void recycleChildren(RecyclerView.Recycler recycler, int startIndex, int endIndex) {
        if (startIndex == endIndex) {
            return;
        }
        if (endIndex > startIndex) {
            for (int i = endIndex - 1; i >= startIndex; i--) {
                removeAndRecycleViewAt(i, recycler);
            }
        } else {
            for (int i = startIndex; i > endIndex; i--) {
                removeAndRecycleViewAt(i, recycler);
            }
        }
    }

    private int measureViewHeight(View view) {

        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        return getDecoratedMeasuredHeight(view) + params.topMargin
                + params.bottomMargin;
    }

    public int getDecoratedStart(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        return getDecoratedTop(view) - params.topMargin;
    }

    public int getDecoratedEnd(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        return getDecoratedBottom(view) + params.bottomMargin;
    }


    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    static class LayoutState {
        final static int LAYOUT_START = -1;
        final static int LAYOUT_END = 1;

        int mCurrentPosition = 0;
        int mItemDirection = LAYOUT_END;

        View next(RecyclerView.Recycler recycler) {
            final View view = recycler.getViewForPosition(mCurrentPosition);
            mCurrentPosition += mItemDirection * LAYOUT_END;
            return view;
        }

        void reset() {
            mCurrentPosition = 0;
            mItemDirection = LAYOUT_END;
        }
    }
}