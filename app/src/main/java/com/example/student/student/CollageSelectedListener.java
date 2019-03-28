package com.example.student.student;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.student.R;

/**
 * @description 学院专业二级联动监听器
 * @author somelou
 * @date 2019/3/20
 *
 */
public class CollageSelectedListener implements AdapterView.OnItemSelectedListener {

    private static int count=0;
    private static String TAG;

    private Spinner speciality;
    private ArrayAdapter<CharSequence> adapterSpeciality;

    /**
     *
     * @param spinner Spinner
     */
    public CollageSelectedListener(Spinner spinner,String tag) {
        TAG=tag;
        setSpeciality(spinner);
    }

    /**当
     * 
     * @param parent AdapterView<?>
     * @param view View
     * @param position 位置
     * @param id 学号
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(TAG==null||TAG.equals("UPDATE")){
            TAG="ADD";
            return;
        }
        if(TAG.equals("ADD")) {
            String str = (String) (parent.getItemAtPosition(position));
            //System.out.println("choosen:"+str);
            if (str.equals(parent.getResources().getString(R.string.speciality_cs))) {
                loadSpinnerSpecialityCS(parent);
            } else if (str.equals((parent.getResources().getString(R.string.speciality_eg)))) {
                loadSpinnerSpecialityEG(parent);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * 设置被联动的Spinner
     * @param spinner Spinner
     */
    private void setSpeciality(Spinner spinner) {
        speciality = spinner;
    }

    public static String getTAG(){
        return TAG;
    }

    /**
     * 加载计算机学院专业
     * @param parent 父适配器
     */
    private void loadSpinnerSpecialityCS(AdapterView<?> parent){
        adapterSpeciality=ArrayAdapter.createFromResource(parent.getContext(),R.array.spinner_speciality_cs,R.layout.support_simple_spinner_dropdown_item);
        speciality.setAdapter(adapterSpeciality);
        //使用res目录下的string.xml资源
        //speciality.getResources().getStringArray(R.array.spinner_speciality_cs);
    }

    /**
     * 加载电气学院专业
     * @param parent 父适配器
     */
    private void loadSpinnerSpecialityEG(AdapterView<?> parent){
        //使用res目录下的string.xml资源
        //String[] array=new String[]{"请选择","eg1","eg2"};s
        adapterSpeciality=ArrayAdapter.createFromResource(parent.getContext(),R.array.spinner_speciality_eg,android.R.layout.simple_spinner_dropdown_item);
        speciality.setAdapter(adapterSpeciality);
        //speciality.getResources().getStringArray(R.array.spinner_speciality_eg);
    }

}
