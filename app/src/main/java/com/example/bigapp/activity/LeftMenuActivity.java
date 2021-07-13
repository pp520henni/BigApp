package com.example.bigapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bigapp.R;
import com.example.bigapp.layout.TitleBar;

public class LeftMenuActivity extends AppCompatActivity {
    private TitleBar mTitleBar;
    private TextView mTextView1;
    private TextView mTextView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_left_menu);
        //改变状态栏字体颜色
        LeftMenuActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        final DrawerLayout drawerLayout=findViewById(R.id.drawer);
        mTitleBar = findViewById(R.id.title_bar);
        mTitleBar.setLeftIconOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });
        mTextView1 = findViewById(R.id.setting1);
        mTextView1.setOnClickListener(v -> {
            Intent intent = new Intent(this, MimiActivity.class);
            startActivity(intent);
        });
        mTextView2 = findViewById(R.id.setting2);
        mTextView2.setOnClickListener(v -> {
            Intent intent = new Intent(this, DialogActivity.class);
            startActivity(intent);
        });

    }

}