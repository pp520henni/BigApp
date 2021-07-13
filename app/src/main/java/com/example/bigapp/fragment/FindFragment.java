package com.example.bigapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bigapp.R;
import com.example.bigapp.activity.DialogActivity;
import com.example.bigapp.activity.FingerprintActivity;
import com.example.bigapp.activity.LeftMenuActivity;
import com.example.bigapp.activity.MapActivity;
import com.example.bigapp.controller.ServiceController;

public class FindFragment extends Fragment
        implements View.OnClickListener {

    private Button mDialogButton;
    private Button mServiceButton;
    ServiceController sc;
    private Button mFingerprintButton;
    private Button mLeftMenuButton;
    private Button mMapButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.find_fragment, container, false);

        init(view);

        return view;
    }
    private void init(View view){
        mDialogButton = view.findViewById(R.id.dialog_button);
        mServiceButton = view.findViewById(R.id.music_service);
        mFingerprintButton = view.findViewById(R.id.fingerprint_button);
        mLeftMenuButton = view.findViewById(R.id.left_menu_button);
        mMapButton = view.findViewById(R.id.map_button);
        mMapButton.setOnClickListener(this);
        mLeftMenuButton.setOnClickListener(this);
        mFingerprintButton.setOnClickListener(this);
        mServiceButton.setOnClickListener(this);
        mDialogButton.setOnClickListener(this);
        sc = new ServiceController(getActivity());
    }
    @Override
    public void onClick(View view) {
        if (view == mDialogButton) {
            Intent intent = new Intent(getActivity(), DialogActivity.class);
            startActivity(intent);
        } else if (view == mServiceButton) {
            sc.setService();
        } else if (view == mFingerprintButton) {
            Intent intent = new Intent(getActivity(), FingerprintActivity.class);
            startActivity(intent);
        } else if (view == mLeftMenuButton) {
            Intent intent = new Intent(getActivity(), LeftMenuActivity.class);
            startActivity(intent);
        } else if(view == mMapButton){
            Intent intent = new Intent(getActivity(), MapActivity.class);
            startActivity(intent);
        }
    }
}
