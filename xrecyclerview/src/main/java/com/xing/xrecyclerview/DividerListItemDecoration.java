package com.xing.xrecyclerview;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class DividerListItemDecoration extends RecyclerView.ItemDecoration {
    public static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL = LinearLayoutManager.VERTICAL;
    private int mOrientation;
    private int mSpacing;
    private Drawable mDrawable;

    /**
     * RecyclerView的list分割线
     *
     * @param orientation  方向
     * @param spacing      间距
     * @param dividerColor 颜色
     */
    public DividerListItemDecoration(int orientation, int spacing, int dividerColor) {
        mOrientation = orientation;
        mSpacing = spacing;
        mDrawable = new ColorDrawable(dividerColor);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent) {
        if (mOrientation == VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount() - 1;
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView v = new RecyclerView(parent.getContext());
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mSpacing;
            mDrawable.setBounds(left, top, right, bottom);
            mDrawable.draw(c);
        }
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getRight() + params.rightMargin;
            int right = left + mSpacing;
            mDrawable.setBounds(left, top, right, bottom);
            mDrawable.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
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