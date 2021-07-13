package com.example.bigapp.sqlite;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesController {
    private static final String SP_FILE_NAME = "sp_file";
    private static SharedPreferences mPreferences = null;

    /**
     * 创建实例对象
     * @param context
     */
    public SharedPreferencesController(Context context) {
        synchronized (this) {
            if (mPreferences == null) {
                mPreferences = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
            }
        }
    }

    private static SharedPreferencesController instance = null;

    /**
     * 单例模式
     * @param context
     * @return
     */
    public synchronized static SharedPreferencesController newInstance(Context context) {
        context = context.getApplicationContext();
        if (instance == null) {
            synchronized (SharedPreferencesController.class) {
                if(instance == null) {
                    instance = new SharedPreferencesController(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public void spPutInt(String key, int value) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int spGetInt(String key) {
        return mPreferences.getInt(key, 0);
    }

    public void spPutString(String key, String value) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String spGetString(String key) {
        return mPreferences.getString(key, null);
    }

    /**
     * editor.remove方法移除指定的一条信息
     * @param key
     */
    public void spDeleteData(String key){
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.remove(key);
        editor.apply();
    }
}
