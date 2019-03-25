package com.example.student.widget;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author somelou
 * ViewPagerAdapter
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();

    private int mChildCount = 0;

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }


    @Override
    public long getItemId(int position){
        return super.getItemId(position);
    }


    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }

    // 可以删除这段代码看看，数据源更新而viewpager不更新的情况
    @Override
    public void notifyDataSetChanged() {
        // 重写这个方法，取到子Fragment的数量，用于下面的判断，以执行多少次刷新
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if ( mChildCount > 0) {
            // 这里利用判断执行若干次不缓存，刷新
            mChildCount --;
            // 返回这个是强制ViewPager不缓存，每次滑动都刷新视图
            return POSITION_NONE;
        }
        // 这个则是缓存不刷新视图
        return super.getItemPosition(object);
    }



}
