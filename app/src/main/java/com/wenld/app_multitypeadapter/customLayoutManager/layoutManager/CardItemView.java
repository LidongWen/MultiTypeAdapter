package com.wenld.app_multitypeadapter.customLayoutManager.layoutManager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.wenld.app_multitypeadapter.R;

/**
 * Created by wenld on 2017/6/25.
 */

public class CardItemView extends View {

    private int mSize;
    private Paint mPaint;
    private Path mDrawPath;
    private Region mRegion;

    public CardItemView(Context context) {
        this(context, null, 0);
    }

    public CardItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Card, defStyleAttr, 0);
        mSize = ta.getDimensionPixelSize(R.styleable.Card_size, 10);
        mPaint.setColor(ta.getColor(R.styleable.Card_bgColor, 0));
        ta.recycle();

        mRegion = new Region();
        mDrawPath = new Path();

        mDrawPath.moveTo(0, mSize / 2);
        mDrawPath.lineTo(mSize / 2, 0);
        mDrawPath.lineTo(mSize, mSize / 2);
        mDrawPath.lineTo(mSize / 2, mSize);
        mDrawPath.close();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mSize, mSize);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!isEventInPath(event)) { return false;}
        }

        return super.dispatchTouchEvent(event);
    }

    private boolean isEventInPath(MotionEvent event) {
        RectF bounds = new RectF();
        mDrawPath.computeBounds(bounds, true);
        mRegion.setPath(mDrawPath, new Region((int)bounds.left,
                (int)bounds.top, (int)bounds.right, (int)bounds.bottom));
        return mRegion.contains((int) event.getX(), (int) event.getY());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawPath(mDrawPath, mPaint);
    }

    public void setCardColor(int color) {
        mPaint.setColor(color);
        invalidate();
    }
}