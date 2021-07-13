
package com.example.bigapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.bigapp.R;
import com.example.bigapp.fragment.NoteFragment;

import java.util.UUID;

public class NoteActivity extends AppCompatActivity {
    public static final String NOTE_UUID = "note_uuid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        //用于改变状态栏字体颜色
        NoteActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        //改变状态栏的背景颜色
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.background_grey));
        //获取FragmentManager
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);//找到fragment
        if(fragment == null){
            UUID noteId = (UUID)getIntent().getSerializableExtra(NOTE_UUID);
            fragment = NoteFragment.newInstance(noteId);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
    public static Intent newIntent(Context packageContext, UUID noteId){
        Intent intent = new Intent(packageContext, NoteActivity.class);
        intent.putExtra(NOTE_UUID, noteId);
        return intent;
    }
}