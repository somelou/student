package com.example.student.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.student.R;
import com.example.student.bean.Student;
import com.example.student.util.DatabaseHelper;
import com.example.student.util.StudentListAdapter;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;


public class MainActivity extends AppCompatActivity {

    public static final String BTN_UPDATE = "UPDATE";
    public static final String BTN_ADD = "ADD";

    public static final String RESULT_QUERY = "QUERY";
    public static final String RESULT_ALL = "ALL";
    public DatabaseHelper dbHelper;

    ListView listView;
    ArrayList<Student> listStuInfo;
    StudentListAdapter adapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMain();
        //长按
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                CharSequence[] items = {"Update", "Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

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

    /*
    * 初始化
     */
    private void initMain(){
        listView = findViewById(R.id.listStudentInfo);
        listStuInfo = new ArrayList<>();
        adapter = new StudentListAdapter(this, R.layout.activity_student_item, listStuInfo);
        listView.setAdapter(adapter);

        dbHelper = new DatabaseHelper(this);

        updateStuListData(RESULT_ALL, null);
    }

    /*
    * 转到StudentActivity
     */
    public void turnToAddStuInfo(View view) {
        Intent intent = new Intent(this, StudentActivity.class);
        intent.putExtra(BTN_ADD, "add");
        startActivity(intent);
    }

    /*
    * 在对话框点击编辑
     */
    private void showDialogUpdate(final long idStudent) {
        System.out.println("update stu id:" + idStudent);
        Student student = dbHelper.getOneData(idStudent);
        // Do something in response to button
        System.out.println("after get, update stu id:" + student.getStuID());
        Intent intent = new Intent(this, StudentActivity.class);
        //R是什么？
        intent.putExtra(BTN_UPDATE, student);
        startActivity(intent);

    }

    /*
     * 在对话框点击删除
     */
    private void showDialogDelete(final long idStudent) {
        System.out.println("delete stu id:" + idStudent);
        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(MainActivity.this);

        dialogDelete.setTitle("Warning")
                .setMessage("Are you sure to delete this student?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            dbHelper.deleteStudentInfo(idStudent);
                            Toasty.success(getApplicationContext(), "Delete successfully!", Toast.LENGTH_SHORT, true).show();
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

    private void showDialogQuery(Activity activity) {

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.query_student_info);

        final EditText queryInput = dialog.findViewById(R.id.editQueryStu);
        Button btnQuery = dialog.findViewById(R.id.buttonQuery);

        // set width for dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.8);
        // set height for dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.3);
        dialog.setTitle("Query");
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        //确定按钮
        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //执行查询
                    updateStuListData(RESULT_QUERY, queryInput.getText().toString());
                    //关闭dialog
                    dialog.dismiss();
                    if(listStuInfo.isEmpty()){
                        Toasty.error(getApplicationContext(),"No result found!").show();
                    }else {
                        Toasty.success(getApplicationContext(), "Query successfully!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception error) {
                    Log.e("Query error", error.getMessage());
                }
            }
        });
    }

    // get data from sqlite
    private void updateStuListData(String kind, String condition) {
        listStuInfo.clear();
        if (kind.equals(RESULT_ALL)) {
            listStuInfo = dbHelper.getAllData(listStuInfo);
        } else if (kind.equals(RESULT_QUERY)) {
            listStuInfo=dbHelper.getQueryData(listStuInfo,condition);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 布局填充器:将布局文件转换成View 对象
     * <p>
     * 创建选项菜单时自动调用的方法
     * @param menu 菜单栏对象
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //1.得到菜单填充器对象:将菜单文件转换成菜单对象并挂载到指定的菜单栏上
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        System.out.println("=====onCreateOptionsMenu(Menu menu)====");
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 每次展开菜单项时都会自动调用的方法,可以修改某些菜单的可用性
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        //根据菜单项的id 值查找菜单对象
        //MenuItem menuItem_view = menu.findItem(R.id.menu_add_stu);
        //设置菜单项的可用性
        //menuItem_view.setEnabled(false);//禁用菜单项
        //设置菜单项的可见性
        //menuItem_view.setVisible(false);//菜单性不可见
        //System.out.println("===onPrepareOptionsMenu(Menu menu)==");
        return super.onPrepareOptionsMenu(menu);//如果想显示菜单项,则当前方法必须返回true
        //return false;//不会显示任何菜单对象了
    }

    /**
     * 当用户点击菜单项时自动调用的方法
     *
     * @param item 用户点中的菜单项对象
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        System.out.println("==onOptionsItemSelected(MenuItem item=" + item + ")===");
        //得到菜单项的标题
        String title = item.getTitle().toString();
        Toast.makeText(this, "title=" + title, Toast.LENGTH_LONG).show();

        int menuItemId = item.getItemId();
        switch (menuItemId) {
            case R.id.menu_add_stu:
                showDialogQuery(MainActivity.this);
                //turnToAddStuInfo(findViewById(R.id.menu_add_stu));
                System.out.println("=====add====");
                break;
            case R.id.menu_refresh_stu:
                updateStuListData(RESULT_ALL, null);
                System.out.println("====refresh=======");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
