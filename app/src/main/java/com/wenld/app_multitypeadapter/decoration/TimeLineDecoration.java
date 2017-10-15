package com.wenld.app_multitypeadapter.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wenld.app_multitypeadapter.R;

/**
 * Created by wenld on 2017/6/24.
 */

public class TimeLineDecoration extends RecyclerView.ItemDecoration {
    private Context mContext;
    private int distance;

    Drawable drawable;
    Drawable verticalLine;
    Drawable horizontalLine;
    Rect mBounds = new Rect();
    Rect rect = new Rect(0, 0, 0, 0);

    public TimeLineDecoration(Context context, int distance) {
        mContext = context;
        this.distance = distance;
        verticalLine = ContextCompat.getDrawable(mContext, R.mipmap.gray_line);
        drawable = ContextCompat.getDrawable(mContext, R.mipmap.time);
        horizontalLine = ContextCompat.getDrawable(mContext, R.mipmap.horizontal_line);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = distance;
        outRect.right = distance;
        outRect.bottom = 3 * distance;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = distance;
        } else if (parent.getChildAdapterPosition(view) == 1) {
            outRect.top = 4 * distance;
        }
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {

        canvas.save();
        int top;
        int bottom;
        if (parent.getClipToPadding()) {
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            canvas.clipRect(parent.getPaddingLeft(), top, parent.getWidth() - parent.getPaddingRight(), bottom);
        } else {
            top = 0;
            bottom = parent.getHeight();
        }

        //画线
        int parentWidth = parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight();
        verticalLine.setBounds(parentWidth / 2 - 1, top, parentWidth / 2 + 1, bottom);
        verticalLine.draw(canvas);

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; ++i) {
            final View child = parent.getChildAt(i);
            parent.getLayoutManager().getDecoratedBoundsWithMargins(child, this.mBounds);
//            Log.e("mBounds", child.getX() + "   " + child.getTop() + "   " + child.getRight() + "   " +
//                    child.getBottom() + "   " + child.getWidth() + "   " + child.getHeight() + "   " + mBounds.toString());
            // 计算水平线摆放的位置、摆放
            if (parent.getChildAdapterPosition(child) % 2 == 0) {
                rect.left = child.getRight();
                rect.top = child.getTop() + child.getHeight() / 3;
                rect.right = mBounds.right;
                rect.bottom = rect.top + 1;
            } else if (parent.getChildAdapterPosition(child) % 2 == 1) {
                rect.left = parentWidth / 2;
                rect.top = child.getTop() + child.getHeight() / 3;
                rect.right = child.getLeft();
                rect.bottom = rect.top + 1;
            }
            horizontalLine.setBounds(rect);
            horizontalLine.draw(canvas);

            int w = drawable.getIntrinsicWidth();
            int h = drawable.getIntrinsicHeight();
            // 计算图片摆放位置、摆放
            rect.left = parentWidth / 2 - w / 2;
            rect.top = child.getTop() + child.getHeight() / 3 - h / 2;
            rect.right = rect.left + w;
            rect.bottom = rect.top + h;

            drawable.setBounds(rect);
            drawable.draw(canvas);
        }
        canvas.restore();
    }

    TextView header;

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (header == null) {
            header = new TextView(parent.getContext());
            header.setText("I am header!");
            header.setBackgroundResource(R.color.colorAccent);
            header.setTextColor(0xFFFFFFFF);

            int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.UNSPECIFIED);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(), View.MeasureSpec.UNSPECIFIED);
            int childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
                    parent.getPaddingLeft() + parent.getPaddingRight(), parent.getLayoutParams().width);
            int childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
                    parent.getPaddingTop() + parent.getPaddingBottom(), parent.getLayoutParams().height);

            header.measure(childWidth, childHeight);
            header.layout(0, 0, header.getMeasuredWidth(), header.getMeasuredHeight());
        }
        c.save();
        c.translate(10, 10);
        header.draw(c);
        c.restore();
    }
}