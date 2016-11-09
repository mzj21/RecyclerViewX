package com.xing.xrecyclerview;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DividerGridItemDecoration extends RecyclerView.ItemDecoration {
    private int mHeadNum;
    private int mSpanCount;
    private int mSpacing;
    private Drawable mDrawable;
    private boolean isHavaHeadDivider;

    /**
     * RecyclerView的Grid分割线
     *
     * @param headNum      头部数量
     * @param spanCount    Grid中的列或行数
     * @param spacing      间距
     * @param dividerColor 颜色
     */
    public DividerGridItemDecoration(int headNum, int spanCount, int spacing, int dividerColor, boolean isHavaHeadDivider) {
        mHeadNum = headNum;
        mSpanCount = spanCount;
        mSpacing = spacing;
        mDrawable = new ColorDrawable(dividerColor);
        this.isHavaHeadDivider = isHavaHeadDivider;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawVertical(c, parent);
        drawHorizontal(c, parent);
    }


    public void drawVertical(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount() - 1;
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getLeft() - params.leftMargin;
            int right = child.getRight() + params.rightMargin + mSpacing;
            int top = child.getBottom() + params.bottomMargin;
            int bottom;
            if (i < mHeadNum) {
                bottom = top + (isHavaHeadDivider ? mSpacing : 0);
            } else {
                bottom = top + mSpacing;
            }
            mDrawable.setBounds(left, top, right, bottom);
            mDrawable.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount() - 1;
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getTop() - params.topMargin;
            int bottom = child.getBottom() + params.bottomMargin;
            int left = child.getRight() + params.rightMargin;
            int right = left + mSpacing;
            mDrawable.setBounds(left, top, right, bottom);
            mDrawable.draw(c);
        }
    }

    private boolean getLastH(int childCount, int itemPosition) {
        childCount = childCount - childCount % mSpanCount;
        return itemPosition - 2 >= childCount;
    }

    private boolean getLastV(int itemPosition) {
        return (itemPosition - 1) % mSpanCount == 0;
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        int childCount = parent.getAdapter().getItemCount();
        if (itemPosition < mHeadNum) {
            if (isHavaHeadDivider) {
                outRect.set(0, 0, 0, mSpacing);
            } else {
                outRect.set(0, 0, 0, 0);
            }
        } else if (getLastH(childCount, itemPosition)) {
            outRect.set(0, 0, mSpacing, 0);
        } else if (getLastV(itemPosition)) {
            outRect.set(0, 0, 0, mSpacing);
        } else {
            outRect.set(0, 0, mSpacing, mSpacing);
        }

    }
}