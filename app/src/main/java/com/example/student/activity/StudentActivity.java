package com.example.student.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.student.R;
import com.example.student.bean.Student;
import com.example.student.util.CollageSelectedListener;
import com.example.student.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class StudentActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    static String TAG;

    EditText editName, editID, editBirthday;
    RadioGroup radioFender;
    RadioButton male, female;

    Spinner spinnerCollage, spinnerSpeciality;

    CheckBox[] checkBox;
    List<CheckBox> checkBoxList = new ArrayList<CheckBox>();
    Button btnSubmit;

    String fender;

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        init();

        //跳出DatePickerDialog
        editBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()==R.id.editStuBirthday){
                    Calendar calendar=Calendar.getInstance();
                    DatePickerDialog dialog = new DatePickerDialog(StudentActivity.this,StudentActivity.this,
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH));
                    dialog.show();
                }
            }
        });
        //接收参数
        Intent i = getIntent();
        Student student = i.getParcelableExtra(MainActivity.BTN_UPDATE);
        if (student != null) {
            TAG = "UPDATE";
            initUpdateValue(student);
            System.out.println("in update activity, stu id=" + student.getStuID());
        } else {
            TAG = "ADD";
        }

        //二级联动
        spinnerCollage.setOnItemSelectedListener(new CollageSelectedListener(spinnerSpeciality));
//
//        TextView tv = findViewById(R.id.tv);
//        tv.setText("姓名:" + student.getName() + "--年龄:" + student.getAge());

        //通过RadioGroup的setOnCheckedChangeListener（）来监听选中哪一个单选按钮
        radioFender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                fender = selectRadioButton();
            }

            private String selectRadioButton() {
                //通过radioGroup.getCheckedRadioButtonId()来得到选中的RadioButton的ID，从而得到RadioButton进而获取选中值
                RadioButton rb = findViewById(radioFender.getCheckedRadioButtonId());
                return rb.getText().toString();
            }
        });

        //提交信息至数据库
        //databaseHelper=new DatabaseHelper(StudentActivity.this);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //编辑
                if (StudentActivity.TAG.equals("UPDATE")) {
                    dbHelper.updateStudentInfo(
                            Long.valueOf(editID.getText().toString().trim()),
                            editName.getText().toString().trim(),
                            fender,
                            spinnerCollage.getSelectedItem().toString().trim(),
                            spinnerSpeciality.getSelectedItem().toString().trim(),
                            getHobby(),
                            editBirthday.getText().toString().trim()
                    );
                    Toasty.success(getApplicationContext(), "Update successfully!", Toast.LENGTH_SHORT, true).show();
                    //Toast.makeText(getApplicationContext(), "Update successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(StudentActivity.this, MainActivity.class);
                    startActivity(intent);
                } else
                    //添加
                    if (StudentActivity.TAG.equals("ADD")) {
                    dbHelper.insertStudentInfo(
                            Long.valueOf(editID.getText().toString().trim()),
                            editName.getText().toString().trim(),
                            fender,
                            spinnerCollage.getSelectedItem().toString().trim(),
                            spinnerSpeciality.getSelectedItem().toString().trim(),
                            getHobby(),
                            editBirthday.getText().toString().trim()
                    );
                    Toasty.success(getApplicationContext(), "Add successfully!", Toast.LENGTH_SHORT, true).show();
                    Intent intent = new Intent(StudentActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        });

    }

    private String getHobby() {

        StringBuilder sb = new StringBuilder();
        //遍历集合中的checkBox,判断是否选择，获取选中的文本
        for (CheckBox checkbox : checkBoxList) {
            if (checkbox.isChecked()) {
                sb.append(checkbox.getText().toString() + ",");
            }
        }
        if ("".equals(sb.toString())) {
            Toast.makeText(getApplicationContext(), "请至少选择一个", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show();
        }
        return sb.toString();
    }


    private void init() {

        dbHelper=new DatabaseHelper(getApplicationContext());

        editName = findViewById(R.id.editStuName);
        editID = findViewById(R.id.editStuNo);
        radioFender = findViewById(R.id.buttonGroupFender);
        male = findViewById(R.id.radioButtonMale);
        female = findViewById(R.id.radioButtonFemale);

        editBirthday = findViewById(R.id.editStuBirthday);

        spinnerCollage = findViewById(R.id.spinnerCollage);
        spinnerSpeciality = findViewById(R.id.spinnerSpeciality);

        checkBox = new CheckBox[4];
        checkBox[0] = findViewById(R.id.checkLiterature);
        checkBox[1] = findViewById(R.id.checkPE);
        checkBox[2] = findViewById(R.id.checkMusic);
        checkBox[3] = findViewById(R.id.checkArt);
        // 将所有的checkbox放到一个集合中
        for (int i = 0; i < 4; i++) {
            checkBoxList.add(checkBox[i]);
        }

        btnSubmit = findViewById(R.id.buttonSubmit);
    }

    private void initUpdateValue(Student stu) {
        editName.setText(stu.getStuName());

        editID.setText(String.valueOf(stu.getStuID()));
        editID.setFocusable(false);

        fender=stu.getStuFender();//因为radioButton根据改变select来获取值，直接set会让fender为null
        setRadioSelectedByValue(radioFender,fender);

        setSpinnerItemSelectedByValue(spinnerCollage, stu.getStuCollage());
        setSpinnerItemSelectedByValue(spinnerSpeciality, stu.getStuSpeciality());

        setCheckBoxSelectedByValue(checkBoxList, stu.getStuHobby());

        editBirthday.setText(stu.getStuBirthday());
    }

    private void setRadioSelectedByValue(RadioGroup radioGroup,String value){
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
    private void setCheckBoxSelectedByValue(List<CheckBox> checklist, String value) {
        String[] oldHobby = value.split(",");
        for (String item : oldHobby) {
            for (CheckBox checkbox : checklist) {
                if (checkbox.getText().toString().equals(item)) {
                    checkbox.setChecked(true);
                }
            }
        }
    }

    /* 根据值, 设置spinner默认选中:
     * @param spinner
     * @param value
     */
    private void setSpinnerItemSelectedByValue(Spinner spinner, String value) {
        SpinnerAdapter apsAdapter = spinner.getAdapter(); //得到SpinnerAdapter对象
        int k = spinner.getCount();
        System.out.println("spinner items count:" + k);
        for (int i = 0; i < k; i++) {
            if (value.equals(apsAdapter.getItem(i).toString())) {
                spinner.setSelection(i, true);// 默认选中项
                break;
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String desc=year+"-"+(month+1)+"-"+dayOfMonth;
        editBirthday.setText(desc);
    }
}
