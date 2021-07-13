package com.example.bigapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.bigapp.R;
import com.example.bigapp.fragment.ChatFragment;
import com.example.bigapp.fragment.NoteListFragment;
import com.example.bigapp.fragment.FindFragment;
import com.example.bigapp.fragment.SettingFragment;
import com.example.bigapp.socket.SocketServer;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavigationView;
    private ViewPager mViewPager;
    private List<Fragment> mFragmentList;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        //禁用水平方向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //用于改变状态栏字体颜色
        HomePageActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        //改变状态栏的背景颜色和底部控制栏颜色
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.background_grey));
        window.setNavigationBarColor(this.getResources().getColor(R.color.purple_200));

        init();
    }

    private void init() {
        mBottomNavigationView = findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //取消长按菜单弹出的提示
        Menu menu = mBottomNavigationView.getMenu();
        for(int i = 0; i < menu.size(); i++){
            mBottomNavigationView.findViewById(menu.getItem(i).getItemId()).setOnLongClickListener(v -> true);
        }

        mFragmentList = new ArrayList<>();
        mFragmentList.add(new ChatFragment());
        mFragmentList.add(new NoteListFragment());
        mFragmentList.add(new FindFragment());
        mFragmentList.add(new SettingFragment());
        FragAdapter adapter = new FragAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setAdapter(adapter);


    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
             = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            Log.d("Memory", "item + " + item.getItemId());
            switch (item.getItemId()){
                case R.id.chat:
                    Log.d("Memory", "chat + " + R.id.chat);
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.contacts:
                    Log.d("Memory", "contacts + " + R.id.contacts);
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.find:
                    Log.d("Memory", "find + " + R.id.find);
                    mViewPager.setCurrentItem(2);
                    return true;
                case R.id.setting:
                    Log.d("Memory", "setting + " + R.id.setting);
                    mViewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };

    /**
     * fragment适配器
     */
    public static class FragAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments;

        public FragAdapter(@NonNull FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

}