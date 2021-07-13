package com.example.bigapp.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bigapp.R;
import com.example.bigapp.sqlite.DBOpenHelper;
import com.example.bigapp.sqlite.DbSchema;
import com.example.bigapp.sqlite.SharedPreferencesController;
import com.example.bigapp.util.ToastUtil;

import static com.example.bigapp.sqlite.DbSchema.*;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mPhoneEdit;
    private EditText mPassword;
    private Button mSubmitLogin;
    private TextView mRegister;
    private SQLiteDatabase mDb;
    private SharedPreferencesController mSP;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //改变状态栏字体颜色
        LoginActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        init();
        autoLogin();
    }

    private void init() {
        mPhoneEdit = findViewById(R.id.phone_number_login);
        mPassword = findViewById(R.id.password_login);
        mSubmitLogin = findViewById(R.id.submit_login);
        mRegister = findViewById(R.id.register_login);
        mRegister.setOnClickListener(this);
        mSubmitLogin.setOnClickListener(this);
        DBOpenHelper dbHelper = new DBOpenHelper(this, "user_db", null, 1);
        mDb = dbHelper.getWritableDatabase();
        mSP = SharedPreferencesController.newInstance(this);
    }
    private void autoLogin(){
        String phoneNumber = mSP.spGetString("phoneNumber");
        if(phoneNumber != null&& !mSP.spGetString("phoneNumber").equals("")){
            if(!tableIsExist("user"+phoneNumber, mDb)){
                mDb.execSQL("CREATE TABLE IF NOT EXISTS " + "user" + phoneNumber + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + DataTable.Cols.PHONE_NUMBER + ","
                        + DataTable.Cols.NAMEId + "," + DataTable.Cols.CONTENT + ")");
            }
            Intent intent = new Intent(this, HomePageActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mSubmitLogin) {
            String phone = mPhoneEdit.getText().toString();
            String password = mPassword.getText().toString();
            if(phone.length() != 11){ //格式不对
                ToastUtil.showToast(getApplicationContext(), "手机号码格式不正确");
                return;
            } else if(password.equals("")){//密码错误
                ToastUtil.showToast(getApplicationContext(), "密码不能为空");
                return;
            }

            Cursor cursor = mDb.query("user", null,
                    "phoneNumber = ? and password = ?", new String[]{phone,password}, null, null, null);

            if(cursor.getCount() != 0){
                ToastUtil.showToast(getApplicationContext(), "登录成功");
                SharedPreferencesController SP = SharedPreferencesController.newInstance(this);
                SP.spPutString("phoneNumber", phone);
                Intent intent = new Intent(this, HomePageActivity.class);
                startActivity(intent);
                finish();
            } else {
                Cursor cursor2 = mDb.query("user", null, "phoneNumber = ?", new String[]{phone}, null, null, null);

                if(cursor2.getCount() != 0){

                    ToastUtil.showToast(getApplicationContext(), "密码错误");
                } else {
                    ToastUtil.showToast(getApplicationContext(), "用户不存在");
                }
                cursor2.close();
            }
            cursor.close();
        } else if (view == mRegister) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 判断数据表是否存在
     * @param tabName
     * @param db
     * @return
     */
    public boolean tableIsExist(String tabName, SQLiteDatabase db) {
        boolean result = false;
        if (tabName == null) {
            return false;
        }
        Cursor cursor = null;
        try {

            String sql = "select count(*) as c from sqlite_master where type ='table' and name ='" + tabName.trim() + "' ";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }
            cursor.close();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return result;
    }
}