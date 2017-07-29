package com.xing.xrecyclerviewsample;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

class RecyclerViewHolder extends RecyclerView.ViewHolder {
    public TextView tv;

    RecyclerViewHolder(View itemView) {
        super(itemView);
        tv = (TextView) itemView.findViewById(R.id.tv);
    }
}
