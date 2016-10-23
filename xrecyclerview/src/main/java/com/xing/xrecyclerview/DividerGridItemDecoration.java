package com.xing.xrecyclerview;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DividerGridItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;
    private int spanCount;          // 行数
    private int spacing;            // 间距
    private boolean includeEdge;   // 最边缘是否有间距

    /**
     * RecyclerView的Grid分割线
     *
     * @param spanCount   行数
     * @param spacing     间距
     * @param includeEdge 最边缘是否有间距
     */
    public DividerGridItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column
        if (includeEdge) {

            if (position < spanCount) {
                outRect.set(spacing - column * spacing / spanCount, spacing,
                        (column + 1) * spacing / spanCount, spacing);
            } else {
                outRect.set(spacing - column * spacing / spanCount, 0,
                        (column + 1) * spacing / spanCount, spacing);
            }
            if (position < spanCount) { // top edge
                outRect.top = spacing;
            }
            outRect.bottom = spacing; // item bottom
        } else {
            outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacing; // item top
            }
        }
    }
}