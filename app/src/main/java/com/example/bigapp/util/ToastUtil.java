package com.example.bigapp.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    private static Toast sToast;

    public static void showToast(Context context, String message){
        context = context.getApplicationContext();
        if(sToast == null){
            sToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            sToast.show();
        }else {
            sToast.setText(message);
        }
        sToast.show();
    }
}
