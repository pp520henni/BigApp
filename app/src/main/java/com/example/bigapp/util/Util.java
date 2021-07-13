package com.example.bigapp.util;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Util {
    /**
     * dp转px
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * sp转px
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
    /**
     * 将消息转成JSON格式
     */
    public static String getSendMessageJSON(String message, String id){
        String json = null;
        try{
            JSONObject object = new JSONObject();
            object.put("flag", "message");
            object.put("sessionId", id );
            object.put("message", message);
            json = object.toString();
            Log.d("Memory","123321" + json);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return json;
    }

}
