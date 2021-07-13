package com.example.bigapp.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bigapp.R;
import com.example.bigapp.activity.ChatActivity;
import com.example.bigapp.layout.TitleBar;
import com.example.bigapp.socket.SocketServer;
import com.example.bigapp.sqlite.DBOpenHelper;
import com.example.bigapp.sqlite.SharedPreferencesController;
import com.example.bigapp.util.ToastUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatFragment extends Fragment {
    private TextView mWelcomeText;
    private SQLiteDatabase mDb;
    private Button mChatButton;
    private EditText mEditIP;
    private volatile SocketServer server;
    private boolean mSetServer = true;
    private SharedPreferencesController mSP;
    private String mPhoneNumber;
    private String mWelcome;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.chat_fragment, container, false);
        init(view);
        mWelcome = null;
        getWelcome();
        mWelcomeText.setText(mWelcome);
        mChatButton.setOnClickListener(v -> {
            String x = mEditIP.getText().toString();
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra("IPString", x);
            startActivity(intent);
        });
        return view;
    }
    private void init(View view){
        mSP = SharedPreferencesController.newInstance(getActivity());
        mPhoneNumber = mSP.spGetString("phoneNumber");
        DBOpenHelper dbHelper = new DBOpenHelper(getActivity(), "user_db", null, 1);
        mDb = dbHelper.getWritableDatabase();
        if (mSetServer) {
//            getPort();
            server = new SocketServer(9808);
            server.beginListen();
            mSetServer = !mSetServer;
        }
        mWelcomeText = view.findViewById(R.id.welcome_text);
        mChatButton = view.findViewById(R.id.chat_button);
        mEditIP = view.findViewById(R.id.edit_IP);
    }
    private void getWelcome(){
        Cursor cursor = mDb.query("user", null, "phoneNumber = ?", new String[]{mPhoneNumber}, null, null, null);
        String phone = null;
        String name = null;

        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex("name"));
            phone = cursor.getString(cursor.getColumnIndex("phoneNumber"));
            if (name == null) {
                mWelcome = getResources().getString(R.string.welcome, phone);
            } else {
                mWelcome = getResources().getString(R.string.welcome, name);
            }
        }
        cursor.close();
    }

    /**
     * 判断端口是否可用
     * @return
     */
    public int getPort() {
        final int[] portNumber = {0};
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1025; i < 49151; i++) {
                    if (isPortAvailable(i)) {
                        Log.d("Memory", i + " :不能用");
                    } else {
                        Log.d("Memory", i + " :！能用");
                        portNumber[0] = i;
                        break;
                    }
                }
            }
        }).start();
        return 0;
    }

    public boolean isPortAvailable(int port) {
        String host = "0,0,0,0";
        try {
            Socket s = new Socket(host, port);
            s.bind(new InetSocketAddress(host, port));
            s.close();
            return true;
        } catch (IOException e) {
//            e.printStackTrace();
            return false;
        }
    }

}
