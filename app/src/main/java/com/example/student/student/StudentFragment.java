package com.example.student.student;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.example.student.util.DatabaseHelper;

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
 * {@link StudentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StudentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "BIRTHDAY";
    private static final String ARG_PARAM2 = "Student";

    // TODO: Rename and change types of parameters
    private String day;
    private Student student;

    private DatabaseHelper dbHelper;

    static String TAG;

    EditText editName, editID, editBirthday;
    RadioGroup radioFender;
    RadioButton male, female;
    Spinner spinnerCollage, spinnerSpeciality;
    CheckBox[] checkBox;
    List<CheckBox> checkBoxList = new ArrayList<CheckBox>();
    Button btnSubmit, btnCancel;

    String fender;
    GiveBackByValue giveBackByValue;

    private OnFragmentInteractionListener mListener;

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
    public static StudentFragment newInstance(String day, Student student) {
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student, container, false);
        initStudent(view);
        fromWhichAction();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {

        //二级联动
        spinnerCollage.setOnItemSelectedListener(new CollageSelectedListener(spinnerSpeciality));

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
                    String getFender = rb.getText().toString();
                    return getFender;
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
            }
        });

        //提交信息至数据库
        //databaseHelper=new DatabaseHelper(StudentActivity.this);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    } catch (SQLException e) {
                        Toasty.error(getContext(), "更新出现了问题").show();
                    }
                    Toasty.success(getContext(), "Update successfully!", Toast.LENGTH_SHORT, true).show();
                    //Toast.makeText(getApplicationContext(), "Update successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
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
                        } catch (SQLException e) {
                            Toasty.error(getContext(), "插入出现了问题").show();
                        }
                        Toasty.success(getContext(), "Add successfully!", Toast.LENGTH_SHORT, true).show();
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                    }

            }
        });
    }

    /**
     * 初始化
     */
    private void initStudent(View view) {

        dbHelper = new DatabaseHelper(view.getContext());
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
     * 根据传来的student赋值回form.
     *
     * @param stu 需要编辑的Student
     */
    private void initUpdateValue(Student stu) {
        editName.setText(stu.getStuName());

        editID.setText(valueOf(stu.getStuID()));
        editID.setFocusable(false);

        //因为radioButton根据改变select来获取值，直接set会让fender为null
        fender = stu.getStuFender();
        giveBackByValue.setRadioSelectedByValue(radioFender, fender);

        giveBackByValue.setSpinnerItemSelectedByValue(spinnerCollage, stu.getStuCollage());

        spinnerCollage.setOnItemSelectedListener(new CollageSelectedListener(spinnerSpeciality));
        System.out.println("get stu speciality in list:" + stu.getStuSpeciality());
        giveBackByValue.setSpinnerItemSelectedByValue(spinnerSpeciality, stu.getStuSpeciality());
        spinnerSpeciality.invalidate();
        System.out.println("spinner check item in fragment:" + spinnerSpeciality.getSelectedItem().toString().trim());

        giveBackByValue.setCheckBoxSelectedByValue(checkBoxList, stu.getStuHobby());

        editBirthday.setText(stu.getStuBirthday());
    }

    /**
     * 判断动作来源，并产生对应反应
     */
    private void fromWhichAction() {

        student = ((MainActivity) Objects.requireNonNull(getActivity())).getStudentToUpdate();
        if (student != null) {
            System.out.println("(on init)get stu from activity in update, name:" + student.getStuName());
        }

        //如果接受的参数不为空，说明来自addButton
        if (student != null) {
            TAG = "UPDATE";
            initUpdateValue(student);
            //System.out.println("in update activity, stu id=" + student.getStuID());
        } else {
            System.out.println("no student in student fragment");
            TAG = "ADD";
        }
    }

    private void reSetOriginalView() {
        editName.setText(null);

        editID.setText(null);
        editID.setFocusable(true);

        radioFender.clearCheck();
        spinnerCollage.setSelection(0, true);
        spinnerSpeciality.setSelection(0, true);

        giveBackByValue.setCheckBoxSelectedByValue(checkBoxList, null);

        editBirthday.setText(null);

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


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //callBackValue= (CallBackValue) getActivity();

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        //spinnerCollage.setOnItemSelectedListener(new CollageSelectedListener(spinnerSpeciality));
        fromWhichAction();
        super.onResume();

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        student = null;
    }

}
