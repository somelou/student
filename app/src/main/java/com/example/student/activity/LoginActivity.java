package com.example.student.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.student.R;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    final static String LOGIN_NAME="admin";
    final static String LOGIN_PSWD="123456";

    static String BTN_LOGIN="LOGIN_SUCCESS";

    ProgressBar loginBar;
    EditText name,pswd;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initLogin();
    }

    /*
    * 初始化
     */
    private void initLogin(){
        loginBar=findViewById(R.id.progressBarLogin);
        name=findViewById(R.id.editLoginName);
        pswd=findViewById(R.id.editLoginPassword);
        login=findViewById(R.id.btnLogin);
    }

    /*
    * 当用户名或密码错误之后
     */
    private void reInitForWrong(){
        name.setText(null);
        pswd.setText(null);
        login.setClickable(true);
    }

    //
    public void loginToMainActivity(View view) {
        //启动线程
        login.setClickable(false);
        loginBar.setVisibility(View.VISIBLE);
        MyThread toLogin=new MyThread();
        toLogin.start();
    }

/*    Handler handler=new Handler(){
        //接收消息，用于更新UI界面
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int i=msg.what;
            tv_main_text.setText(i+"");
        }
    };*/
    class MyThread extends Thread{
        @Override
        public void run() {
            super.run();
            for (int i = 0; i <= 100; i++) {
                //loginBar.setProgress(i);
                //在子线程中发送消息
                //handler.sendEmptyMessage(i);
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            isUser();
        }
    }

    private void isUser(){
        if(name.getText().toString().trim().equals(LOGIN_NAME)&&pswd.getText().toString().trim().equals(LOGIN_PSWD)){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra(BTN_LOGIN, "login_success");
            startActivity(intent);
            //Toasty.success(LoginActivity.this.getApplicationContext(),"Login successfully!",Toasty.LENGTH_SHORT).show();
        }else{
            reInitForWrong();
            Toasty.error(LoginActivity.this.getApplicationContext(),"用户名或密码错误",Toasty.LENGTH_SHORT).show();
        }
    }
}
