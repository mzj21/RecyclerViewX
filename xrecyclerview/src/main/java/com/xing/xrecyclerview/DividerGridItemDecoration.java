package com.xing.xrecyclerview;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;

public class DividerGridItemDecoration extends RecyclerView.ItemDecoration {
    private int mHeadNum;
    private int mSpanCount;
    private int mSpacing;
    private Drawable mDrawable;
    private boolean isHavaHeadDivider;

    /**
     * RecyclerView的Grid分割线
     *
     * @param headNum   头部数量
     * @param spanCount Grid中的列或行数
     * @param spacing   间距
     */
    public DividerGridItemDecoration(int headNum, int spanCount, int spacing, boolean isHavaHeadDivider) {
        mHeadNum = headNum;
        mSpanCount = spanCount;
        mSpacing = spacing;
        this.isHavaHeadDivider = isHavaHeadDivider;
    }

    private boolean getFirstH(int itemPosition) {
        return (itemPosition - mHeadNum) % mSpanCount == 0;
    }

    private boolean getSecondH(int itemPosition) {
        return (itemPosition - mHeadNum) % mSpanCount == 1;
    }

    private boolean getSecondLastH(int itemPosition) {
        return (itemPosition - mHeadNum) % mSpanCount == mSpanCount - 2;
    }

    private boolean getLastH(int itemPosition) {
        return (itemPosition - mHeadNum) % mSpanCount == mSpanCount - 1;
    }

    private boolean getLastV(int childCount, int itemPosition) {
        return childCount - itemPosition - mHeadNum < mSpanCount;
    }

    private int getBottomSpacing(int childCount, int itemPosition) {
        int bottomSpacing;
        if (getLastV(childCount, itemPosition)) {
            bottomSpacing = 0;
        } else {
            bottomSpacing = mSpacing;
        }
        return bottomSpacing;
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
        } else if (getFirstH(itemPosition)) {
            outRect.set(0, 0, mSpacing / mSpanCount * (mSpanCount - 1), getBottomSpacing(childCount, itemPosition));
        } else if (getSecondH(itemPosition)) {
            outRect.set(mSpacing / mSpanCount, 0, mSpacing / mSpanCount * (mSpanCount - 2), getBottomSpacing(childCount, itemPosition));
        } else if (getSecondLastH(itemPosition)) {
            outRect.set(mSpacing / mSpanCount * (mSpanCount - 2), 0, mSpacing / mSpanCount, getBottomSpacing(childCount, itemPosition));
        } else if (getLastH(itemPosition)) {
            outRect.set(mSpacing / mSpanCount * (mSpanCount - 1), 0, 0, getBottomSpacing(childCount, itemPosition));
        } else {
            outRect.set(mSpacing / 2, 0, mSpacing / 2, getBottomSpacing(childCount, itemPosition));
        }
    }
}