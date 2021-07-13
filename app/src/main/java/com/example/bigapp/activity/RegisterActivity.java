package com.example.bigapp.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.bigapp.R;
import com.example.bigapp.sqlite.DBOpenHelper;
import com.example.bigapp.util.ToastUtil;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mRegisterSubmit;
    private EditText mRegisterPhone;
    private EditText mRegisterPassword;
    private EditText mPasswordAgain;
    private SQLiteDatabase mDb;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        RegisterActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        init();
    }

    public void init() {
        mRegisterSubmit = findViewById(R.id.submit_register);
        mRegisterPhone = findViewById(R.id.phone_number_register);
        mRegisterPassword = findViewById(R.id.password_register);
        mPasswordAgain = findViewById(R.id.password_again_register);
        mRegisterSubmit.setOnClickListener(this);
        DBOpenHelper dbHelper = new DBOpenHelper(this, "user_db", null, 1);
        mDb = dbHelper.getWritableDatabase();
    }

    @Override
    public void onClick(View view) {
        if(view == mRegisterSubmit){
            String phone = mRegisterPhone.getText().toString();
            String password = mRegisterPassword.getText().toString();
            if(phone.length() != 11){
                ToastUtil.showToast(getApplicationContext(), "手机号码格式不对");
                return;
            } else if(!password.equals(mPasswordAgain.getText().toString())){
                ToastUtil.showToast(getApplicationContext(), "两次输入的密码不相等，请重写输入");
                return;
            }
            Cursor cursor = mDb.query("user", new String[]{"phoneNumber"}, "phoneNumber = ?" , new String[]{phone},
                    null, null, null);

            if(cursor.getCount() != 0){
                ToastUtil.showToast(getApplicationContext(), "用户：" + phone + "已存在");
            }else {
                ContentValues values = new ContentValues();
                values.put("phoneNumber", phone);
                values.put("password", password);
                mDb.insert("user", null, values);
                ToastUtil.showToast(getApplicationContext(), "用户：" + phone + "创建成功");
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
            cursor.close();
        }
    }
}