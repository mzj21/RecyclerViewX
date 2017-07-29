package com.xing.xrecyclerviewsample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    private Context mContext;
    private List<String> datas;
    private LayoutInflater mLayoutInflater;
    private int HeadViewsNum;
    private RelativeLayout.LayoutParams params;

    public RecyclerViewAdapter(Context context, List<String> datas, int HeadViewsNum, int orientation) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.datas = datas;
        this.HeadViewsNum = HeadViewsNum;
        params = new RelativeLayout.LayoutParams(orientation == 1 ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT,
                orientation == 1 ? ViewGroup.LayoutParams.WRAP_CONTENT : ViewGroup.LayoutParams.MATCH_PARENT);
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = mLayoutInflater.inflate(R.layout.item, parent, false);
        return new RecyclerViewHolder(mView);
    }

    /**
     * 绑定ViewHoler，给item中的控件设置数据
     */
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        final int newposition = holder.getLayoutPosition() - HeadViewsNum;
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, newposition);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(holder.itemView, newposition);
                    return true;
                }
            });
        }
        holder.tv.setLayoutParams(params);
        holder.tv.setText(datas.get(newposition));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}
