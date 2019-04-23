package com.example.student.student;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.student.MainActivity;
import com.example.student.R;
import com.example.student.bean.Student;
import com.example.student.db.StudentDAL;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

import static java.lang.String.valueOf;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link StudentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "BIRTHDAY";

    // TODO: Rename and change types of parameters
    private String day;

    private StudentDAL dbHelper;

    String TAG;

    private EditText editName, editID, editBirthday;
    private RadioGroup radioFender;
    RadioButton male, female;
    private Spinner spinnerCollage, spinnerSpeciality;
    CheckBox[] checkBox;
    private List<CheckBox> checkBoxList = new ArrayList<CheckBox>();
    private Button btnSubmit, btnCancel;

    String fender;

    GiveBackByValue giveBackByValue;

    public StudentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param day Parameter 1.
     * @return A new instance of fragment StudentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentFragment newInstance(String day) {
        StudentFragment fragment = new StudentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, day);
        //args.putBundle(ARG_PARAM2,(Bundle)student);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            day = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout-port for this fragment
        View view = inflater.inflate(R.layout.fragment_student, container, false);
        initStudent(view);
        fromWhichAction();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) throws NumberFormatException {

        //点击跳出DatePickerDialog
        editBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.editStuBirthday) {
                    Calendar calendar = Calendar.getInstance();
                    DatePickerDialog dialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String setDay = year + "-" + (month + 1) + "-" + dayOfMonth;
                            editBirthday.setText(setDay);
                        }
                    },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH));
                    dialog.show();
                }
            }
        });

        //通过RadioGroup的setOnCheckedChangeListener（）来监听选中哪一个单选按钮
        radioFender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                fender = selectRadioButton();
            }

            private String selectRadioButton() throws NullPointerException {
                //通过radioGroup.getCheckedRadioButtonId()来得到选中的RadioButton的ID，从而得到RadioButton进而获取选中值
                try {
                    RadioButton rb = view.findViewById(radioFender.getCheckedRadioButtonId());
                    return rb.getText().toString();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reSetOriginalView();
                onResume();
            }
        });

        //提交信息至数据库
        //databaseHelper=new DatabaseHelper(StudentActivity.this);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success = true;
                //编辑
                if (TAG.equals("UPDATE")) {
                    try {
                        dbHelper.updateStudentInfo(
                                Long.valueOf(editID.getText().toString().trim()),
                                editName.getText().toString().trim(),
                                fender,
                                spinnerCollage.getSelectedItem().toString().trim(),
                                spinnerSpeciality.getSelectedItem().toString().trim(),
                                getHobby(),
                                editBirthday.getText().toString().trim()
                        );
                    } catch (NumberFormatException e) {
                        success = false;
                    }
                    isOperationSuccess(success, TAG);
                } else
                    //添加
                    if (TAG.equals("ADD")) {
                        try {
                            dbHelper.insertStudentInfo(
                                    Long.valueOf(editID.getText().toString().trim()),
                                    editName.getText().toString().trim(),
                                    fender,
                                    spinnerCollage.getSelectedItem().toString().trim(),
                                    spinnerSpeciality.getSelectedItem().toString().trim(),
                                    getHobby(),
                                    editBirthday.getText().toString().trim()
                            );
                        } catch (NumberFormatException e) {
                            success = false;
                        }
                        isOperationSuccess(success, TAG);
                    }
            }
        });

        fromWhichAction();
    }

    /**
     * 初始化
     */
    private void initStudent(View view) {

        dbHelper = new StudentDAL(view.getContext());
        giveBackByValue = new GiveBackByValue();

        editName = view.findViewById(R.id.editStuName);
        editID = view.findViewById(R.id.editStuNo);
        radioFender = view.findViewById(R.id.buttonGroupFender);
        male = view.findViewById(R.id.radioButtonMale);
        female = view.findViewById(R.id.radioButtonFemale);

        editBirthday = view.findViewById(R.id.editStuBirthday);

        spinnerCollage = view.findViewById(R.id.spinnerCollage);
        spinnerSpeciality = view.findViewById(R.id.spinnerSpeciality);

        checkBox = new CheckBox[4];
        checkBox[0] = view.findViewById(R.id.checkLiterature);
        checkBox[1] = view.findViewById(R.id.checkPE);
        checkBox[2] = view.findViewById(R.id.checkMusic);
        checkBox[3] = view.findViewById(R.id.checkArt);
        // 将所有的checkbox放到一个集合中
        for (int i = 0; i < 4; i++) {
            Collections.addAll(checkBoxList, checkBox[i]);
        }

        btnCancel = view.findViewById(R.id.buttonCancel);
        btnSubmit = view.findViewById(R.id.buttonSubmit);
    }

    /**
     * 判断动作来源，并产生对应反应
     */
    private void fromWhichAction() {
        System.out.println("TAG :" + TAG);

        Student student = ((MainActivity) Objects.requireNonNull(getActivity())).getStudentToUpdate();
        if (student != null) {
            System.out.println("(on resume)get stu from activity in update, name:" + student.getStuName());
        }

        //如果接受的参数不为空，说明来自addButton
        if (student != null) {
            TAG = "UPDATE";
            spinnerCollage.setOnItemSelectedListener(new CollageSelectedListener(spinnerSpeciality, TAG));
            initUpdateValue(student);
            this.TAG = CollageSelectedListener.getTAG();
            //System.out.println("in update activity, stu id=" + student.getStuID());
        } else {
            System.out.println("no student info in student fragment");
            TAG = "ADD";
            spinnerCollage.setOnItemSelectedListener(new CollageSelectedListener(spinnerSpeciality, TAG));
            //reSetOriginalView();
        }
    }

    /**
     * 根据传来的student赋值回form.
     *
     * @param stu 需要编辑的Student
     */
    private void initUpdateValue(Student stu) {
        editName.setText(stu.getStuName());

        editID.setText(valueOf(stu.getStuID()));
        editID.setEnabled(false);

        //因为radioButton根据改变select来获取值，直接set会让fender为null
        fender = stu.getStuFender();
        giveBackByValue.setRadioSelectedByValue(radioFender, fender);
        giveBackByValue.setSpinnerItemSelectedByValue(spinnerCollage, stu.getStuCollage());
        giveBackByValue.setSpinnerItemSelectedByValue(spinnerSpeciality, stu.getStuSpeciality());
        giveBackByValue.setCheckBoxSelectedByValue(checkBoxList, stu.getStuHobby());

        editBirthday.setText(stu.getStuBirthday());
    }

    /**
     * 重置所有组件
     */
    private void reSetOriginalView() {
        editName.setText(null);

        editID.setText(null);
        editID.setEnabled(true);

        radioFender.clearCheck();
        spinnerCollage.setSelection(0, true);
        spinnerSpeciality.setSelection(0, true);

        giveBackByValue.setCheckBoxSelectedByValue(checkBoxList, null);

        editBirthday.setText(null);
    }

    /**
     * 判断操作是否成功并生成相应吐司
     *
     * @param success boolean
     * @param tag     TAG
     */
    private void isOperationSuccess(boolean success, String tag) {
        if (success) {
            Toasty.success(Objects.requireNonNull(getContext()), tag + "successfully!", Toast.LENGTH_SHORT, true).show();
            //Toast.makeText(getApplicationContext(), "Update successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        } else {
            Toasty.error(Objects.requireNonNull(getContext()), tag + "出现了问题").show();
        }
    }

    /**
     * 得到所有选中的checkbox的值
     *
     * @return StudentHobbyString
     */
    private String getHobby() {

        StringBuilder hobby = new StringBuilder();
        //遍历集合中的checkBox,判断是否选择，获取选中的文本
        for (CheckBox checkbox : checkBoxList) {
            if (checkbox.isChecked()) {
                hobby.append(checkbox.getText().toString());
                hobby.append(",");
            }
        }
        if ("".equals(hobby.toString())) {
            Toast.makeText(getContext(), "请至少选择一个", Toast.LENGTH_SHORT).show();
        }
        return hobby.toString();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        //spinnerCollage.setOnItemSelectedListener(new CollageSelectedListener(spinnerSpeciality));
        fromWhichAction();
        super.onResume();
    }

}
