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
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StickyAnyDecoration extends RecyclerView.ItemDecoration {

    private Map<Long, RecyclerView.ViewHolder> mHeaderCache;
    private StickyHeaderAdapter mAdapter;


    private int gravity = Gravity.LEFT;

    public StickyAnyDecoration(StickyHeaderAdapter adapter) {
        mAdapter = adapter;
        mHeaderCache = new HashMap<>();
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public void clearHeaderCache() {
        mHeaderCache.clear();
    }


    private boolean hasHeader(int position) {
        return mAdapter.isHeader(position);
    }

    List<Long> keyList;
    int lastMaxPos = -1;

    private int getThisOrLastHeaderPos(int position) {
        if (lastMaxPos < 0 || (lastMaxPos > -1 && lastMaxPos < position)) {
            keyList = new ArrayList(mHeaderCache.keySet());
            Collections.sort(keyList, new Comparator<Long>() {
                @Override
                public int compare(Long e1, Long e2) {
                    return e1.compareTo(e2);
                }
            });
        }
        if (keyList == null) {
            return -1;
        }
        for (int i = keyList.size() - 1; i >= 0; i--) {
            int s = keyList.get(i).intValue();
            if (position == s || position > s) {
                lastMaxPos = s;
                return s;
            }
        }
        return -1;
    }

    private RecyclerView.ViewHolder getHeader(RecyclerView parent, int position, ViewParams view) {
        if (position < 0)
            return null;
        long key = position;
        if (mHeaderCache.containsKey(key)) {
            RecyclerView.ViewHolder viewHolder = mHeaderCache.get(key);
            return viewHolder;
        } else {
            cacaheHeaderView(parent, position, view);
            return getHeader(parent, position, view);
        }
    }

    private void cacaheHeaderView(RecyclerView parent, int position, ViewParams view) {
        if (!mHeaderCache.containsKey(position)) {
            final RecyclerView.ViewHolder holder = mAdapter.onCreateHeaderViewHolder(parent, position);
            final View header = holder.itemView;
            mAdapter.onBindHeaderViewHolder(holder, position);

            int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(), View.MeasureSpec.UNSPECIFIED);
//
            int childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
                    parent.getPaddingLeft() + parent.getPaddingRight(), view.getMeasuredWidth());
            int childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
                    parent.getPaddingTop() + parent.getPaddingBottom(), view.getMeasuredHeight());
//            Log.e("headerParams", position + " " + view.getMeasuredWidth() + "  " + view.getMeasuredHeight());
            header.measure(childWidth, childHeight);
            header.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            mHeaderCache.put((long) position, holder);
        }
    }

    int childCount;
    List<ViewParams> viewList = new ArrayList<>();
    List<Integer> adapterPosList = new ArrayList<>();

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
//        if (true) return;
        childCount = parent.getChildCount();
        viewList.clear();
        adapterPosList.clear();
        calculationHeaderPosition(parent, childCount, viewList, adapterPosList);
        cacheHeaderAndsetLayoutParams(parent, viewList, adapterPosList);

        if (adapterPosList.size() < 1) {
            if (childCount >= 1) {
                final View child = parent.getChildAt(0);
                final int adapterPos = parent.getChildAdapterPosition(child);
                int lastHeaderPos = getThisOrLastHeaderPos(adapterPos);
                ViewParams viewParams = new ViewParams((int) child.getY(), child.getLeft(), child.getMeasuredWidth(), child.getMeasuredHeight());
                RecyclerView.ViewHolder viewHolder = getHeader(parent, lastHeaderPos, viewParams);
                if (viewHolder != null) {
                    int top = 0;
                    drawHeader(c, viewParams, viewHolder.itemView, top);
                    return;
                }
            }
        }
        for (int i = 0; i < adapterPosList.size() && i < 1; i++) {
            int thisHeaderPos = adapterPosList.get(i);
            ViewParams child = viewList.get(i);
            if (i == 0) {
                // 水平线下
                if (child.getY() > 0) {
                    int lastHeaderPos = getThisOrLastHeaderPos(thisHeaderPos - 1);
                    if (lastHeaderPos >= 0 && child.getY() > 0) {
                        View header = getHeader(parent, lastHeaderPos, viewList.get(i)).itemView;
                        int top = (int) (child.getY() - header.getHeight());
                        drawHeader(c, child, header, top);
                        return;
                    }
                } else if (child.getY() < 0) {  // 水平线上
                    for (int j = 1; j < adapterPosList.size() && i < 2; j++) {
                        //nextHeader's getY  > thisHeader .height
                        if (viewList.get(j).getY() > child.getMeasuredHeight()) {
                            View thisHeader = getHeader(parent, thisHeaderPos, child).itemView;
                            c.save();
                            int top = 0;
                            drawHeader(c, child, thisHeader, top);
                            return;
                        } else {        // nextHeader'getY  <  thisHeader .height
                            if (viewList.get(j).getY() == child.getY() + child.getMeasuredHeight()) {
                                return;
                            }
                            View thisHeader = getHeader(parent, thisHeaderPos, child).itemView;
                            c.save();
                            int top = (int) (viewList.get(j).getY() - child.getMeasuredHeight());
                            drawHeader(c, child, thisHeader, top);
                            return;
                        }
                    }
                    if (adapterPosList.size() < 2) {
                        View header = getHeader(parent, thisHeaderPos, child).itemView;
                        if (header != null) {
                            int top = 0;
                            drawHeader(c, child, header, top);
                            return;
                        }
                    }
                }
            }
        }
    }

    int size;
    int layoutPos;
    int adapterPos;

    private void calculationHeaderPosition(RecyclerView parent, int childCount,
                                           List<ViewParams> viewList,
                                           List<Integer> adapterPosList) {

        for (layoutPos = 0; layoutPos < childCount; layoutPos++) {
            final View child = parent.getChildAt(layoutPos);
            adapterPos = parent.getChildAdapterPosition(child);
            if (hasHeader(adapterPos)) {
                size = viewList.size();
                if (size > 0) {
                    if (child.getY() == viewList.get(size - 1).getY()) {
                        viewList.remove(size - 1);
                        adapterPosList.remove(size - 1);
//                        mHeaderCache.remove(adapterPos);
                    }
                }
                viewList.add(new ViewParams((int) child.getY(), child.getLeft(), child.getMeasuredWidth(), child.getMeasuredHeight()));
                adapterPosList.add(adapterPos);
//                if (viewList.size() > 1) {
//                    return;
//                }
            }
        }
    }

    private void cacheHeaderAndsetLayoutParams(RecyclerView parent,
                                               List<ViewParams> viewList,
                                               List<Integer> adapterPosList) {
        for (int i = 0; i < adapterPosList.size(); i++) {
            cacaheHeaderView(parent, adapterPosList.get(i), viewList.get(i));
        }
    }

    private void drawHeader(Canvas c, ViewParams child, View header, int top) {
        c.save();
        int left = 0;
        Region re;
        top = Math.min(top, 0);
        if (null != header.getTag() && (header.getTag() instanceof Region)) {
            re = (Region) header.getTag();
            left = re.left;
            re.top = top;
            re.bottom = top + header.getMeasuredHeight();
        } else {
            left = child.getLeft();
            re = new Region(left, top, left + header.getMeasuredWidth(), top + header.getMeasuredHeight());
        }
        c.translate(left, top);
        header.setTag(re);
        header.draw(c);
        c.restore();
    }

    public View findHeaderView(int x, int y) {
        for (Map.Entry<Long, RecyclerView.ViewHolder> entry : mHeaderCache.entrySet()) {
            if (entry.getValue().itemView.getTag() != null) {
                Region region = (Region) entry.getValue().itemView.getTag();
                if (x > region.left && x < region.right && y > region.top && y < region.bottom) {
//                    Log.e("region", entry.getKey() + "  " + region.left + "  " + region.right + "  " + region.top + "  " + region.bottom);
                    return entry.getValue().itemView;
                }
            }
        }
        return null;
    }

    public class Region {
        public int left;
        public int top;
        public int right;
        public int bottom;

        public Region(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }
    }

    public class ViewParams {
        public int y;
        public int measuredWigth;
        public int measuredHeight;
        public int left;

        public ViewParams(int y, int left, int measureWigth, int measureHeight) {
            this.y = y;
            this.left = left;
            this.measuredHeight = measureHeight;
            this.measuredWigth = measureWigth;
        }

        public int getY() {
            return y;
        }

        public int getLeft() {
            return left;
        }

        public int getMeasuredWidth() {
            return measuredWigth;
        }

        public int getMeasuredHeight() {
            return measuredHeight;
        }
    }

}
