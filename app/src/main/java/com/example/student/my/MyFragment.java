package com.example.student.my;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.student.MainActivity;
import com.example.student.R;
import com.example.student.login.LoginActivity;
import com.example.student.util.SharedPreferencesUtils;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private Button login,logout;

    public MyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment MyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyFragment newInstance(String param1) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout-port for this fragment
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        login=view.findViewById(R.id.buttonGotoLogin);
        logout=view.findViewById(R.id.buttonLogout);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLogin(v);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLogout(v);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    /**
     * 返回登录界面，不消除用户和密码
     */
    public void toLogin(View view) {
        //不用自动登录就可
        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        SharedPreferencesUtils helper = new SharedPreferencesUtils(view.getContext(), "setting");
        //创建记住密码和自动登录是默认不选,密码为空
        helper.putValues(new SharedPreferencesUtils.ContentValue("autoLogin", false));
        Intent intent=new Intent();
        intent.setClass(Objects.requireNonNull(getContext()), LoginActivity.class);
        intent.putExtra(MainActivity.FROM_ME,"MY");
        startActivity(intent);
    }

    /**
     * 返回登录界面，消除用密码
     */
    public void toLogout(View view) {
        //置空密码即可
        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        SharedPreferencesUtils helper = new SharedPreferencesUtils(view.getContext(), "setting");
        //创建记住密码和自动登录是默认不选,密码为空
        helper.putValues(
                new SharedPreferencesUtils.ContentValue("rememberPassword", false),
                new SharedPreferencesUtils.ContentValue("autoLogin", false),
                new SharedPreferencesUtils.ContentValue("password", null));
//        Intent intent= new Intent(this,LoginActivity.class);
//        intent.putExtra(FROM_ME,"MY");
        Intent intent=new Intent();
        intent.setClass(Objects.requireNonNull(getActivity()).getApplicationContext(),LoginActivity.class);
        startActivity(intent);
    }
}
