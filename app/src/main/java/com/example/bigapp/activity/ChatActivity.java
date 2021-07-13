package com.example.bigapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bigapp.R;
import com.example.bigapp.data.ChatMessage;
import com.example.bigapp.fragment.NoteListFragment;
import com.example.bigapp.layout.TitleBar;
import com.example.bigapp.socket.SocketClient;
import com.example.bigapp.socket.SocketServer;
import com.example.bigapp.sqlite.DBOpenHelper;
import com.example.bigapp.sqlite.SharedPreferencesController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.bigapp.util.Util.getSendMessageJSON;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private TitleBar mChatTitleBar;
    private EditText mEditMessage;
    private Button mButtonMessage;
    private SocketClient mClient;
    private String mData;
    private boolean mSendState;
    private boolean mGetState;
    private SQLiteDatabase mDb;
    private SharedPreferencesController mSP;
    private String mPhoneNumber;
    private List<ChatMessage> mChatMessageList;
    private MessageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //用于改变状态栏字体颜色
        ChatActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Intent intent = getIntent();
        String site = intent.getStringExtra("IPString");
        Log.d("Memory", site);

        //客户端
        mClient = new SocketClient();
        //服务端的IP地址和端口号
        mClient.clintValue(this, site, 9808);
        //开启客户端接受信息线程
        mClient.openClientThread();
        init();
        mChatMessageList = new ArrayList<>();
        //获取信息
        getMessage();
        mRecyclerView = findViewById(R.id.chat_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new MessageAdapter(mChatMessageList);
        mRecyclerView.setAdapter(mAdapter);
        mButtonMessage.setOnClickListener(v -> {
            mClient.sendMsg(getSendMessageJSON(mEditMessage.getText().toString(), getMyName()));
            Log.d("Memory", getSendMessageJSON(mEditMessage.getText().toString(), getMyName()));
            ChatMessage message = new ChatMessage(getMyName(), mEditMessage.getText().toString(), false);
            mChatMessageList.add(message);
            mEditMessage.setText("");
            updateUI();
        });

    }

    private void init() {
        mChatTitleBar = findViewById(R.id.chat_title_bar);
        mEditMessage = findViewById(R.id.message_edit);
        mButtonMessage = findViewById(R.id.message_button);
        mData = "";
        mSendState = true;
        mGetState = true;
        DBOpenHelper openHelper = new DBOpenHelper(this, "user_db", null, 1);
        mDb = openHelper.getWritableDatabase();
        mSP = SharedPreferencesController.newInstance(this);
        mPhoneNumber = mSP.spGetString("phoneNumber");

    }

    /**
     * 获取信息
     */
    public void getMessage() {
        //Socket服务端接受信息
        SocketServer.ServerHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Log.d("Memory", msg.obj.toString());
                parseMessage(msg.obj.toString());
            }
        };

    }

    private class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {
        private final int LEFT_MESSAGE = 2;
        private final int RIGHT_MESSAGE = 3;
        private List<ChatMessage> mChatMessageList;
        private View mView;

        public MessageAdapter(List<ChatMessage> list) {
            mChatMessageList = list;
        }

        @NonNull
        @Override
        public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == LEFT_MESSAGE) {
                mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_message_left, parent, false);
            } else {
                mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_message_right, parent, false);
            }
            MessageViewHolder holder = new MessageViewHolder(mView);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
            ChatMessage message = mChatMessageList.get(position);
            holder.bind(message);
        }

        @Override
        public int getItemViewType(int position) {
            ChatMessage message = mChatMessageList.get(position);
            if (message.isSelf()) { //true就是对方的信息
                return LEFT_MESSAGE;
            } else {
                return RIGHT_MESSAGE;
            }

        }

        @Override
        public int getItemCount() {
            return mChatMessageList.size();
        }
    }

    private static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView mNameMsg;
        TextView mTxtMsg;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            mNameMsg = itemView.findViewById(R.id.lblMsgFrom);
            mTxtMsg = itemView.findViewById(R.id.txtMsg);
        }

        public void bind(ChatMessage message) {
            mNameMsg.setText(message.getFromName());
            mTxtMsg.setText(message.getMessage());
        }
    }

    /**
     * 获取自己的名字/电话号码
     */
    public String getMyName() {
        Cursor cursor = mDb.query("user", null, "phoneNumber = ?", new String[]{mPhoneNumber}, null, null, null);
        String phone = null;
        String name = null;
        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex("name"));
            phone = cursor.getString(cursor.getColumnIndex("phoneNumber"));
        }
        cursor.close();
        if (name == null) {
            return phone;
        } else {
            return name;
        }
    }
    private static final String TAG_SELF = "self", TAG_NEW = "new",
            TAG_MESSAGE = "message", TAG_EXIT = "exit";
    /**
     * 解析从服务端获取到的信息
     */
    private void parseMessage(final String msg){
        try{
            JSONObject object = new JSONObject(msg);
            String flag = object.getString("flag");
            if(flag.equalsIgnoreCase(TAG_MESSAGE)){
                String sessionId = object.getString("sessionId");
                String message = object.getString("message");
                Log.d("Memory","sessionId" + sessionId);
                Log.d("Memory","message" + message);
                ChatMessage chatMessage = new ChatMessage(sessionId, message, true);
                mChatMessageList.add(chatMessage);
                updateUI();
            }

        } catch (JSONException e){
            e.printStackTrace();
        }
    }
    public void updateUI(){
        if(mAdapter == null){
            mAdapter = new MessageAdapter(mChatMessageList);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

}