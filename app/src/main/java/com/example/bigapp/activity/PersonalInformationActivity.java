package com.example.bigapp.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bigapp.R;
import com.example.bigapp.layout.SettingBar;
import com.example.bigapp.layout.TitleBar;
import com.example.bigapp.sqlite.DBOpenHelper;
import com.example.bigapp.sqlite.DbSchema;
import com.example.bigapp.sqlite.SharedPreferencesController;
import com.example.bigapp.util.ToastUtil;

import java.io.ByteArrayOutputStream;

public class PersonalInformationActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int REQUEST_SYSTEM_PIC = 1;
    private TitleBar mInformationTitleBar;
    private SettingBar mName;
    private SettingBar mHeadSculpture;
    private SettingBar mSex;
    private int mYourChoice;
    private SQLiteDatabase mDb;
    private String mPhoneNumber;
    private ImageView mPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);

        init();
        mInformationTitleBar.setLeftIconOnClickListener(v -> {
            finish();
        });
    }
    public void init(){
        mName = findViewById(R.id.name_setting);
        mHeadSculpture = findViewById(R.id.head_sculpture_setting);
        mSex = findViewById(R.id.sex_setting);
        mName.setOnClickListener(this);
        mSex.setOnClickListener(this);
        mHeadSculpture.setOnClickListener(this);
        mPhoto = findViewById(R.id.photo_head);
        mInformationTitleBar = findViewById(R.id.title_bar);

        DBOpenHelper openHelper = new DBOpenHelper(this, "user_db", null, 1);
        mDb = openHelper.getWritableDatabase();
        SharedPreferencesController SP = SharedPreferencesController.newInstance(getApplicationContext());
        mPhoneNumber = SP.spGetString("phoneNumber");
        Cursor cursor = mDb.query("user", null, "phoneNumber = ?", new String[]{mPhoneNumber}, null, null, null);
        if(cursor.getCount() != 0) {
            cursor.moveToFirst();
            String name = cursor.getString(cursor.getColumnIndex("name")); //个人信息
            mName.setRightText(name);
            String sex = cursor.getString(cursor.getColumnIndex("sex"));//性别
            mSex.setRightText(sex);
            String picture = cursor.getString(cursor.getColumnIndex("picture"));//头像
            if(picture != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(picture);
                Bitmap bitmapScr = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
                mPhoto.setImageBitmap(bitmapScr);
            }
        }
        cursor.close();

    }
    @Override
    public void onClick(View v) {
        if(v == mName){
            final EditText editText = new EditText(this);
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.nickname))
                    .setView(editText)
                    .setPositiveButton(getResources().getString(R.string.return_note_sure), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ToastUtil.showToast(getApplicationContext(), editText.getText().toString());
                            ContentValues values = new ContentValues();
                            values.put("name", editText.getText().toString());
                            mDb.update("user", values, "phoneNumber = ?", new String[]{mPhoneNumber});
                            mName.setRightText(editText.getText().toString());
                        }
                    })
                    .show();
        } else if(v == mHeadSculpture){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                openPhotos();//打开相册
            }
        } else if(v == mSex){
//           //男、女、保密的数组
            final String[] items = {getResources().getString(R.string.man), getResources().getString(R.string.woman), getResources().getString(R.string.secrecy)};
            mYourChoice = 0;
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.chai)
                    .setTitle(getResources().getString(R.string.sex)) //性别
                    .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mYourChoice = which;
                        }
                    })
                    .setPositiveButton(getResources().getString(R.string.return_note_sure), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ToastUtil.showToast(getApplicationContext(), items[mYourChoice]);
                            ContentValues values = new ContentValues();
                            values.put("sex", items[mYourChoice]);
                            mDb.update("user", values, "phoneNumber = ?", new String[]{mPhoneNumber});
                            mSex.setRightText(items[mYourChoice]);
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.return_note_cancle), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
    }
    private void openPhotos() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_SYSTEM_PIC);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SYSTEM_PIC && resultCode == RESULT_OK && data != null) {
            handleImageOnKitkat(data);
        }
    }
    private void handleImageOnKitkat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            Log.d("Memory", "URI =  " + uri.getAuthority());
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content:" + "//downloads/public_downloads"),
                        Long.parseLong(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if(cursor.getCount() == 0){
        }
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private void displayImage(String imagePath) {
        if(imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            Bitmap bitmapScr = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
            mPhoto.setImageBitmap(bitmapScr);

            ContentValues values = new ContentValues();
            values.put("picture", imagePath);
            mDb.update("user", values, DbSchema.UserTable.Cols.PHONE_NUMBER + " = ?", new String[]{mPhoneNumber});

        } else {
            Toast.makeText(this, getResources().getString(R.string.failed_get_image), Toast.LENGTH_SHORT).show();      //无法获取图片
        }
    }
}