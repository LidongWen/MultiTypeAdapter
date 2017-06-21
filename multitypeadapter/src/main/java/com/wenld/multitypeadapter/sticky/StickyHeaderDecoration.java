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
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

public class StickyHeaderDecoration extends RecyclerView.ItemDecoration {

    private Map<Long, RecyclerView.ViewHolder> mHeaderCache;

    private StickyHeaderAdapter mAdapter;

    private boolean isImmersion;

    private int gravity = Gravity.LEFT;

    public StickyHeaderDecoration(StickyHeaderAdapter adapter) {
        this(adapter, false);
    }

    public StickyHeaderDecoration(StickyHeaderAdapter adapter, boolean isImmersion) {
        mAdapter = adapter;
        mHeaderCache = new HashMap<>();
        this.isImmersion = isImmersion;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);

        int headerHeight = 0;
        if (position != RecyclerView.NO_POSITION && hasHeader(position)) {
            View header = getHeader(parent, position).itemView;
            headerHeight = getHeaderHeightForLayout(header);
        }

        outRect.set(0, headerHeight, 0, 0);
    }


    public void clearHeaderCache() {
        mHeaderCache.clear();
    }


    private boolean hasHeader(int position) {
        return mAdapter.isHeader(position);
    }

    private int getThisOrLastHeaderPos(int position) {
        int hearderPos = -1;
        if (!hasHeader(position)) {
            for (int i = position - 1; i >= 0; i--) {
                if (hasHeader(i)) {
                    hearderPos = i;
                    break;
                }
            }
        } else {
            hearderPos = position;
        }
        return hearderPos;
    }

    private RecyclerView.ViewHolder getHeader(RecyclerView parent, int position) {
        long key = position;
        if (mHeaderCache.containsKey(key)) {
            return mHeaderCache.get(key);
        } else {
            final RecyclerView.ViewHolder holder = mAdapter.onCreateHeaderViewHolder(parent, position);
            final View header = holder.itemView;

            //noinspection unchecked
            mAdapter.onBindHeaderViewHolder(holder, position);

            int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(), View.MeasureSpec.UNSPECIFIED);

            int childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
                    parent.getPaddingLeft() + parent.getPaddingRight(), header.getLayoutParams().width);
            int childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
                    parent.getPaddingTop() + parent.getPaddingBottom(), header.getLayoutParams().height);

            header.measure(childWidth, childHeight);
            header.layout(0, 0, header.getMeasuredWidth(), header.getMeasuredHeight());

            mHeaderCache.put(key, holder);

            return holder;
        }
    }


    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        final int childCount = parent.getChildCount();

        for (int layoutPos = 0; layoutPos < childCount; layoutPos++) {
            final View child = parent.getChildAt(layoutPos);
            final int adapterPos = parent.getChildAdapterPosition(child);
            if (adapterPos != RecyclerView.NO_POSITION) {
                if (layoutPos == 0) {
                    int headerPos = getThisOrLastHeaderPos(adapterPos);
                    if (headerPos < 0) {
                        return;
                    }
                    View header = getHeader(parent, getThisOrLastHeaderPos(adapterPos)).itemView;
                    c.save();
                    int left = 0;
                    if (isImmersion) left = child.getLeft();
                    int top = getHeaderTop(parent, child, header, adapterPos, layoutPos);
                    c.translate(left, top);
                    header.setTag(new Region(left, top, left + header.getMeasuredWidth(), top + header.getMeasuredHeight()));
                    header.setTranslationX(left);
                    header.setTranslationY(top);
                    header.draw(c);
                    c.restore();
                } else if (hasHeader(adapterPos)) {
                    View header = getHeader(parent, adapterPos).itemView;
                    c.save();
                    int left = 0;
                    if (isImmersion) left = child.getLeft();
                    int top = getHeaderTop(parent, child, header, adapterPos, layoutPos);
                    c.translate(left, top);
                    header.setTranslationX(left);
                    header.setTranslationY(top);
                    header.setTag(new Region(left, top, left + header.getMeasuredWidth(), top + header.getMeasuredHeight()));
                    header.draw(c);
                    c.restore();
                }
            }
        }
    }

    private int getHeaderTop(RecyclerView parent, View child, View header, int adapterPos, int layoutPos) {
        int headerHeight = getHeaderHeightForLayout(header);
        int top = ((int) child.getY()) - headerHeight;
        if (layoutPos == 0) {

            final int count = parent.getChildCount();
            for (int i = 1; (i < count); i++) {
                int adapterPosHere = parent.getChildAdapterPosition(parent.getChildAt(i));
                if (adapterPosHere != RecyclerView.NO_POSITION && hasHeader(adapterPosHere)) {
                    final View next = parent.getChildAt(i);
                    final int offset = ((int) next.getY()) - (header.getHeight() + getHeaderHeightForLayout(getHeader(parent, adapterPosHere).itemView));
                    if (offset < 0) {
                        return offset;
                    }
                    break;
                }
            }
        }
        top = Math.max(0, top);
        return top;
    }

    private int getHeaderHeightForLayout(View header) {
        return isImmersion ? 0 : header.getHeight();
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

}
