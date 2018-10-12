package com.xiante.jingwu.qingbao.CustomView.CommonView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.xiante.jingwu.qingbao.CustomView.WheelView.WheelView;
import com.xiante.jingwu.qingbao.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zhong on 2018/4/27.
 */

public class TimeSelectView extends LinearLayout {

    WheelView yearWheelview,monthWheelView,dayWheelView,hourWheelView,minuteWheelView;
    List<String> yearlist,monthlist,daylist,hourlist,minutelist;
    int curYear,curMonth,curday,curHour,curMinute;
    public TimeSelectView(Context context) {
        super(context);
        init(context);
    }

    public TimeSelectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TimeSelectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        View.inflate(context,R.layout.time_select_view,this);
        yearWheelview=findViewById(R.id.time_year_select);
        monthWheelView=findViewById(R.id.time_month_select);
        hourWheelView=findViewById(R.id.time_hour_select);
        dayWheelView=findViewById(R.id.time_day_select);
        minuteWheelView=findViewById(R.id.time_minute_select);
        initData();
        initListener();
    }

    private void initData(){
        yearlist=new ArrayList<>();
        monthlist=new ArrayList<>();
        daylist=new ArrayList<>();
        hourlist=new ArrayList<>();
        minutelist=new ArrayList<>();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy:MM:dd:HH:mm");
        String currenttime=sdf.format(new Date());
        String[] time=currenttime.split(":");
        curYear=Integer.parseInt(time[0]);
        curMonth=Integer.parseInt(time[1]);
        curday=Integer.parseInt(time[2]);
        curHour=Integer.parseInt(time[3]);
        curMinute=Integer.parseInt(time[4]);
        for(int i=curYear;i>curYear-6;i--){
            yearlist.add(i+"");
        }
        for(int i=1;i<=12;i++){
            monthlist.add(i+"");
        }
        changeDayByMonthAndYear(curYear,curMonth);
        for(int i=0;i<24;i++){
            hourlist.add(i+"");
        }
        for(int i=0;i<60;i++){
            minutelist.add(i+"");
        }
        yearWheelview.setItems(yearlist,0);
        monthWheelView.setItems(monthlist,curMonth-1);
        dayWheelView.setItems(daylist,curday-1);
        hourWheelView.setItems(hourlist,curHour);
        minuteWheelView.setItems(minutelist,curMinute);
    }

    private void initListener(){
         yearWheelview.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(int selectedIndex, String item) {
                 dayWheelView.reset();
                curYear=Integer.parseInt(item);
                 changeDayByMonthAndYear(curYear,curMonth);
                 dayWheelView.setItems(daylist,0);
                 curday=1;
             }
         });
         monthWheelView.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(int selectedIndex, String item) {
                 dayWheelView.reset();
                 curMonth=Integer.parseInt(item);
                 changeDayByMonthAndYear(curYear,curMonth);
                 dayWheelView.setItems(daylist,0);
                 curday=1;

             }
         });
         dayWheelView.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(int selectedIndex, String item) {
                 curday=Integer.parseInt(item);
             }
         });
         hourWheelView.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(int selectedIndex, String item) {
                 curHour=Integer.parseInt(item);
             }
         });
         minuteWheelView.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(int selectedIndex, String item) {
                 curMinute=Integer.parseInt(item);
             }
         });
    }

    public String getSelectTime(){
        StringBuilder builder=new StringBuilder();
        builder.append(curYear+"").append("-").append(String.format("%02d", curMonth)+"").append("-")
                .append(String.format("%02d", curday)+"").append(" ").
                append(String.format("%02d", curHour)+"").append(":")
                .append(String.format("%02d", curMinute)+"");
        return  builder.toString();
    }

    private void changeDayByMonthAndYear(int year,int month){
          int day=0;
          switch (month){
              case 1:
                  day=31;
                  break;
              case 2:
                  if(isRun(year)){
                   day=29;
                  }else {
                      day=28;
                  }
                  break;
              case 3:
                  day=31;
                  break;
              case 4:
                  day=30;
                  break;
              case 5:
                  day=31;
                  break;
              case 6:
                  day=30;
                  break;
              case 7:
                  day=31;
                  break;
              case 8:
                  day=31;
                  break;
              case 9:
                  day=30;
                  break;
              case 10:
                  day=31;
                  break;
              case 11:
                  day=30;
                  break;
              case 12:
                  day=31;
                  break;
          }
          daylist.clear();
          for(int i=1;i<=day;i++){
              daylist.add(i+"");
          }
    }

    private boolean isRun(int year){
        boolean run=false;
        if(year%100==0){
            if(year%4==0){
                run=true;
            }
        }else {
            if(year%4==0){
                run=true;
            }
        }
        return run;
    }

}
