package com.example.bigapp.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;

import com.example.bigapp.sqlite.DbSchema.NoteTable;

import static com.example.bigapp.sqlite.DbSchema.*;

public class DBOpenHelper extends SQLiteOpenHelper {
    private String mTableName;
    private SQLiteDatabase mSQLiteDatabase;
    public DBOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + UserTable.USER_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + UserTable.Cols.NAME + ","
                                                                        + UserTable.Cols.PHONE_NUMBER + "," + UserTable.Cols.PASSWORD + ", "
                                                                        +UserTable.Cols.SEX + "," + UserTable.Cols.PICTURE +")");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + NoteTable.NOTE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                                        + NoteTable.Cols.PHONE_NUMBER + ","
                                                                        + NoteTable.Cols.UUID + "," + NoteTable.Cols.TITLE + ","
                                                                        + NoteTable.Cols.DATE + "," + NoteTable.Cols.CONTEXT + ")");
        Log.d("Memory", "onCreate数据库");
    }

    /**
     *
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
