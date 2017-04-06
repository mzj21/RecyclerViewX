package com.xing.xrecyclerviewsample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.xing.xrecyclerview.DividerGridItemDecoration;
import com.xing.xrecyclerview.DividerListItemDecoration;
import com.xing.xrecyclerview.XRecyclerViewRefresh;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mzj on 2017/4/6.
 */

public class XRecyclerViewRefreshActivity extends AppCompatActivity implements XRecyclerViewRefresh.LoadMoreAndRefreshListener {
    XRecyclerViewRefresh xRecyclerViewRefresh;
    List<String> datas;
    RecyclerViewAdapter recyclerViewAdapter;
    int loadnum = 20;
    boolean isneterror, isnomore;
    int Type;
    int a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("XRecyclerViewRefreshActivity");
        setContentView(R.layout.activity_xrecyclerviewrefresh);
        xRecyclerViewRefresh = (XRecyclerViewRefresh) findViewById(R.id.xRecyclerViewRefresh);
//        xRecyclerViewRefresh.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        xRecyclerViewRefresh.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        isneterror = false;
        isnomore = false;
        init();
    }

    private void init() {
        xRecyclerViewRefresh.setColorSchemeResources(R.color.colorPrimary);
        xRecyclerViewRefresh.setLoadMoreAndRefreshListener(this);

        View view1 = LayoutInflater.from(this).inflate(R.layout.headview1, xRecyclerViewRefresh, false);
        View view2 = LayoutInflater.from(this).inflate(R.layout.headview2, xRecyclerViewRefresh, false);
        xRecyclerViewRefresh.addHeaderView(view1);
        xRecyclerViewRefresh.addHeaderView(view2);
//        xRecyclerViewRefresh.addItemDecoration(new DividerListItemDecoration(DividerListItemDecoration.VERTICAL,
//                getResources().getDimensionPixelSize(R.dimen._1dp), getResources().getColor(R.color.colorPrimary)));
        xRecyclerViewRefresh.addItemDecoration(new DividerGridItemDecoration(2, 3, getResources().getDimensionPixelSize(R.dimen._20dp), true));
        xRecyclerViewRefresh.post(new Runnable() {
            @Override
            public void run() {
                xRecyclerViewRefresh.setRefreshing(true);
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
                        xRecyclerViewRefresh.setFootNormal();
                    }
                }, 1000);
                Type = 1;
                break;
            case 1:
                xRecyclerViewRefresh.setFootError();
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
                        xRecyclerViewRefresh.setFootNoMore();
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
                        xRecyclerViewRefresh.setFootNormal();
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
        xRecyclerViewRefresh.setAdapter(recyclerViewAdapter);
        xRecyclerViewRefresh.setFootNormal();
        xRecyclerViewRefresh.setRefreshing(false);
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
