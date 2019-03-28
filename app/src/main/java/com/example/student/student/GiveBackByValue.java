package com.example.student.student;

import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.student.R;

import java.util.List;


/**
 * @description 根据View类型和数据库中的值传回form
 * @author somelou
 * @date 2019/3/20
 */
public class GiveBackByValue {

    public GiveBackByValue(){}

    /*根据值设置radio默认选中
    * 不能通用
     * @param radioGroup
     * @param value
     */
    public void setRadioSelectedByValue(RadioGroup radioGroup, String value){
        if (value.equals("男")) {
            radioGroup.check(R.id.radioButtonMale);
        } else {
            radioGroup.check(R.id.radioButtonFemale);
        }
    }

    /*根据值设置checkbox默认选中
     * @param checklist
     * @param value
     */
    public void setCheckBoxSelectedByValue(List<CheckBox> checklist, String value) {
        if(value==null){
            for (CheckBox checkbox : checklist) {
                    checkbox.setChecked(false);
            }
        }else {
            String[] oldHobby = value.split(",");
            for (String item : oldHobby) {
                for (CheckBox checkbox : checklist) {
                    if (checkbox.getText().toString().equals(item)) {
                        checkbox.setChecked(true);
                    }
                }
            }
        }
    }

    /* 根据值, 设置spinner默认选中:
     * @param spinner
     * @param value
     */
    public void setSpinnerItemSelectedByValue(Spinner spinner, String value) {
        SpinnerAdapter apsAdapter = spinner.getAdapter(); //得到SpinnerAdapter对象
        int k = spinner.getCount();
        //System.out.println("spinner items count:" + k);
        for (int i = 0; i < k; i++) {
            if (value.equals(apsAdapter.getItem(i).toString())) {
                //System.out.println("spinner has items,"+i+":"+apsAdapter.getItem(i).toString());
                //((BaseAdapter) apsAdapter).notifyDataSetChanged();
                spinner.setSelection(i,true);// 默认选中项
                //count++;
                //System.out.println("setSpinnerItemSelectedByValue has used "+count+" times, text is "+spinner.getSelectedItem().toString());
                break;
            }
        }

    }
}
