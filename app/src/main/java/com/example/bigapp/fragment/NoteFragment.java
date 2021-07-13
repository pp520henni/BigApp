package com.example.bigapp.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bigapp.R;
import com.example.bigapp.activity.HomePageActivity;
import com.example.bigapp.data.Note;
import com.example.bigapp.data.NoteLab;
import com.example.bigapp.layout.TitleBar;
import com.example.bigapp.util.ToastUtil;

import java.util.UUID;


public class NoteFragment extends Fragment {
    private static final String ARG_NOTE_ID = "note_id";
    private Note mNote;
    private EditText mTitleField;
    private EditText mContentField;
    private Button mDateButton;
    private TitleBar mTitleBar;
    private Boolean mFlag;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID noteId = (UUID)getArguments().getSerializable(ARG_NOTE_ID);
        mNote = NoteLab.newInstance(getActivity()).getNote(noteId);
        mFlag = true;
//        Log.d("Memory", "uuid" + mNote.getDate());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_note, container, false);
        //标题栏返回键
        mTitleBar = v.findViewById(R.id.note_fragment_title_bar);
        //返回上一级
        mTitleBar.setLeftIconOnClickListener(v1 -> {
            mFlag = false;
            returnNote();
        });
        //删除此笔记
        mTitleBar.setRightIconOnClickListener(v1 -> {
            mFlag = false;
            deleteNote();
        });
        //标题EditText
        mTitleField = v.findViewById(R.id.title_note);
        mTitleField.setText(mNote.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //内容EditText
        mContentField = v.findViewById(R.id.content_note);
        mContentField.setText(mNote.getContent());
        mContentField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setContent(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //时间按钮
        mDateButton = v.findViewById(R.id.time_note);
        mDateButton.setText(mNote.getDate().toString());
        mDateButton.setEnabled(false);
        return v;
    }

    public static NoteFragment newInstance(UUID noteId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTE_ID, noteId);
        NoteFragment noteFragment = new NoteFragment();
        noteFragment.setArguments(args);
        return noteFragment;
    }

    @Override
    public void onPause() {
        super.onPause();
//        Log.d("Memory", "退出NoteFragment");
        if(mFlag) {
            returnNote();
        }
        mFlag = true;

    }
    private void returnNote(){
        if(mTitleField.getText().toString().equals("")){
            new AlertDialog.Builder(getActivity())
                    .setIcon(R.drawable.chai)
                    .setTitle(getResources().getString(R.string.return_note_title))
                    .setMessage(getResources().getString(R.string.return_note_message))
                    .setPositiveButton(getResources().getString(R.string.return_note_sure), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ToastUtil.showToast(getActivity(), getResources().getString(R.string.return_note_result));
//                            Intent intent = new Intent(getActivity(), HomePageActivity.class);
//                            startActivity(intent);
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.return_note_cancle), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        } else {
            //刷新数据
            NoteLab.newInstance(getActivity()).updateNote(mNote);
            getActivity().finish();
        }
    }
    public void deleteNote(){
        NoteLab.newInstance(getActivity()).deleteNote(mNote);
        getActivity().finish();
    }
}
