package com.example.bigapp.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bigapp.activity.HomePageActivity;
import com.example.bigapp.R;
import com.example.bigapp.activity.LoginActivity;
import com.example.bigapp.activity.PersonalInformationActivity;
import com.example.bigapp.layout.SettingBar;
import com.example.bigapp.sqlite.SharedPreferencesController;

import java.util.Locale;
import java.util.Objects;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;

public class SettingFragment extends Fragment implements View.OnClickListener{
    private SettingBar mLanguage;
    private SettingBar mPersonalInformation;
    private int mYourChoice;
    private String[] mItems;
    private SettingBar mLogOut;
    private SharedPreferencesController mSP;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.setting_fragment, container, false);

        mLanguage = view.findViewById(R.id.language_setting);
        mLogOut = view.findViewById(R.id.log_out);
        mPersonalInformation = view.findViewById(R.id.personal_information);
        mPersonalInformation.setOnClickListener(this);
        mLogOut.setOnClickListener(this);
        mLanguage.setOnClickListener(this);
        mItems = new String[]{getResources().getString(R.string.chinese_language), getResources().getString(R.string.english_language)};
        //SharedPreferences中获取数据，显示再语言栏的右边
        mSP = SharedPreferencesController.newInstance(getActivity());
        mLanguage.setRightText(mSP.spGetString("LANGUAGE"));

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view == mLanguage){
            new AlertDialog.Builder(getActivity())
                    .setTitle(getResources().getString(R.string.set_language))
                    .setItems(mItems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mYourChoice = which;
                            Log.d("Memory", "mYourChoice " + mYourChoice);
                            SharedPreferencesController SP = SharedPreferencesController.newInstance(getActivity());
//                            spPutInt("LANGUAGE", mYourChoice);
                            if (mYourChoice == 0) {
                                //中文
                                changeLanguage(Locale.CHINESE);
                                mLanguage.setRightText(getResources().getString(R.string.chinese_language));
                                SP.spPutString("LANGUAGE", getResources().getString(R.string.chinese_language));
                            } else if (mYourChoice == 1) {
                                //英文
                                changeLanguage(Locale.ENGLISH);
                                mLanguage.setRightText(getResources().getString(R.string.english_language));
                                SP.spPutString("LANGUAGE", getResources().getString(R.string.english_language));
                            }
                        }
                    })
                    .show();
        } else if(view == mPersonalInformation){
            Intent intent = new Intent(getActivity(), PersonalInformationActivity.class);
            startActivity(intent);
        } else if(view == mLogOut){
            new AlertDialog.Builder(getActivity())
                    .setTitle(getResources().getString(R.string.log_out))
                    .setMessage(getResources().getString(R.string.log_out_message))
                    .setNegativeButton(getResources().getString(R.string.return_note_cancle), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton(getResources().getString(R.string.return_note_sure), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferencesController SP = SharedPreferencesController.newInstance(getActivity());
                            SP.spDeleteData("phoneNumber");
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            intent.setFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(intent);
                            Objects.requireNonNull(getActivity()).finish();
                        }
                    })
                    .show();
        }
    }
    /**
     * 语言切换
     */
    public void changeLanguage(Locale locale) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Configuration configuration = getResources().getConfiguration();
        configuration.setLocale(locale);
        getResources().updateConfiguration(configuration, metrics);
        Intent intent = new Intent(getActivity(), HomePageActivity.class);
        startActivity(intent);
    }
}
