package com.xiante.jingwu.qingbao.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.xiante.jingwu.qingbao.Adapter.BigimageAdapter;
import com.xiante.jingwu.qingbao.R;

import java.util.List;

/**
 * Created by zhong on 2018/7/17.
 */

public class BigImageActivity extends BaseActivity {
  private ViewPager viewPager;
  int count;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.big_image_activity);
        initView();
        initData();
        initListener();
    }

    @Override
    public void initView() {
          initTitlebar("","","");
          viewPager=findViewById(R.id.imageViewpager);
          List<String> data=getIntent().getStringArrayListExtra("image");
          count=data.size();
          int position=getIntent().getIntExtra("position",0);
          BigimageAdapter adapter=new BigimageAdapter(data,this);
          viewPager.setAdapter(adapter);
          viewPager.setCurrentItem(position);
          titleTextView.setText(position+1+"/"+count);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                  titleTextView.setText(position+1+"/"+count);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
