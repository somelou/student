package com.example.student.ad;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.example.student.MainActivity;
import com.example.student.R;

import java.text.MessageFormat;
import java.util.Timer;
import java.util.TimerTask;

public class AdActivity extends AppCompatActivity {

    private Button go;
    private int i;
    private Timer timer;
    static Handler handler;


    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        initView();

        handler = new Handler(){
            public void handleMessage(Message msg){
                switch(msg.what){
                    case 200:
                        go.setText(MessageFormat.format("跳过({0})", i));
                        //为什么用for循环就不行？
                        i--;
                        if (i<0){
                            turnToMainActivity();
                        }
                }
            }
        };
        //倒计时
        Countdown();

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnToMainActivity();
            }
        });

    }


    private void turnToMainActivity(){
        //关闭定时器
        timer.cancel();
        //跳往主界面
        startActivity(new Intent(AdActivity.this, MainActivity.class));
        //关闭启动页
        finish();
    }


    private void Countdown() {

        //初始倒计时3秒
        i = 5;

        //定时器
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //向handler发送状态值
                handler.sendEmptyMessage(200);
            }
        };

        //开启定时器，时间差值为1000毫秒
        timer.schedule(task,1,1000);
    }

    private void initView() {
        go = findViewById(R.id.go);
    }
}
