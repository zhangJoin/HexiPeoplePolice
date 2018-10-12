package com.xiante.jingwu.qingbao.Bean.Common;

import java.io.Serializable;

/**
 * Created by zhong on 2018/5/3.
 */

public class ReportTypeEntity implements Serializable{
   String strTypeName="",strIco="", strUrl="", clickType="",showNumUrl="";//web页-web , 表单-form , 列表-list
   ActionBarEntity strTtileBar;

    public ReportTypeEntity() {
    }

    public String getStrTypeName() {
        return strTypeName;
    }

    public void setStrTypeName(String strTypeName) {
        this.strTypeName = strTypeName;
    }

    public String getStrIco() {
        return strIco;
    }

    public void setStrIco(String strIco) {
        this.strIco = strIco;
    }

    public String getStrUrl() {
        return strUrl;
    }

    public void setStrUrl(String strUrl) {
        this.strUrl = strUrl;
    }

    public String getClickType() {
        return clickType;
    }

    public void setClickType(String clickType) {
        this.clickType = clickType;
    }

    public ActionBarEntity getStrTtileBar() {
        return strTtileBar;
    }

    public void setStrTtileBar(ActionBarEntity strTtileBar) {
        this.strTtileBar = strTtileBar;
    }

    public String getShowNumUrl() {
        return showNumUrl;
    }

    public void setShowNumUrl(String showNumUrl) {
        this.showNumUrl = showNumUrl;
    }
}
