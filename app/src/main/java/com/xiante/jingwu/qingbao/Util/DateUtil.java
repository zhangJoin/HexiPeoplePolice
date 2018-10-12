package com.xiante.jingwu.qingbao.Util;

import com.xiante.jingwu.qingbao.Bean.Common.SearchTypeBean;

import java.util.Arrays;
import java.util.List;


public class DateUtil {
    public static String splitString(String... characters) {
        StringBuffer oriString = new StringBuffer();
        for (int i = 0; i < characters.length; i++) {
            if(i==0){
                oriString.append(characters[0].toString());
            }else{
                oriString.append(",").append(characters[i].toString());
            }
        }
        return oriString.toString();
    }


    public static String splitString(List<String> list) {
        StringBuffer oriString = new StringBuffer();

        for (int i = 0; i < list.size(); i++) {
            if(i==0){
                oriString = oriString.append(list.get(0));
            }else{
                oriString = oriString.append(",").append(list.get(i));
            }
        }
        return oriString.toString();
    }

    public static String getTypeString(List<SearchTypeBean> list) {
        StringBuffer oriString = new StringBuffer();

        for (int i = 0; i < list.size(); i++) {
            if(i==0){
                oriString = oriString.append(list.get(0).getStrValue());
            }else{
                oriString = oriString.append(",").append(list.get(i).getStrValue());
            }
        }
        return oriString.toString();
    }

    public static String appendString(String... characters) {
        StringBuffer oriString = new StringBuffer();
        for (int i = 0; i < characters.length; i++) {
            if(i==0){
                oriString.append(characters[0].toString());
            }else{
                oriString.append("  ,  ").append(characters[i].toString());
            }
        }
        return oriString.toString();
    }
    public static List appendList(String string) {
        StringBuffer oriString = new StringBuffer();
        String[] splitStr = string.split(",");
        return Arrays.asList(splitStr);
    }
}
