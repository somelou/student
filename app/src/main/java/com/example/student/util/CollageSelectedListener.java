package com.example.student.util;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.student.R;

/*
* 二级联动
* 根据上一个spinner的选择对下一个spinner的内容进行改变
 */
public class CollageSelectedListener implements AdapterView.OnItemSelectedListener {

    private Spinner speciality;
    private ArrayAdapter<CharSequence> adapterSpeciality;

    public CollageSelectedListener(Spinner spinner) {
        setSpeciality(spinner);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String str = (String)(parent.getItemAtPosition(position));
        //System.out.println("choosen:"+str);
        if (str.equals(parent.getResources().getString(R.string.speciality_cs))) {
            loadSpinnerSpecialityCS(parent);
        }else if(str.equals((parent.getResources().getString(R.string.speciality_eg)))){
            loadSpinnerSpecialityEG(parent);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setSpeciality(Spinner spinner) {
        speciality = spinner;
    }

    //加载计算机学院专业
    private void loadSpinnerSpecialityCS(AdapterView<?> parent){
        adapterSpeciality=ArrayAdapter.createFromResource(parent.getContext(),R.array.spinner_speciality_cs,R.layout.support_simple_spinner_dropdown_item);
        speciality.setAdapter(adapterSpeciality);
        //使用res目录下的string.xml资源
        //speciality.getResources().getStringArray(R.array.spinner_speciality_cs);
    }

    //加载电气学院专业
    private void loadSpinnerSpecialityEG(AdapterView<?> parent){
        //使用res目录下的string.xml资源
        //String[] array=new String[]{"请选择","eg1","eg2"};s
        adapterSpeciality=ArrayAdapter.createFromResource(parent.getContext(),R.array.spinner_speciality_eg,android.R.layout.simple_spinner_dropdown_item);
        speciality.setAdapter(adapterSpeciality);
        //speciality.getResources().getStringArray(R.array.spinner_speciality_eg);
    }

}
