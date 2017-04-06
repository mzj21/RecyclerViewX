package com.xing.xrecyclerviewsample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ActivityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("ActivityMain");
        init();
    }

    private void init() {
        findViewById(R.id.t1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ActivityMain.this, XRecyclerViewActivity.class);
            }
        });
        findViewById(R.id.t2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ActivityMain.this, XRecyclerViewRefreshActivity.class);
            }
        });
    }

    private void startActivity(Activity activity, Class clazz) {
        Intent intent = new Intent(activity, clazz);
        startActivity(intent);
    }
}
