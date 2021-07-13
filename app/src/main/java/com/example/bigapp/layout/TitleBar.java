package com.example.bigapp.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bigapp.R;

public class TitleBar extends RelativeLayout {
    private ImageView mLeftIcon;
    private TextView mLeftText;
    private TextView mTitleText;
    private TextView mRightText;
    private ImageView mRightIcon;

    public TitleBar(@NonNull Context context) {
        this(context, null);
    }

    public TitleBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TitleBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        View view = LayoutInflater.from(context).inflate(R.layout.layout_titlebar, this);
        mLeftIcon = view.findViewById(R.id.left_icon);
        mLeftText = view.findViewById(R.id.left_text);
        mTitleText = view.findViewById(R.id.title_text);
        mRightText = view.findViewById(R.id.right_text);
        mRightIcon = view.findViewById(R.id.right_icon);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        int backgroundColor = typedArray.getColor(R.styleable.TitleBar_backgroundColor, 0);
        int leftIcon = typedArray.getResourceId(R.styleable.TitleBar_left_icon, 0);
        String leftTitle = typedArray.getString(R.styleable.TitleBar_left_title);
        String title = typedArray.getString(R.styleable.TitleBar_title);
        String rightTitle = typedArray.getString(R.styleable.TitleBar_right_title);
        int rightIcon = typedArray.getResourceId(R.styleable.TitleBar_right_icon, 0);


        mLeftIcon.setImageResource(leftIcon);
        mLeftText.setText(leftTitle);
        mTitleText.setText(title);
        mRightText.setText(rightTitle);
        mRightIcon.setImageResource(rightIcon);
        view.setBackgroundColor(backgroundColor);
        typedArray.recycle();

    }

    public void setLeftIconOnClickListener(OnClickListener listener) {
        mLeftIcon.setOnClickListener(listener);
    }

    public void setRightIconOnClickListener(OnClickListener listener) {
        mRightIcon.setOnClickListener(listener);
    }

    public void setTitleText(String titleText){
        mTitleText.setText(titleText);
    }
}
