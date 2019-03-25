package com.example.student.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.example.student.R;


/**
 * 加载对话框
 */

public class LoadingDialog extends ProgressDialog {

    private String mMessage;

    private TextView mTitleTv;


    public LoadingDialog(Context context, String message, boolean canceledOnTouchOutside) {
        super(context, R.style.Theme_Light_LoadingDialog);
        this.mMessage = message;
        // 如果触摸屏幕其它区域,可以选择让这个progressDialog消失或者无变化
        setCanceledOnTouchOutside(canceledOnTouchOutside);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        mTitleTv = findViewById(R.id.tv_loading_dialog);
        mTitleTv.setText(mMessage);
        setCancelable(false);//不可取消
    }

    public void setTitle(String message) {
        this.mMessage = message;
        mTitleTv.setText(mMessage);
    }
}
