package com.example.bigapp.socket;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient {
    private Socket client;
    private Context context;
    private int port;    //IP
    private String site;    //端口
    private Thread thread;
    public static Handler mHandler;
    private boolean isClient = false;
    private PrintWriter out;
    private InputStream in;
    private String str;


    /**
     * @effect 开启线程建立连接开启客户端
     */
    public void openClientThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    /**
                     *  connect()步骤
                     * */
                    client = new Socket(site, port);
//                    client.setSoTimeout(3000);//设置超时时间
                    if (client != null) {
                        isClient = true;
                        forOut();
                        forIn();
                    } else {
                        isClient = false;
                        Toast.makeText(context, "网络连接失败", Toast.LENGTH_LONG).show();
                    }
                    Log.i("Memory", "site=" + site + " ,port=" + port);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    Log.i("Memory", "6 " + e);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("Memory", "7 " + e);
                }

            }
        }).start();
    }

    /**
     * 调用时向类里传值
     */
    public void clintValue(Context context, String site, int port) {
        this.context = context;
        this.site = site;
        this.port = port;
    }

    /**
     * @effect 得到输出字符串
     */
    public void forOut() {
        try {
            out = new PrintWriter(client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("Memory", "8");
        }
    }

    /**
     * @steps read();
     * @effect 得到输入字符串
     */
    public void forIn() {

        while (isClient) {
            try {
                in = client.getInputStream();

                /**得到的是16进制数，需要进行解析*/
                byte[] bt = new byte[50];
                in.read(bt);
                str = new String(bt, "UTF-8");
            } catch (IOException e) {
            }
            if (str != null) {
                Message msg = new Message();
                msg.obj = str;
                mHandler.sendMessage(msg);
            }

        }
    }

    /**
     * @steps write();
     * @effect 发送消息
     */
    public void sendMsg(final String str) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (client != null) {
                    out.print(str);
                    out.flush();
                    Log.i("outtt", out + "");
                } else {
                    isClient = false;
                    Toast.makeText(context, "网络连接失败", Toast.LENGTH_LONG).show();
                }
            }
        }).start();

    }
}