package com.xiante.jingwu.qingbao.Bean.Common;

import java.io.Serializable;

/**
 * Created by zhong on 2018/5/8.
 */

public class ClickEntity implements Serializable{
    String strType="",strIco="",strText="",strUrl="",strTitle="";
    public ClickEntity(){}

    public ClickEntity(String strType, String strIco, String strText, String strUrl, String strTitle) {
        this.strType = strType;
        this.strIco = strIco;
        this.strText = strText;
        this.strUrl = strUrl;
        this.strTitle = strTitle;
    }

    public ClickEntity(String strText) {
        this.strText = strText;
    }

    public String getStrType() {
        return strType;
    }

    public void setStrType(String strType) {
        this.strType = strType;
    }

    public String getStrIco() {
        return strIco;
    }

    public void setStrIco(String strIco) {
        this.strIco = strIco;
    }

    public String getStrText() {
        return strText;
    }

    public void setStrText(String strText) {
        this.strText = strText;
    }

    public String getStrUrl() {
        return strUrl;
    }

    public void setStrUrl(String strUrl) {
        this.strUrl = strUrl;
    }

    public String getStrTitle() {
        return strTitle;
    }

    public void setStrTitle(String strTitle) {
        this.strTitle = strTitle;
    }
}
