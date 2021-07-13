package com.example.bigapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bigapp.R;
import com.example.bigapp.util.ToastUtil;

import java.util.ArrayList;

public class DialogActivity extends AppCompatActivity
        implements View.OnClickListener {
    private Button mNormalDialog;
    private Button mListDialog;
    private Button mRadioDialog;
    private int mYourChoice;
    private Button mCheckDialog;
    private Button mEditDialog;
    private Button mCustomDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        //用于改变状态栏字体颜色
        DialogActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        //改变状态栏的背景颜色
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.background_grey));

        mNormalDialog = findViewById(R.id.normal_dialog);
        mListDialog = findViewById(R.id.list_dialog);
        mRadioDialog = findViewById(R.id.radio_dialog);
        mCheckDialog = findViewById(R.id.check_dialog);
        mEditDialog = findViewById(R.id.edit_dialog);
        mCustomDialog = findViewById(R.id.custom_dialog);
        mNormalDialog.setOnClickListener(this);
        mListDialog.setOnClickListener(this);
        mRadioDialog.setOnClickListener(this);
        mCheckDialog.setOnClickListener(this);
        mEditDialog.setOnClickListener(this);
        mCustomDialog.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mNormalDialog) {
            showNormalDialog();
        } else if (view == mListDialog) {
            showListDialog();
        } else if (view == mRadioDialog) {
            showRadioDialog();
        } else if (view == mCheckDialog) {
            showCheckDialog();
        } else if (view == mEditDialog) {
            showEditDialog();
        } else if (view == mCustomDialog) {
            showCustomDialog();
        }
    }

    /**
     * 普通的对话框
     */
    public void showNormalDialog() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.chai)
                .setTitle(getResources().getString(R.string.normal_dialog_title))
                .setMessage(getResources().getString(R.string.normal_dialog_message))
                .setPositiveButton(getResources().getString(R.string.return_note_sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtil.showToast(getApplicationContext(), getResources().getString(R.string.return_note_sure));
                    }
                })
                .setNegativeButton(getResources().getString(R.string.return_note_cancle), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtil.showToast(getApplicationContext(), getResources().getString(R.string.return_note_cancle));
                    }
                })
                .show();
    }

    /**
     * 列表对话框
     */
    public void showListDialog() {
        //"第一条信息", "第二条信息", "第三条信息", "第四条信息"
        final String[] items = {getResources().getString(R.string.first_message), getResources().getString(R.string.second_message), getResources().getString(R.string.third_message), getResources().getString(R.string.fourth_message)};
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.list_dialog_title))
                .setIcon(R.drawable.chai)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtil.showToast(getApplicationContext(), getResources().getString(R.string.your_choose) + items[which]);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.return_note_cancle), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtil.showToast(getApplicationContext(), getResources().getString(R.string.return_note_cancle));
                    }
                })
                .show();
    }

    /**
     * 单选对话框
     */
    public void showRadioDialog() {
        final String[] items = {getResources().getString(R.string.man), getResources().getString(R.string.woman), getResources().getString(R.string.secrecy)};
        mYourChoice = 0;
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.chai)
                .setTitle(getResources().getString(R.string.sex_dialog))
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mYourChoice = which;
                    }
                })
                .setPositiveButton(getResources().getString(R.string.return_note_sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtil.showToast(getApplicationContext(), getResources().getString(R.string.your_choose) + items[mYourChoice]);
                    }
                })
                .show();
    }

    /**
     * 多选对话框
     */
    public void showCheckDialog() {
        final String[] items = {getResources().getString(R.string.monday), getResources().getString(R.string.tuesday),
                getResources().getString(R.string.wednesday), getResources().getString(R.string.thursday),
                getResources().getString(R.string.friday), getResources().getString(R.string.saturday),
                getResources().getString(R.string.sunday)};
        final boolean[] initChoices = {false, false, false, false, false, false, false};
        ArrayList<Integer> choices = new ArrayList<>();
        choices.clear();
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.chai)
                .setTitle(getResources().getString(R.string.workday_dialog))
                .setMultiChoiceItems(items, initChoices, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            choices.add(which);
                        } else {
                            choices.remove(which);
                        }
                    }
                })
                .setPositiveButton(getResources().getString(R.string.return_note_sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuilder str = new StringBuilder();
                        for (int i = 0; i < choices.size(); i++) {
                            str.append(items[choices.get(i)]);
                        }
                        ToastUtil.showToast(getApplicationContext(), getResources().getString(R.string.your_choose) + str);
                    }
                })
                .show();
    }

    /**
     * 输入对话框
     */
    public void showEditDialog() {
        final EditText editText = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.name_dialog))
                .setView(editText)
                .setPositiveButton(getResources().getString(R.string.return_note_sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtil.showToast(getApplicationContext(), editText.getText().toString());
                    }
                })
                .show();
    }

    /**
     * 自定义对话框
     */
    public void showCustomDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.custom_view))
                .setView(R.layout.custom_dialog)
                .setIcon(R.drawable.chai)
                .setPositiveButton(getResources().getString(R.string.return_note_sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
}