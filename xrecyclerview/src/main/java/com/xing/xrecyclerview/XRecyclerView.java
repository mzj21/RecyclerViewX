package com.xing.xrecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class XRecyclerView extends RecyclerView {
    private static float DEF_FOOTVIEW_TEXTSIZE;
    private static int DEF_FOOTVIEW_TEXTCOLOR;
    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    public final static int TYPE_FOOTER = 1;    // 底部--往往是loading_more
    private float footview_textsize;
    private int footview_textcolor;
    private String footview_loading;
    private String footview_loaderror;
    private String footview_loadfinish;
    private boolean mIsFooterEnable = true;     // 是否允许加载更多
    private boolean mIsLoadMore = true;    // 调用是否允许加载更多
    private boolean mIsLoadingMore;             // 标记是否正在加载更多，防止再次调用加载更多接口
    private AutoLoadAdapter mAutoLoadAdapter;    // 自定义实现了头部和底部加载更多的adapter
    private int mLoadMorePosition;              // 标记加载更多的position
    private LoadMoreListener mLoadMoreListener; // 加载更多的监听-业务需要实现加载数据
    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private View FootView;
    private ProgressBar footer_progressbar;
    private TextView footer_hint_text;
    private AutoLoadAdapter.HeaderViewHolder headerViewHolder;
    private TypedArray ta;

    public XRecyclerView(Context context) {
        super(context, null);
    }

    public XRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ta = context.obtainStyledAttributes(attrs, R.styleable.XRecyclerView);
        DEF_FOOTVIEW_TEXTSIZE = getResources().getDimension(R.dimen.text_size_14sp);
        DEF_FOOTVIEW_TEXTCOLOR = getResources().getColor(R.color.color_cccccc);
        footview_textsize = ta.getDimension(R.styleable.XRecyclerView_xrv_footview_textsize, DEF_FOOTVIEW_TEXTSIZE);
        footview_textcolor = ta.getColor(R.styleable.XRecyclerView_xrv_footview_textcolor, DEF_FOOTVIEW_TEXTCOLOR);
        footview_loading = ta.getString(R.styleable.XRecyclerView_xrv_footview_loading);
        footview_loaderror = ta.getString(R.styleable.XRecyclerView_xrv_footview_loaderror);
        footview_loadfinish = ta.getString(R.styleable.XRecyclerView_xrv_footview_loadfinish);
        ta.recycle();
        init();
    }

    private void init() {
        super.addOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (getLayoutManager().canScrollVertically()) {
                    if (mLoadMoreListener != null && !mIsLoadingMore && dy > 0 && mIsFooterEnable) {
                        int lastVisiblePosition = getLastVisiblePosition();
                        if (lastVisiblePosition + 1 == mAutoLoadAdapter.getItemCount()) {
                            mLoadMorePosition = lastVisiblePosition;
                            setLoadingMore(true);
                            if (mIsLoadMore) {
                                mLoadMoreListener.onLoadMore();
                            }
                        }
                    }
                } else {
                    if (mLoadMoreListener != null && !mIsLoadingMore && dx > 0 && mIsFooterEnable) {
                        int lastVisiblePosition = getLastVisiblePosition();
                        if (lastVisiblePosition + 1 == mAutoLoadAdapter.getItemCount()) {
                            mLoadMorePosition = lastVisiblePosition;
                            setLoadingMore(true);
                            if (mIsLoadMore) {
                                mLoadMoreListener.onLoadMore();
                            }
                        }
                    }
                }
            }
        });
        FootView = View.inflate(getContext(), R.layout.view_foot, null);
        footer_hint_text = (TextView) FootView.findViewById(R.id.footer_hint_text);
        footer_progressbar = (ProgressBar) FootView.findViewById(R.id.footer_progressbar);
        footer_hint_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, footview_textsize);
        footer_hint_text.setTextColor(footview_textcolor);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        if (((LinearLayoutManager) layout).getOrientation() == LinearLayoutManager.VERTICAL) {
            FootView.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        } else {
            FootView.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }
    }

    /**
     * 设置加载更多的监听
     */
    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
    }

    /**
     * 设置是否正在可以加载更多
     */
    public void setLoadingMore(boolean loadingMore) {
        mIsLoadingMore = loadingMore;
    }

    /**
     * 设置是否正在可以加载更多
     */
    public void setLoadMore(boolean loadingMore) {
        mIsLoadMore = loadingMore;
    }

    /**
     * 加载更多监听
     */
    public interface LoadMoreListener {
        /**
         * 加载更多
         */
        void onLoadMore();
    }

    /**
     *
     */
    public class AutoLoadAdapter extends Adapter<ViewHolder> {

        /**
         * 数据adapter
         */
        private Adapter mInternalAdapter;

        AutoLoadAdapter(Adapter adapter) {
            mInternalAdapter = adapter;
        }

        @Override
        public int getItemViewType(int position) {
            if (isHeaderViewPos(position)) {
                return mHeaderViews.keyAt(position);
            } else if (position == getItemCount() - 1 && mIsFooterEnable) {
                return TYPE_FOOTER;
            }
            return mInternalAdapter.getItemViewType(position - getHeadersCount());
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            mInternalAdapter.onAttachedToRecyclerView(recyclerView);
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        int viewType = getItemViewType(position);
                        if (mHeaderViews.get(viewType) != null) {
                            return gridLayoutManager.getSpanCount();
                        } else if (position == getItemCount() - 1) {
                            return gridLayoutManager.getSpanCount();
                        }
                        return 1;
                    }
                });
                gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (mHeaderViews.get(viewType) != null) {
                return new HeaderViewHolder(mHeaderViews.get(viewType));
            }
            if (viewType == TYPE_FOOTER) {
                return new FooterViewHolder(FootView);
            } else {
                return mInternalAdapter.onCreateViewHolder(parent, viewType);
            }
        }

        class FooterViewHolder extends ViewHolder {
            FooterViewHolder(View itemView) {
                super(itemView);
            }
        }

        class HeaderViewHolder extends ViewHolder {
            HeaderViewHolder(View itemView) {
                super(itemView);
            }
        }

        private int getRealItemCount() {
            return mInternalAdapter.getItemCount();
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            int type = getItemViewType(position);
            if (isHeaderViewPos(position)) {
                return;
            }
            if (type == TYPE_FOOTER) {
                return;
            }
            mInternalAdapter.onBindViewHolder(holder, position - getHeadersCount());
        }

        @Override
        public int getItemCount() {
            return mInternalAdapter.getItemCount() + getHeadersCount() + 1;
        }
    }

    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    private boolean isHeaderViewPos(int position) {
        return position < getHeadersCount();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter != null) {
            mAutoLoadAdapter = new AutoLoadAdapter(adapter);
        }
        super.swapAdapter(mAutoLoadAdapter, true);
    }

    /**
     * 切换layoutManager
     */
    public void switchLayoutManager(LayoutManager layoutManager) {
        removeAllViews();
//        int firstVisiblePosition = getFirstVisiblePosition();
        setLayoutManager(layoutManager);
//        getLayoutManager().scrollToPosition(firstVisiblePosition);
    }

    /**
     * 获取第一条展示的位置
     */
    private int getFirstVisiblePosition() {
        int position;
        if (getLayoutManager() instanceof GridLayoutManager) {
            position = ((GridLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        } else if (getLayoutManager() instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getLayoutManager();
            int[] lastPositions = layoutManager.findFirstVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMinPositions(lastPositions);
        } else {
            position = 0;
        }
        return position;
    }

    /**
     * 获得当前展示最小的position
     */
    private int getMinPositions(int[] positions) {
        int minPosition = Integer.MAX_VALUE;
        for (int position : positions) {
            minPosition = Math.min(minPosition, position);
        }
        return minPosition;
    }

    /**
     * 获取最后一条展示的位置
     */
    private int getLastVisiblePosition() {
        int position;
        if (getLayoutManager() instanceof GridLayoutManager) {
            position = ((GridLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        } else if (getLayoutManager() instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getLayoutManager();
            int[] lastPositions = layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMaxPosition(lastPositions);
        } else {
            position = getLayoutManager().getItemCount() - 1;
        }
        return position;
    }

    /**
     * 获得最大的位置
     */
    private int getMaxPosition(int[] positions) {
        int maxPosition = Integer.MIN_VALUE;
        for (int position : positions) {
            maxPosition = Math.max(maxPosition, position);
        }
        return maxPosition;
    }

    /**
     * 添加头部view
     */
    public void addHeaderView(View view) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }

    /**
     * 移除view
     */
    public void removeHeaderView(int index) {
        mHeaderViews.delete(index + BASE_ITEM_TYPE_HEADER);
    }

    /**
     * 移除view
     */
    public void removeAllHeaderView() {
        mHeaderViews.clear();
    }

    public int getHeadViewSize() {
        if (mHeaderViews == null) {
            return 0;
        }
        return mHeaderViews.size();
    }


    /**
     * 设置是否支持自动加载更多
     */
    private void setAutoLoadMoreEnable(boolean autoLoadMore) {
        mIsFooterEnable = autoLoadMore;
    }

    /**
     * 删除指定item
     *
     * @param position 位置
     */
    public void notifyItemRemoved(int position) {
        getAdapter().notifyItemRemoved(position);
    }

    /**
     * 刷新指定item
     *
     * @param position 位置
     */
    public void notifyItemChanged(int position) {
        getAdapter().notifyItemChanged(position);
    }

    /**
     * 加载更多成功，且还有更多
     */
    public void setFootNormal() {
        setAutoLoadMoreEnable(true);
        setLoadingMore(false);
        setLoadMore(true);

        FootView.setVisibility(View.VISIBLE);
        footer_progressbar.setVisibility(View.VISIBLE);
        footer_hint_text.setVisibility(View.VISIBLE);
        footer_hint_text.setText(TextUtils.isEmpty(footview_loading) ? getResources().getString(R.string.footer_hint_loading) : footview_loading);
        FootView.setOnClickListener(null);
    }

    /**
     * 加载更多失败
     */
    public void setFootError() {
        setAutoLoadMoreEnable(true);
        setLoadingMore(false);
        setLoadMore(false);

        FootView.setVisibility(View.VISIBLE);
        footer_progressbar.setVisibility(View.INVISIBLE);
        footer_hint_text.setVisibility(View.VISIBLE);
        footer_hint_text.setText(TextUtils.isEmpty(footview_loaderror) ? getResources().getString(R.string.footer_hint_load_error) : footview_loaderror);
        FootView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadMoreListener.onLoadMore();
                setAutoLoadMoreEnable(true);
                setLoadingMore(false);
                setLoadMore(true);
                footer_progressbar.setVisibility(View.VISIBLE);
                footer_hint_text.setText(TextUtils.isEmpty(footview_loading) ? getResources().getString(R.string.footer_hint_loading) : footview_loading);
            }
        });
    }

    /**
     * 没有更多了
     */
    public void setFootNoMore() {
        setFootNoMore(null);
    }

    /**
     * 没有更多了
     *
     * @param onClickListener 监听
     */
    public void setFootNoMore(OnClickListener onClickListener) {
        setAutoLoadMoreEnable(true);
        setLoadingMore(false);
        setLoadMore(false);

        footer_progressbar.setVisibility(View.INVISIBLE);
        FootView.setVisibility(View.VISIBLE);
        footer_hint_text.setVisibility(View.VISIBLE);
        footer_hint_text.setText(TextUtils.isEmpty(footview_loadfinish) ? getResources().getString(R.string.footer_hint_load_finish) : footview_loadfinish);
        FootView.setOnClickListener(onClickListener);
    }

    /**
     * 没有更多了,且FootView隐藏
     */
    public void setFootNone() {
        setAutoLoadMoreEnable(true);
        setLoadingMore(false);
        setLoadMore(false);

        FootView.setVisibility(View.GONE);
        FootView.setOnClickListener(null);
    }

    /**
     * 获取XRecyclerView的高度
     *
     * @param dividerHeight 分割线高度
     * @return XRecyclerView的高度
     */
    public int getScollYDistance(int dividerHeight) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight() + dividerHeight;
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }

    /**
     * 获取XRecyclerView的宽度
     *
     * @param dividerWidth 分割线宽度
     * @return XRecyclerView的宽度
     */
    public int getScollXDistance(int dividerWidth) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemWidth = firstVisiableChildView.getWidth() + dividerWidth;
        return (position) * itemWidth - firstVisiableChildView.getLeft();
    }
}