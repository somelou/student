package com.example.student;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.example.student.util.BottomNavigationViewHelper;
import com.example.student.bean.Student;
import com.example.student.list.ListStuFragment;
import com.example.student.my.MyFragment;
import com.example.student.phone.PhonePlaceActivity;
import com.example.student.util.CallBackValue;
import com.example.student.student.StudentFragment;
import com.example.student.adapter.ViewPagerAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements CallBackValue {

    public static final String RESULT_QUERY = "QUERY";
    public static final String RESULT_ALL = "ALL";
    public static final String FROM_ME = "IN_MY";

    private ViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;

    String birthday;//  其实没什么用的值
    Student student;

    String updateKind, condition;
    Map<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        viewPager = findViewById(R.id.viewpager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        //新版本的sdk好像已经解决了这个问题
        //默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
        //BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_list:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.item_add:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.item_find:
                                viewPager.setCurrentItem(2);
                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
//                FragmentManager fm = getSupportFragmentManager();
//
//                // fm.getFragments(),当点了第三页时，再回来，返回值的内容就不同了，ListStuFragment和StudentFragment的顺序发生了改变
//                List<Fragment> fragments = fm.getFragments();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        setupViewPager(viewPager);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(ListStuFragment.newInstance("1"));
        adapter.addFragment(StudentFragment.newInstance(birthday));
        adapter.addFragment(MyFragment.newInstance("1"));
        viewPager.setAdapter(adapter);
    }

    /**
     * 布局填充器:将布局文件转换成View 对象
     * <p>
     * 创建选项菜单时自动调用的方法
     *
     * @param menu 菜单栏对象
     * @return 父类构造方法
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
     *
     * @param menu Menu
     * @return 父类构造方法
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
     * 显示查询 dialog
     *
     * @param activity Activity
     */
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
        Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
        dialog.show();

        //确定按钮
        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //执行查询
                    condition = queryInput.getText().toString();
                    //关闭dialog
                    dialog.dismiss();
                    makeFragmentRefresh(0);
                    //updateStuListData(RESULT_QUERY, queryInput.getText().toString());


                } catch (Exception error) {
                    Log.e("Query error", error.getMessage());
                }
            }
        });
    }

    /**
     * 当用户点击菜单项时自动调用的方法
     *
     * @param item 用户点中的菜单项对象
     * @return 父类构造方法
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        System.out.println("==onOptionsItemSelected(MenuItem item=" + item + ")===");
        //得到菜单项的标题
        String title = item.getTitle().toString();
        Toast.makeText(this, "title=" + title, Toast.LENGTH_LONG).show();

        int menuItemId = item.getItemId();
        switch (menuItemId) {
            case R.id.menu_query_stu:
                updateKind = RESULT_QUERY;
                showDialogQuery(this);
                //turnToAddStuInfo(findViewById(R.id.menu_add_stu));
                System.out.println("=====query====");
                break;
            case R.id.menu_refresh_stu:
                updateKind = RESULT_ALL;
                condition = null;
                makeFragmentRefresh(0);
                //updateStuListData(RESULT_ALL, null);
                System.out.println("====refresh=======");
                break;
            case R.id.menu_phone_place:
                turnToPhonePlace(findViewById(R.id.menu_phone_place));
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 在list中点击update，把选中的student传值给activity，然后转到student页
     *
     * @param student
     * @param pageNum
     */
    @Override
    public void SendValueFromStudentMessage(Student student, int pageNum) {
        this.student = student;
        System.out.println("get student from list stu fragment name:" + student.getStuName());
        makeFragmentRefresh(pageNum);
        this.student = null;
    }

    @Override
    public void SendQueryValue(Map<String, String> map, int pageNum) {
        this.map = map;
        makeFragmentRefresh(pageNum);
        this.map = null;
    }


    /**
     * 返回MainActivity中对应的student
     * studentFragment需要
     *
     * @return
     */
    public Student getStudentToUpdate() {
        if (student != null) {
            System.out.println("in return stu to update,name:" + student.getStuName());
        }
        return student;
    }

    /**
     * 返回MainActivity中对应的查询信息
     * listStuFragment需要
     *
     * @return
     */
    public Map<String, String> getListConditionToList() {
        Map<String, String> map = new HashMap<>();
        map.put("kind", updateKind);
        map.put("condition", condition);
        return map;
    }


    /**
     * 相当于刷新pageNum对应的页面，使得其能获得MainActivity中对应的值
     *
     * @param pageNum
     */
    private void makeFragmentRefresh(int pageNum) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        // fm.getFragments(),当点了第三页时，再回来，返回值的内容就不同了，ListStuFragment和StudentFragment的顺序发生了改变
        List<Fragment> fragments = fm.getFragments();
        Log.d("Fragment", fragments.get(pageNum).toString());
        fragments.get(pageNum).onResume();
        ft.commit();
        viewPager.setCurrentItem(pageNum);
    }


    public void turnToPhonePlace(View view) {
        Intent intent = new Intent(this, PhonePlaceActivity.class);
        startActivity(intent);
    }

}
