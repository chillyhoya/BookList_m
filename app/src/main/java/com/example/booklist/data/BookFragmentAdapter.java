package com.example.booklist.data;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class BookFragmentAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> datas;//存放要显示的子视图
    ArrayList<String> titles;//存放要显示的标题

    public BookFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setData(ArrayList<Fragment> datas) {
        this.datas = datas;
    }

    public void setTitles(ArrayList<String> titles) {
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }
    //用于获取子视图fragment的数目
    @Override
    public CharSequence getPageTitle(int position) {//用于获取对应位置的标题
        return titles == null ? null : titles.get(position);
    }
}
