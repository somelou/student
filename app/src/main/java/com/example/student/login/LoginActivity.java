package com.example.student.login;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.student.MainActivity;
import com.example.student.R;
import com.example.student.ad.AdActivity;
import com.example.student.util.SharedPreferencesUtils;

import es.dmoral.toasty.Toasty;

/**
 * @author somelou
 * @description 登陆界面 buttonLogin
 * @date 2019/3/20
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    final static String LOGIN_NAME = "admin";
    final static String LOGIN_PSWD = "123456";//"E10ADC3949BA59ABBE56E057F20F883E";

    static String BTN_LOGIN = "LOGIN_SUCCESS";

    private enum IS_AUTO {AUTO,NOT_AUTO}
    private enum IS_IN {IN_START, IN_MY}

    IS_IN FROM_WHICH;

    ProgressBar loginBar;


    //布局内的控件
    private EditText inputName;
    private EditText inputPassword;
    private Button buttonLogin;
    private CheckBox rememberKey;
    private CheckBox keepLogin;
    private ImageView seeOrNotKey;

    private LoadingDialog mLoadingDialog; //显示正在加载的对话框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initLogin();
        setupEvents();
        initData();
    }

    /**
     * 初始化
     */
    private void initLogin() {
        loginBar = findViewById(R.id.progressBarLogin);
        inputName = findViewById(R.id.editLoginName);
        inputPassword = findViewById(R.id.editLoginPassword);
        buttonLogin = findViewById(R.id.btnLogin);
        rememberKey = findViewById(R.id.checkRememberKey);
        keepLogin = findViewById(R.id.checkKeepLogin);
        seeOrNotKey = findViewById(R.id.iv_see_password);

        Intent intent=getIntent();
        String from=intent.getStringExtra(MainActivity.FROM_ME);
        System.out.println("from activity:"+FROM_WHICH);
        if(from!=null){
            FROM_WHICH=IS_IN.IN_MY;
        }else{
            FROM_WHICH=IS_IN.IN_START;
        }
    }

    /**
     * 当用户名或密码错误之后
     */
    private void reInitForWrong() {
        loginBar.setVisibility(View.INVISIBLE);
        //inputName.setText(null);
        //inputPassword.setText(null);
        buttonLogin.setClickable(true);
    }

    /**
     *
     */
    private void initData() {
        //判断用户第一次登陆
        if (firstLogin()) {
            rememberKey.setChecked(false);//取消记住密码的复选框
            keepLogin.setChecked(false);//取消自动登录的复选框
        }
        //判断是否记住密码
        if (rememberPassword()) {
            rememberKey.setChecked(true);//勾选记住密码
            setTextNameAndPassword();//把密码和账号输入到输入框中
        } else {
            setTextName();//把用户账号放到输入账号的输入框中
        }
        //判断是否自动登录
        if (autoLogin()) {
            rememberKey.setChecked(true);
            keepLogin.setChecked(true);
            loginClick(IS_AUTO.AUTO,FROM_WHICH);//去登录就可以
        }
    }

    /**
     * 把本地保存的数据设置数据到输入框中
     */
    public void setTextNameAndPassword() {
        inputName.setText(getLocalName());
        inputPassword.setText(getLocalPassword());
    }

    /**
     * 设置数据到输入框中
     */
    public void setTextName() {
        inputName.setText(getLocalName());
    }


    /**
     * 获得保存在本地的用户名
     */
    public String getLocalName() {
        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        return helper.getString("name");
    }


    /**
     * 获得保存在本地的密码
     * @return password
     */
    public String getLocalPassword() {
        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        String password = helper.getString("password");
        return Base64Utils.decryptBASE64(password);   //解码一下
    }

    /**
     * 判断是否自动登录
     * @return IsAuto
     */
    private boolean autoLogin() {
        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        return helper.getBoolean("autoLogin", false);
    }

    /**
     * 判断是否记住密码
     * @return IsRemember
     */
    private boolean rememberPassword() {
        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        return helper.getBoolean("rememberPassword", false);
    }

    /**
     * 设置监听器
     */
    private void setupEvents() {
        buttonLogin.setOnClickListener(this);
        rememberKey.setOnCheckedChangeListener(this);
        keepLogin.setOnCheckedChangeListener(this);
        seeOrNotKey.setOnClickListener(this);
    }

    /**
     * 判断用户输入是否正确并产生对于动作
     *
     * @param view View
     */
    public void loginToMainActivity(View view) {
        //给登录按钮注册监听器,实现监听器接口，编写事件

        //获取用户输入的数据
        Handler handler = new Handler();
        //显示进度条
        loginBar.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (inputName.getText().toString().trim().equals(LOGIN_NAME) && inputPassword.getText().toString().trim().equals(LOGIN_PSWD)) {
                    Toasty.success(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                    Intent toMain = new Intent(LoginActivity.this, MainActivity.class);
                    // 把 username 传入主界面
                    toMain.putExtra(BTN_LOGIN, inputName.getText().toString().trim());
                    startActivity(toMain);
                    finish();
                } else {
                    Toasty.error(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    reInitForWrong();
                }
            }
        }, 3000);

    }


    /**
     * 判断是否是第一次登陆
     */
    private boolean firstLogin() {
        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        boolean first = helper.getBoolean("first", true);
        if (first) {
            //创建一个ContentVa对象（自定义的）设置不是第一次登录，,并创建记住密码和自动登录是默认不选，创建账号和密码为空
            helper.putValues(new SharedPreferencesUtils.ContentValue("first", false),
                    new SharedPreferencesUtils.ContentValue("rememberPassword", false),
                    new SharedPreferencesUtils.ContentValue("autoLogin", false),
                    new SharedPreferencesUtils.ContentValue("name", null),
                    new SharedPreferencesUtils.ContentValue("password", null));
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                loadUserName();    //无论如何保存一下用户名
                loginClick(IS_AUTO.NOT_AUTO,FROM_WHICH); //登陆
                break;
            case R.id.iv_see_password:
                setPasswordVisibility();    //改变图片并设置输入框的文本可见或不可见
                break;
        }
    }

    /**
     * 模拟登录情况
     * 用户名csdn，密码123456，就能登录成功，否则登录失败
     */
    private void loginClick(IS_AUTO isauto,IS_IN is_in) {

        System.out.println("when login. isauto:"+isauto+", isin:"+is_in);
        //先做一些基本的判断，比如输入的用户命为空，密码为空，网络不可用多大情况，都不需要去链接服务器了，而是直接返回提示错误
        if (getAccount().isEmpty()){
            showErrorToast("你输入的账号为空！");
            return;
        }

        if (getPassword().isEmpty()){
            showErrorToast("你输入的密码为空！");
            return;
        }
        switch (isauto){
            case NOT_AUTO:
                switch (is_in){
                    case IN_MY:
                        loginThread(0,is_in);
                        break;
                    case IN_START:
                        showLoading();//显示加载框
                        loginThread(3000,is_in);
                        break;
                }
////                if(is_in==IS_IN.IN_MY){
////                    loginThread(0,is_in);
////                }else {
//                    showLoading();//显示加载框
//                    loginThread(3000, is_in);
//                //}
                break;
            case AUTO:
                loginThread(0,is_in);
                break;
        }
    }


    private void loginThread(final int mills, final IS_IN is_in){
        //登录一般都是请求服务器来判断密码是否正确，要请求网络，要子线程
        Thread loginRunnable = new Thread() {

            @Override
            public void run() {
                super.run();
                setLoginBtnClickable(false);//点击登录后，设置登录按钮不可点击状态

                //睡眠mills/1000秒
                try {
                    Thread.sleep(mills);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //判断账号和密码
                if (getAccount().equals(LOGIN_NAME) && getPassword().equals(LOGIN_PSWD)) {
                    showSuccessToast("登录成功");
                    loadCheckBoxState();//记录下当前用户记住密码和自动登录的状态;
                    finish();//关闭页面
                    if(is_in==IS_IN.IN_MY){
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else{
                        startActivity(new Intent(LoginActivity.this, AdActivity.class));
                    }

                } else {
                    showErrorToast("输入的登录账号或密码不正确");
                    reInitForWrong();
                }
                setLoginBtnClickable(true);  //这里解放登录按钮，设置为可以点击
                hideLoading();//隐藏加载框
            }
        };
        loginRunnable.start();
    }



    /**
     * 保存用户账号
     */
    public void loadUserName() {
        if (!getAccount().equals("") || !getAccount().equals("请输入登录账号")) {
            SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
            helper.putValues(new SharedPreferencesUtils.ContentValue("name", getAccount()));
        }

    }

    /**
     * 设置密码可见和不可见的相互转换
     */
    private void setPasswordVisibility() {
        if (seeOrNotKey.isSelected()) {
            seeOrNotKey.setSelected(false);
            //密码不可见
            inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        } else {
            seeOrNotKey.setSelected(true);
            //密码可见
            inputPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }

    }

    /**
     * 获取账号
     */
    public String getAccount() {
        return inputName.getText().toString().trim();
    }

    /**
     * 获取密码
     */
    public String getPassword() {
        return inputPassword.getText().toString().trim();
    }


    /**
     * 保存用户选择“记住密码”和“自动登陆”的状态
     */
    private void loadCheckBoxState() {
        loadCheckBoxState(rememberKey, keepLogin);
    }

    /**
     * 保存按钮的状态值
     */
    public void loadCheckBoxState(CheckBox checkBox_password, CheckBox checkBox_login) {

        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");

        //如果设置自动登录
        if (checkBox_login.isChecked()) {
            //创建记住密码和自动登录是都选择,保存密码数据
            helper.putValues(
                    new SharedPreferencesUtils.ContentValue("rememberPassword", true),
                    new SharedPreferencesUtils.ContentValue("autoLogin", true),
                    new SharedPreferencesUtils.ContentValue("password", Base64Utils.encryptBASE64(getPassword())));

        } else if (!checkBox_password.isChecked()) { //如果没有保存密码，那么自动登录也是不选的
            //创建记住密码和自动登录是默认不选,密码为空
            helper.putValues(
                    new SharedPreferencesUtils.ContentValue("rememberPassword", false),
                    new SharedPreferencesUtils.ContentValue("autoLogin", false),
                    new SharedPreferencesUtils.ContentValue("password", ""));
        } else if (checkBox_password.isChecked()) {   //如果保存密码，没有自动登录
            //创建记住密码为选中和自动登录是默认不选,保存密码数据
            helper.putValues(
                    new SharedPreferencesUtils.ContentValue("rememberPassword", true),
                    new SharedPreferencesUtils.ContentValue("autoLogin", false),
                    new SharedPreferencesUtils.ContentValue("password", Base64Utils.encryptBASE64(getPassword())));
        }
    }

    /**
     * 是否可以点击登录按钮
     *
     * @param clickable
     */
    public void setLoginBtnClickable(boolean clickable) {
        buttonLogin.setClickable(clickable);
    }


    /**
     * 显示加载的进度款
     */
    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this, getString(R.string.loading_prompt), false);
        }
        mLoadingDialog.show();
    }


    /**
     * 隐藏加载的进度框
     */
    public void hideLoading() {
        if (mLoadingDialog != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoadingDialog.hide();
                }
            });
        }
    }


    /**
     * CheckBox点击时的回调方法 ,不管是勾选还是取消勾选都会得到回调
     *
     * @param buttonView 按钮对象
     * @param isChecked  按钮的状态
     */
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == rememberKey) {  //记住密码选框发生改变时
            if (!isChecked) {   //如果取消“记住密码”，那么同样取消自动登陆
                keepLogin.setChecked(false);
            }
        } else if (buttonView == keepLogin) {   //自动登陆选框发生改变时
            if (isChecked) {   //如果选择“自动登录”，那么同样选中“记住密码”
                rememberKey.setChecked(true);
            }
        }
    }


    /**
     * 监听回退键
     */
    @Override
    public void onBackPressed() {
        if (mLoadingDialog != null) {
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.cancel();
            } else {
                finish();
            }
        } else {
            finish();
        }

    }

    /**
     * 页面销毁前回调的方法
     */
    protected void onDestroy() {
        if (mLoadingDialog != null) {
            mLoadingDialog.cancel();
            mLoadingDialog = null;
        }
        super.onDestroy();
    }


    public void showSuccessToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toasty.success(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showErrorToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toasty.error(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
