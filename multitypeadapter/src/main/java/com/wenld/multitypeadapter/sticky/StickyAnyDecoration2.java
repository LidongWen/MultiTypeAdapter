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
import android.util.Log;
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

    public void clearHeaderCache() {
        mHeaderCache.clear();
    }

    private boolean isHeader(int position) {
        return mAdapter.isHeader(position);
    }


    int childCount;
    List<Integer> adapterHeaderPosList = new ArrayList<>();
    List<Integer> currentHeaderInPageLists = new ArrayList<>();
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
        adapterHeaderPosList.clear();
        currentHeaderInPageLists.clear();
        adapterHeaderPosList = new ArrayList(mHeaderCache.keySet());


        Collections.sort(adapterHeaderPosList, new Comparator<Integer>() {
            @Override
            public int compare(Integer e1, Integer e2) {
                return e1.compareTo(e2);
            }
        });

        // 分组头部 高度小于 下一个分组头部 y 显示分组（偏移）

        // 拿到显示的分组 以及下一个分组
        curPosition = -1;
        int sencondPosition = -1;
        // 最多拿三个
        findBreak:
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final int pos = parent.getChildAdapterPosition(child);
            if (i == 0) {
                for (int j = adapterHeaderPosList.size() - 1; j >= 0; j--) {
                    if (pos >= adapterHeaderPosList.get(j)) {
                        parent.getLayoutManager().getDecoratedBoundsWithMargins(mHeaderCache.get(adapterHeaderPosList.get(j)).itemView, this.boundDecoration);
                        if (boundDecoration.top <= 0) {
                            curPosition = adapterHeaderPosList.get(j);
                            break;
                        }
                    }
                }
            } else if (isHeader(pos)) {
                if (currentHeaderInPageLists.size() >= 2)
                    break;
                currentHeaderInPageLists.add(pos);
            }
        }
        if (curPosition == -1 && currentHeaderInPageLists.size() < 1)
            return;
        if (curPosition == -1) {
            curPosition = currentHeaderInPageLists.get(0);
            if (mHeaderCache.get(curPosition).itemView.getTop() >= 0)
                return;

            if (currentHeaderInPageLists.size() > 1) {
                sencondPosition = currentHeaderInPageLists.get(1);
            }
        } else {
            if(currentHeaderInPageLists.size() >1) {
                parent.getLayoutManager().getDecoratedBoundsWithMargins(mHeaderCache.get(currentHeaderInPageLists.get(0)).itemView, this.boundDecoration);
                if (boundDecoration.top < 0) {
                    curPosition = currentHeaderInPageLists.get(0);
                    if (currentHeaderInPageLists.size() > 1) {
                        sencondPosition = currentHeaderInPageLists.get(1);
                    }
                } else {
                    sencondPosition = currentHeaderInPageLists.get(0);
                }
            }
        }
        curView = mHeaderCache.get(curPosition).itemView;
        parent.getLayoutManager().getDecoratedBoundsWithMargins(curView, this.boundDecoration);
        final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) curView.getLayoutParams();
        boundDecoration.left = curView.getLeft() - lp.leftMargin;
        boundDecoration.top = curView.getTop() - lp.topMargin - boundDecoration.top;
//        outBounds.set(view.getLeft() - insets.left - lp.leftMargin,
//                view.getTop() - insets.top - lp.topMargin,
//                view.getRight() + insets.right + lp.rightMargin,
//                view.getBottom() + insets.bottom + lp.bottomMargin);
        Log.e(" sickyAnyDecoration ", String.format("curPosition: %s  sencondPosition:%s  left:%s top：%s", curPosition, sencondPosition, boundDecoration.left
                , boundDecoration.top));
        if (sencondPosition == -1) {
            //显示一个分组
            currentRect.top = Math.max(parent.getPaddingTop(), 0) + boundDecoration.top;

        } else {
            int offset = curView.getMeasuredHeight() + parent.getPaddingTop()
                    - mHeaderCache.get(sencondPosition).itemView.getTop();
            Log.e(" sickyAnyDecoration ", String.format("offset: %s", offset));

            if (offset > 0) {
                currentRect.top = Math.max(parent.getPaddingTop(), 0)  - offset;
            } else {
                currentRect.top = Math.max(parent.getPaddingTop(), 0) + boundDecoration.top;
            }
        }
        currentRect.left = boundDecoration.left;
        currentRect.right = currentRect.left + curView.getMeasuredWidth();
        currentRect.bottom = curView.getMeasuredHeight() + currentRect.top;

        // 画入;
        c.save();
        c.translate(currentRect.left, currentRect.top);
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
