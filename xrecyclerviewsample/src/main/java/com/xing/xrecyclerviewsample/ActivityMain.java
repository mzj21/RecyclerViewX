package com.xing.xrecyclerviewsample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xing.xrecyclerview.DividerGridItemDecoration;
import com.xing.xrecyclerview.DividerListItemDecoration;
import com.xing.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends AppCompatActivity implements XRecyclerView.LoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout mSwipeRefreshLayout;
    XRecyclerView xRecyclerView;
    List<String> datas;
    RecyclerViewAdapter recyclerViewAdapter;
    int loadnum = 10;
    boolean isneterror, isnomore;
    int Type;
    int a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mSwipeRefreshLayout);
        xRecyclerView = (XRecyclerView) findViewById(R.id.xRecyclerView);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
//        xRecyclerView.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        isneterror = false;
        isnomore = false;
        init();
    }

    private void init() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        View view1 = LayoutInflater.from(this).inflate(R.layout.headview1, xRecyclerView, false);
        View view2 = LayoutInflater.from(this).inflate(R.layout.headview2, xRecyclerView, false);
        xRecyclerView.addHeaderView(view1);
        xRecyclerView.addHeaderView(view2);
        xRecyclerView.setLoadMoreListener(this);
        xRecyclerView.addItemDecoration(new DividerListItemDecoration(DividerListItemDecoration.VERTICAL,
                getResources().getDimensionPixelSize(R.dimen._1dp), getResources().getColor(R.color.colorPrimary)));
//        xRecyclerView.addItemDecoration(new DividerGridItemDecoration(2, 3,
//                getResources().getDimensionPixelSize(R.dimen._1dp), getResources().getColor(R.color.colorPrimary), true));
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    // 模拟更多加载的各种情况
    @Override
    public void onLoadMore() {
        a = datas.size();
        switch (Type) {
            case 0:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < loadnum; i++) {
                            int b = a + i;
                            datas.add("" + b);
                        }
                        xRecyclerView.setFootNormal();
                    }
                }, 1000);
                Type = 1;
                break;
            case 1:
                xRecyclerView.setFootError();
                Type = 2;
                break;
            case 2:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < loadnum - 5; i++) {
                            int b = a + i;
                            datas.add("" + b);
                        }
                        xRecyclerView.setFootNoMore();
                    }
                }, 2000);
                break;
            default:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < loadnum; i++) {
                            int b = a + i;
                            datas.add("" + b);
                        }
                        xRecyclerView.setFootNormal();
                    }
                }, 1000);
                break;
        }
    }

    @Override
    public void onRefresh() {
        datas = new ArrayList<>();
        for (int i = 0; i < loadnum; i++) {
            datas.add("" + i);
        }
        recyclerViewAdapter = new RecyclerViewAdapter(this, datas, 2);
        xRecyclerView.setAdapter(recyclerViewAdapter);
        xRecyclerView.setFootNormal();
        mSwipeRefreshLayout.setRefreshing(false);
        Type = 0;
        recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                toast("" + position);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    private void toast(String title) {
        Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
    }
}
