package com.xing.xrecyclerviewsample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.xing.xrecyclerview.XItemDecoration;
import com.xing.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mzj on 2017/4/6.
 */

public class ActivityMain extends AppCompatActivity implements XRecyclerView.LoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    LinearLayout ll1;
    LinearLayout ll2;
    SwipeRefreshLayout mSwipeRefreshLayout;
    XRecyclerView xRecyclerView;
    SeekBar seekbar1;
    SeekBar seekbar2;
    List<String> datas;
    RecyclerViewAdapter adapter;
    View headview;
    XItemDecoration itemDecoration;
    int loadnum = 20;
    boolean isneterror, isnomore;
    int LayoutOrientation = 1;
    int LoadType;
    int data_size;
    int spanCount;
    int spacing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xrecyclerview);
        ll1 = (LinearLayout) findViewById(R.id.ll1);
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mSwipeRefreshLayout);
        xRecyclerView = (XRecyclerView) findViewById(R.id.xRecyclerView);
        seekbar1 = (SeekBar) findViewById(R.id.seekbar1);
        seekbar2 = (SeekBar) findViewById(R.id.seekbar2);
        isneterror = false;
        isnomore = false;
        spanCount = 3;
        spacing = 5;
        init();
    }

    private void init() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        xRecyclerView.setLoadMoreListener(this);
        headview = LayoutInflater.from(this).inflate(R.layout.headview, xRecyclerView, false);
        xRecyclerView.addHeaderView(headview);
        headview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("head1");
            }
        });

        seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                spacing = progress;
                mSwipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        onRefresh();
                    }
                });
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                spanCount = progress + 2;
                if (xRecyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    xRecyclerView.removeItemDecoration(itemDecoration);
                    int o = ((GridLayoutManager) xRecyclerView.getLayoutManager()).getOrientation();
                    xRecyclerView.switchLayoutManager(new GridLayoutManager(ActivityMain.this, spanCount,
                            o, false));
                    itemDecoration = new XItemDecoration(xRecyclerView.getHeadViewSize(), spacing, getResources().getColor(R.color.colorAccent));
                    xRecyclerView.addItemDecoration(itemDecoration);
                }
                mSwipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        onRefresh();
                    }
                });
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        xRecyclerView.removeItemDecoration(itemDecoration);
        switch (item.getItemId()) {
            case R.id.menu_1:
                LayoutOrientation = 1;
                xRecyclerView.switchLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                ll2.setVisibility(View.GONE);
                break;
            case R.id.menu_2:
                LayoutOrientation = 0;
                xRecyclerView.switchLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                ll2.setVisibility(View.GONE);
                break;
            case R.id.menu_3:
                LayoutOrientation = 1;
                xRecyclerView.switchLayoutManager(new GridLayoutManager(this, spanCount, GridLayoutManager.VERTICAL, false));
                ll2.setVisibility(View.VISIBLE);
                break;
            case R.id.menu_4:
                LayoutOrientation = 0;
                xRecyclerView.switchLayoutManager(new GridLayoutManager(this, spanCount, GridLayoutManager.HORIZONTAL, false));
                itemDecoration = new XItemDecoration(xRecyclerView.getHeadViewSize(), spacing, getResources().getColor(R.color.colorAccent));
                ll2.setVisibility(View.VISIBLE);
                break;
        }
        ll1.setVisibility(View.VISIBLE);
        changeHeads();
        xRecyclerView.removeItemDecoration(itemDecoration);
        itemDecoration = new XItemDecoration(xRecyclerView.getHeadViewSize(), spacing, getResources().getColor(R.color.colorAccent));
        xRecyclerView.addItemDecoration(itemDecoration);
        onRefresh();
        return super.onOptionsItemSelected(item);
    }

    // 模拟更多加载的各种情况
    @Override
    public void onLoadMore() {
        data_size = datas.size();
        switch (LoadType) {
            case 0:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < loadnum; i++) {
                            int b = data_size + i;
                            datas.add("" + b);
                        }
                        xRecyclerView.setFootNormal();
                    }
                }, 1000);
                LoadType = 1;
                break;
            case 1:
                xRecyclerView.setFootError();
                LoadType = 2;
                break;
            case 2:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < loadnum - 5; i++) {
                            int b = data_size + i;
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
                            int b = data_size + i;
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
        LoadType = 0;
        datas = new ArrayList<>();
        for (int i = 0; i < loadnum; i++) {
            datas.add("" + i);
        }
        adapter = new RecyclerViewAdapter(ActivityMain.this, datas, xRecyclerView.getHeadViewSize(), LayoutOrientation);
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                toast("position=" + position + "\n" +
                        "view.getWidth()=" + view.getWidth() + "\n" +
                        "view.getHeight()=" + view.getHeight());
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setFootNormal();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void changeHeads() {
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(LayoutOrientation == 1 ? RecyclerView.LayoutParams.MATCH_PARENT : getResources().getDimensionPixelSize(R.dimen._50dp)
                , LayoutOrientation == 1 ? getResources().getDimensionPixelSize(R.dimen._50dp) : RecyclerView.LayoutParams.MATCH_PARENT);
        headview.setLayoutParams(params);
    }

    private void toast(String title) {
        Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
    }
}