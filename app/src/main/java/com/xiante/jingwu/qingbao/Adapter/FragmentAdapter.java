package com.xiante.jingwu.qingbao.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by zhong on 2018/4/28.
 */

public class FragmentAdapter extends FragmentPagerAdapter {

    private List<? extends Fragment> allfragment;
    private List<String> titlelist;


    @Override
    public long getItemId(int position) {
        return allfragment.get(position).hashCode();
    }

    public FragmentAdapter(FragmentManager fm, List<? extends  Fragment> allfragment, List<String> titlelist) {
        super(fm);
        this.allfragment = allfragment;
        this.titlelist = titlelist;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titlelist.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return allfragment.get(position);
    }

    @Override
    public int getCount() {
        return allfragment.size();
    }
}
