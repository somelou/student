package com.example.student.phone;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.student.R;
import com.example.student.util.HttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class PhonePlaceActivity extends AppCompatActivity {

    private EditText editPhone,editPlace;
    private Button buttonQuery;

    private String place;

    private static String PHONE_PLACE_URL = "http://mobsec-dianhua.baidu.com/dianhua_api/open/location?tel=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_place);
        initPhonePlace();

        buttonQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = editPhone.getText().toString().trim();
                if (!phoneNumber.equals("")) {
                    final String url =  PHONE_PLACE_URL + phoneNumber;
                    place=getPhonePlace(url,phoneNumber);
                }
                Log.d("Phone",place);
                editPlace.setText(place);
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText(null, place);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                // 获取剪贴板的剪贴数据集
//                ClipData clipData = cm.getPrimaryClip();
//
//                if (clipData != null && clipData.getItemCount() > 0) {
//                    // 从数据集中获取（粘贴）第一条文本数据
//                    CharSequence text = clipData.getItemAt(0).getText();
//                    System.out.println("text: " + text);
//                }
            }
        });
    }


    private String getPhonePlace(final String url,final String phoneNumber) {
        // 非数组型变量会报错
        final String[] location = new String[1];
        final CountDownLatch cdl = new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    location[0] = getLocationFromURL(url,phoneNumber);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                cdl.countDown();
            }
        }).start();
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return location[0];
    }

    private String getLocationFromURL(final String url,final String phoneNumber) throws IOException, JSONException {
        JSONObject jsonObject = HttpHelper.readJsonFromUrl(url);
        JSONObject response=jsonObject.getJSONObject("response");
        JSONObject number=response.getJSONObject(phoneNumber);
        return number.getString("location");
    }

    private void initPhonePlace(){
        editPhone=findViewById(R.id.editPhone);
        editPlace=findViewById(R.id.editPlace);
        //editPlace.setEnabled(false);
        buttonQuery=findViewById(R.id.buttonQueryPlace);
    }

}
