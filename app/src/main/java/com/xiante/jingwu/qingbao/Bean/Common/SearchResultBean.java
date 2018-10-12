package com.xiante.jingwu.qingbao.Bean.Common;

import java.io.Serializable;

/**
 * @author 郑靖廷 Yoda_T
 * @email 17600203706@163.com
 * @time $date$ $time$
 */
public class SearchResultBean implements Serializable{

    private String personName,personImageUrl,personTel;

    private boolean isAdd;

    private String strGuid="";
    private String strName="";//姓名
    private String strPortrait="";//头像路径
    private String strLabelName="";//标签汉字名称串用顿号分割
    private String strLabel="";//标签guid多个用逗号分割
    private String strTradeName="";//信息员行业类别名称
    private String strTrade="";//行业类别guid
    private String strMobile="";
    public String getPersonName() {
        return strName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonImageUrl() {
        return strPortrait;
    }

    public void setPersonImageUrl(String personImageUrl) {
        this.personImageUrl = personImageUrl;
    }

    public String getPersonTel() {
        return strMobile;
    }

    public void setPersonTel(String personTel) {
        this.personTel = personTel;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    public String getStrGuid() {
        return strGuid;
    }

    public void setStrGuid(String strGuid) {
        this.strGuid = strGuid;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        personName=strName;
        this.strName = strName;
    }

    public String getStrPortrait() {
        return strPortrait;
    }

    public void setStrPortrait(String strPortrait) {
        personImageUrl=strPortrait;
        this.strPortrait = strPortrait;
    }

    public String getStrLabelName() {
        return strLabelName;
    }

    public void setStrLabelName(String strLabelName) {
        this.strLabelName = strLabelName;
    }

    public String getStrLabel() {
        return strLabel;
    }

    public void setStrLabel(String strLabel) {
        this.strLabel = strLabel;
    }

    public String getStrTradeName() {
        return strTradeName;
    }

    public void setStrTradeName(String strTradeName) {
        this.strTradeName = strTradeName;
    }

    public String getStrTrade() {
        return strTrade;
    }

    public void setStrTrade(String strTrade) {
        this.strTrade = strTrade;
    }

    public String getStrMobile() {
        return strMobile;
    }

    public void setStrMobile(String strMobile) {
        personTel=strMobile;
        this.strMobile = strMobile;
    }
}
