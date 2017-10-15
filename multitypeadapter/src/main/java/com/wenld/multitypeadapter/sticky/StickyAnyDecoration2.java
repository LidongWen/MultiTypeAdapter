/*
 * Copyright 2014 Eduardo Barrenechea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wenld.multitypeadapter.sticky;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StickyAnyDecoration2 extends RecyclerView.ItemDecoration {

    private Map<Integer, RecyclerView.ViewHolder> mHeaderCache;
    private int curPosition;           //当前显示头部的 position
    private View curView;
    public Rect currentRect = new Rect();      // 当前位置信息

    private StickyHeaderAdapter mAdapter;


    private int gravity = Gravity.LEFT;

    public StickyAnyDecoration2(StickyHeaderAdapter adapter) {
        mAdapter = adapter;
        mHeaderCache = new HashMap<>();
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    private void clearHeaderCache() {
        mHeaderCache.clear();
    }

    private boolean isHeader(int position) {
        return mAdapter.isHeader(position);
    }


    int childCount;
    List<Integer> adapterPosList = new ArrayList<>();

    Rect boundDecoration = new Rect();

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        childCount = parent.getChildCount();
        // 将当前页面中的 headViewHolder找出来 并缓存起来
        cacheHeader(parent);
        if (mHeaderCache.size() < 1) {
            return;
        }
        // 计算当前显示的是哪个？ 且偏移多少 将位置信息放入 currentRect
        adapterPosList.clear();
        adapterPosList = new ArrayList(mHeaderCache.keySet());
        Collections.sort(adapterPosList, new Comparator<Integer>() {
            @Override
            public int compare(Integer e1, Integer e2) {
                return e1.compareTo(e2);
            }
        });

        // 第一个分组头部  y>0  不显示
        if (mHeaderCache.get(adapterPosList.get(0)).itemView.getTop() > 0) {
            return;
        }

        // 分组头部 高度小于 下一个分组头部 y 显示分组（偏移）

        // 拿到显示的分组 以及下一个分组
        int sencondPosition = -1;
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int pos = parent.getChildAdapterPosition(child);

            if (i == 0) {
                for (int j = adapterPosList.size() - 1; j >= 0; j--) {
                    if (pos >= adapterPosList.get(j)) {
                        curPosition = adapterPosList.get(j);
                        break;
                    }
                }
            } else if (isHeader(pos)) {
                sencondPosition = pos;
                break;
            }
        }

//        parent.getLayoutManager().calculateItemDecorationsForChild(mHeaderCache.get(curPosition).itemView, this.boundDecoration);
//        final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) mHeaderCache.get(curPosition).itemView.getLayoutParams();
//        final Rect insets = lp.mDecorInsets;
        parent.getLayoutManager().getDecoratedBoundsWithMargins(mHeaderCache.get(curPosition).itemView, this.boundDecoration);
        curView=mHeaderCache.get(curPosition).itemView;
        final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) curView.getLayoutParams();
        boundDecoration.left=curView.getLeft()-lp.leftMargin;
        boundDecoration.top=curView.getTop()-lp.topMargin;
//        outBounds.set(view.getLeft() - insets.left - lp.leftMargin,
//                view.getTop() - insets.top - lp.topMargin,
//                view.getRight() + insets.right + lp.rightMargin,
//                view.getBottom() + insets.bottom + lp.bottomMargin);
        if (sencondPosition == -1) {
            //显示一个分组
            currentRect.top = Math.max(parent.getPaddingTop(), 0) + boundDecoration.top;

        } else {
            int offset =curView.getMeasuredHeight() + parent.getPaddingTop()
                    - curView.getTop();

            if (offset > 0) {
                currentRect.top = Math.max(parent.getPaddingTop(), 0) + boundDecoration.top - offset;
            } else {
                currentRect.top = Math.max(parent.getPaddingTop(), 0) + boundDecoration.top;
            }
        }
        currentRect.left = Math.max(parent.getPaddingLeft(), 0) + boundDecoration.left;
        currentRect.right = currentRect.left + curView.getMeasuredWidth();
        currentRect.bottom =curView.getMeasuredHeight() + currentRect.top;

        // 画入;
        c.save();
        c.clipRect(currentRect);

        mHeaderCache.get(curPosition).itemView.draw(c);
        c.restore();
    }

    private void cacheHeader(RecyclerView parent) {
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final int pos = parent.getChildAdapterPosition(child);
            if (isHeader(pos)) {
                final RecyclerView.ViewHolder viewHolder = parent.findViewHolderForAdapterPosition(pos);
                mHeaderCache.put(pos, viewHolder);
            }
        }
    }

    public View findHeaderView(int x, int y) {
        if (currentRect.contains(x, y)) {
            return curView;
        }
        return null;
    }

}
