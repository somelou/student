package com.example.student.list;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.student.MainActivity;
import com.example.student.R;
import com.example.student.bean.Student;
import com.example.student.student.CallBackValue;
import com.example.student.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListStuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListStuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListStuFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static final String RESULT_QUERY = "QUERY";
    public static final String RESULT_ALL = "ALL";
    public DatabaseHelper dbHelper;
    private CallBackValue callBackValue;

    ListView listView;
    ArrayList<Student> listStuInfo;
    StudentListAdapter adapter = null;

    private OnFragmentInteractionListener mListener;

    public ListStuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListStuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListStuFragment newInstance(String param1, String param2) {
        ListStuFragment fragment = new ListStuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /**
     * 初始化
     */
    private void initMain(View view) {
        listView = view.findViewById(R.id.listStudentInfo);
        listStuInfo = new ArrayList<>();
        adapter = new StudentListAdapter(getActivity(), R.layout.fragment_student_item, listStuInfo);
        listView.setAdapter(adapter);

        dbHelper = new DatabaseHelper(getContext());

        updateStuListData(RESULT_ALL, null);

    }

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        assert inflater != null;
        return inflater.inflate(R.layout.fragment_list_stu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        initMain(view);
        //长按
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                CharSequence[] items = {"Update", "Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                dialog.setTitle("Choose an action")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                if (item == 0) {
                                    // update
                                    showDialogUpdate(listStuInfo.get(position).getStuID());
                                } else {
                                    // delete
                                    showDialogDelete(listStuInfo.get(position).getStuID());
                                }
                            }
                        })
                        .show();
                return true;
            }
        });
    }

    /**
     * 在对话框点击编辑,把student的值从Fragment传到Activity
     *
     * @param idStudent long
     */
    private void showDialogUpdate(final long idStudent) {
        Student student = dbHelper.getOneData(idStudent);
        callBackValue.SendValueFromStudentMessage(student, 1);
        //Intent intent = new Intent();
        //intent.setClass(Objects.requireNonNull(getActivity()).getApplicationContext(), StudentActivity.class);
        //intent.putExtra(BTN_UPDATE, student);
        //startActivity(intent);
        //getActivity().finish();
    }

    /**
     * 在对话框点击删除
     *
     * @param idStudent long
     */
    private void showDialogDelete(final long idStudent) {
        System.out.println("delete stu id:" + idStudent);
        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        dialogDelete.setTitle("Warning")
                .setMessage("Are you sure to delete this student?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            dbHelper.deleteStudentInfo(idStudent);
                            Toasty.success(Objects.requireNonNull(getContext()), "Delete successfully!").show();
                        } catch (Exception e) {
                            Log.e("error", e.getMessage());
                        }
                        //删除成功后刷新
                        updateStuListData(RESULT_ALL, null);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * get data from sqlite.
     *
     * @param kind      String
     * @param condition String
     */
    public void updateStuListData(String kind, String condition) {
        listStuInfo.clear();
        if (kind == null || kind.equals(RESULT_ALL)) {
            listStuInfo = dbHelper.getAllData(listStuInfo);
        } else if (kind.equals(RESULT_QUERY)) {
            if(condition!=null) {
                listStuInfo = dbHelper.getQueryData(listStuInfo, condition);
                showToastyForListResult(listStuInfo);
            }
        }
        if(listStuInfo.isEmpty()){
            listStuInfo = dbHelper.getAllData(listStuInfo);
        }
        adapter.notifyDataSetChanged();
    }

    private void showToastyForListResult(ArrayList<Student> listStuInfo) {
        if (listStuInfo.isEmpty()) {
            Toasty.error(Objects.requireNonNull(getContext()), "No result found!").show();
        } else {
            Toasty.success(Objects.requireNonNull(getContext()), "Query successfully!").show();
        }
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
        callBackValue = (CallBackValue) getActivity();
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
        //加载数据
        Map<String, String> map = ((MainActivity) Objects.requireNonNull(getContext())).getListConditionToList();
        if (map != null) {
            updateStuListData(map.get("kind"), map.get("condition"));
        }
        super.onResume();
    }
}
