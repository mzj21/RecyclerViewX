package com.xing.xrecyclerview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class XItemDecoration extends RecyclerView.ItemDecoration {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private int mOrientation;
    private int mHeadNum;
    private int mSpanCount;
    private int mSpacing;
    private Paint mPaint;

    /**
     * RecyclerView的Grid分割线
     *
     * @param headNum      头部数量
     * @param spacing      分割线尺寸
     * @param dividerColor 分割线颜色
     */
    public XItemDecoration(int headNum, int spacing, int dividerColor) {
        mHeadNum = headNum;
        mSpacing = spacing;
        mPaint = new Paint();
        mPaint.setColor(dividerColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    private boolean isFootView(int childCount, int itemPosition) {
        return childCount - itemPosition - 1 == 0;
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            mSpanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
            if (mOrientation == VERTICAL) {
                int childCount = parent.getAdapter().getItemCount();
                if (itemPosition < mHeadNum) {
                    outRect.set(0, 0, 0, mSpacing);
                } else if (isFootView(childCount, itemPosition)) {
                    outRect.set(0, 0, 0, 0);
                } else {
                    int n_1 = (itemPosition - mHeadNum) % mSpanCount;
                    int n_2 = mSpanCount - 1 - (itemPosition - mHeadNum) % mSpanCount;
                    outRect.set(mSpacing * n_1 / mSpanCount, 0, mSpacing * n_2 / mSpanCount, mSpacing);
                }
            } else {
                int childCount = parent.getAdapter().getItemCount();
                if (itemPosition < mHeadNum) {
                    outRect.set(0, 0, mSpacing, 0);
                } else if (isFootView(childCount, itemPosition)) {
                    outRect.set(0, 0, 0, 0);
                } else {
                    int n_1 = (itemPosition - mHeadNum) % mSpanCount;
                    int n_2 = mSpanCount - 1 - (itemPosition - mHeadNum) % mSpanCount;
                    outRect.set(0, mSpacing * n_1 / mSpanCount, mSpacing, mSpacing * n_2 / mSpanCount);
                }
            }
        } else if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            if (itemPosition != parent.getAdapter().getItemCount() - 1) { // 过滤最后一位
                if (mOrientation == VERTICAL) {
                    outRect.set(0, 0, 0, mSpacing);
                } else {
                    outRect.set(0, 0, mSpacing, 0);
                }
            } else {
                outRect.set(0, 0, 0, 0);
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            if (((GridLayoutManager) parent.getLayoutManager()).getOrientation() == GridLayoutManager.VERTICAL) {
                mOrientation = VERTICAL;
                int childCount = parent.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    int left = child.getLeft();
                    int top = child.getTop();
                    int right = child.getRight();
                    int bottom = child.getBottom();
                    Rect rect_v = new Rect();
                    Rect rect_h = new Rect();
                    rect_v.set(right, top, right + mSpacing, bottom + mSpacing);
                    rect_h.set(left, bottom, right, bottom + mSpacing);
                    c.drawRect(rect_h, mPaint);
                    c.drawRect(rect_v, mPaint);
                }
            } else {
                mOrientation = HORIZONTAL;
                int childCount = parent.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    int left = child.getLeft();
                    int top = child.getTop();
                    int right = child.getRight();
                    int bottom = child.getBottom();
                    Rect rect_v = new Rect();
                    Rect rect_h = new Rect();
                    rect_v.set(right, top, right + mSpacing, bottom + mSpacing);
                    rect_h.set(left, bottom, right, bottom + mSpacing);
                    c.drawRect(rect_h, mPaint);
                    c.drawRect(rect_v, mPaint);
                }
            }
        } else if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) parent.getLayoutManager()).getOrientation() == LinearLayoutManager.VERTICAL) {
                mOrientation = VERTICAL;
                int childCount = parent.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    int left = child.getLeft();
                    int top = child.getTop();
                    int right = child.getRight();
                    int bottom = child.getBottom();
                    Rect rect_v = new Rect();
                    rect_v.set(left, bottom, right, bottom + mSpacing);
                    c.drawRect(rect_v, mPaint);
                }
            } else {
                mOrientation = HORIZONTAL;
                int childCount = parent.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    int left = child.getLeft();
                    int top = child.getTop();
                    int right = child.getRight();
                    int bottom = child.getBottom();
                    Rect rect_h = new Rect();
                    rect_h.set(right, top, right + mSpacing, bottom);
                    c.drawRect(rect_h, mPaint);
                }
            }
        }
    }
}