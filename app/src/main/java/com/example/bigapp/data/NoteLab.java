package com.example.bigapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.bigapp.sqlite.DBOpenHelper;
import com.example.bigapp.sqlite.DbSchema;
import com.example.bigapp.sqlite.SharedPreferencesController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.example.bigapp.sqlite.DbSchema.*;

public class NoteLab {
    private static NoteLab sNoteLab = null;
    private DBOpenHelper mDBOpenHelper;
    private SQLiteDatabase mDB;
    private Context mContext;

    /**
     * 创建NoteLab对象
     */
    public NoteLab(Context context) {
        mContext = context.getApplicationContext();
        mDBOpenHelper = new DBOpenHelper(mContext, "user_db" ,null, 1);
        mDB = mDBOpenHelper.getWritableDatabase();

    }

    /**
     * 单例模式
     * @return 一个NoteLab对象
     */
    public static NoteLab newInstance(Context context){
        context = context.getApplicationContext();
        if(sNoteLab == null){
            synchronized (context){
                if(sNoteLab == null){
                    sNoteLab = new NoteLab(context);
                }
            }
        }
        return sNoteLab;
    }

    /**
     * @return 返回一个泛型列表
     */
    public List<Note> getNoteList(){
        SharedPreferencesController SP = new SharedPreferencesController(mContext);
        String phoneNumber = SP.spGetString("phoneNumber");
        List<Note> noteList = queryNote(phoneNumber);

        return noteList;
    }

    /**
     * 查找某个Note
     * @param id ID
     * @return 返回找到的Note或null
     */
    public Note getNote(UUID id){
        Cursor cursor = mDB.query("notes", null, "uuid = ?", new String[]{id.toString()}, null, null, null);
        if(cursor.getCount() == 0) {
            return null;
        }
        cursor.moveToFirst();
        String uuid = cursor.getString(cursor.getColumnIndex(NoteTable.Cols.UUID));
        String title = cursor.getString(cursor.getColumnIndex(NoteTable.Cols.TITLE));
        String date = cursor.getString(cursor.getColumnIndex(NoteTable.Cols.DATE));
        String content = cursor.getString(cursor.getColumnIndex(NoteTable.Cols.CONTEXT));
        Note note = new Note(UUID.fromString(uuid));
        note.setContent(content);
        note.setDate(new Date(date));
        note.setTitle(title);
        cursor.close();
        return note;
    }

    /**
     * 添加一个Note
     * @param note
     */
    public void addNote(Note note){
        ContentValues values = getContentValues(note);
        SharedPreferencesController SP = SharedPreferencesController.newInstance(mContext);
        values.put(NoteTable.Cols.PHONE_NUMBER, SP.spGetString("phoneNumber"));
        mDB.insert("notes", null, values);
    }

    /**
     * 查询数据
     */
    public List<Note> queryNote(String whereArgs){
        Log.d("Memory", "111" + whereArgs);
        List<Note> noteList = new ArrayList<>();
        Cursor cursor = mDB.query("notes", null, "phoneNumber = ?", new String[]{whereArgs}, null, null, null);
        while(cursor.moveToNext()){
            String uuid = cursor.getString(cursor.getColumnIndex(NoteTable.Cols.UUID));
            String title = cursor.getString(cursor.getColumnIndex(NoteTable.Cols.TITLE));
            String date = cursor.getString(cursor.getColumnIndex(NoteTable.Cols.DATE));
            String content = cursor.getString(cursor.getColumnIndex(NoteTable.Cols.CONTEXT));
            Note note = new Note(UUID.fromString(uuid));
            note.setContent(content);
            note.setDate(new Date(date));
            note.setTitle(title);
            noteList.add(note);
        }
        cursor.close();
        return noteList;
    }

    /**
     * 更新数据
     */
    public void updateNote(Note note){
        String uuidString = note.getId().toString();
        ContentValues values = getContentValues(note);
        mDB.update("notes", values, NoteTable.Cols.UUID + " = ?", new String[]{uuidString});
    }

    /**
     * 删除一个笔记
     * @param note
     */
    public void deleteNote(Note note){
        String uuidString = note.getId().toString();
        mDB.delete("notes", NoteTable.Cols.UUID + " = ?", new String[]{uuidString});
    }
    /**
     * 插入或更新辅助类
     */
    private static ContentValues getContentValues(Note note){
        ContentValues values = new ContentValues();
        values.put(NoteTable.Cols.UUID, note.getId().toString());
        values.put(NoteTable.Cols.TITLE, note.getTitle());
        values.put(NoteTable.Cols.CONTEXT, note.getContent());
        values.put(NoteTable.Cols.DATE, note.getDate().toString());
        return values;
    }

}
