package com.example.bigapp.controller;

import android.app.Activity;
import android.content.Intent;

import com.example.bigapp.service.MusicPlayerService;

public class ServiceController{
    private boolean flag = true;
    Activity mActivity;
    public ServiceController(Activity activity){
        mActivity = activity;
    }
    public void setService(){
        Intent intent = new Intent(mActivity, MusicPlayerService.class);
        intent.setAction("android.intent.action.MUSIC_SERVICE");
        if(flag){
            intent.putExtra("cmd",0);//0,开启前台服务,1,关闭前台服务
            mActivity.startService(intent);
        }else {
            intent.putExtra("cmd",1);//0,开启前台服务,1,关闭前台服务
            mActivity.stopService(intent);
        }
        flag =! flag;
    }
}
