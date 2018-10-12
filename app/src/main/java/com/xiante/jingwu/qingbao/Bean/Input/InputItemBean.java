package com.xiante.jingwu.qingbao.Bean.Input;

/**
 * Created by zhong on 2018/4/19.
 */

public class InputItemBean {
private String strGuid="",
    strTypeGuid="",
    strField="",
    strFieldName="",
    strFieldType="",
    strPlaceHolder="",
    imageNum="9",
    isMust="",//是否必填1是0不是
    isCheck="",//是否校验1是0不是
    strCheckRule="",//校验规则正则表达式
    strCheckErrInfo="",
    strUrl="",
        strFieldValue="";
    //错误提示文字;

    public InputItemBean() {
    }

    public InputItemBean(String strField, String strFieldName, String strFieldType, String strPlaceHolder, String isMust, String isCheck, String strCheckRule, String strCheckErrInfo) {
        this.strField = strField;
        this.strFieldName = strFieldName;
        this.strFieldType = strFieldType;
        this.strPlaceHolder = strPlaceHolder;
        this.isMust = isMust;
        this.isCheck = isCheck;
        this.strCheckRule = strCheckRule;
        this.strCheckErrInfo = strCheckErrInfo;
    }

    public InputItemBean(String strField, String strFieldName, String strFieldType, String strPlaceHolder) {
        this.strField = strField;
        this.strFieldName = strFieldName;
        this.strFieldType = strFieldType;
        this.strPlaceHolder = strPlaceHolder;
    }

    public String getStrFieldValue() {
        return strFieldValue;
    }

    public void setStrFieldValue(String strFieldValue) {
        this.strFieldValue = strFieldValue;
    }

    public String getStrGuid() {
        return strGuid;
    }

    public void setStrGuid(String strGuid) {
        this.strGuid = strGuid;
    }

    public String getStrTypeGuid() {
        return strTypeGuid;
    }

    public void setStrTypeGuid(String strTypeGuid) {
        this.strTypeGuid = strTypeGuid;
    }

    public String getStrField() {
        return strField;
    }

    public void setStrField(String strField) {
        this.strField = strField;
    }

    public String getStrFieldName() {
        return strFieldName;
    }

    public void setStrFieldName(String strFieldName) {
        this.strFieldName = strFieldName;
    }

    public String getStrFieldType() {
        return strFieldType;
    }

    public void setStrFieldType(String strFieldType) {
        this.strFieldType = strFieldType;
    }

    public String getStrPlaceHolder() {
        return strPlaceHolder;
    }

    public void setStrPlaceHolder(String strPlaceHolder) {
        this.strPlaceHolder = strPlaceHolder;
    }

    public String getIsMust() {
        return isMust;
    }

    public void setIsMust(String isMust) {
        this.isMust = isMust;
    }

    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }

    public String getStrCheckRule() {
        return strCheckRule;
    }

    public void setStrCheckRule(String strCheckRule) {
        this.strCheckRule = strCheckRule;
    }

    public String getStrCheckErrInfo() {
        return strCheckErrInfo;
    }

    public void setStrCheckErrInfo(String strCheckErrInfo) {
        this.strCheckErrInfo = strCheckErrInfo;
    }

    public String getImageNum() {
        return imageNum;
    }

    public void setImageNum(String imageNum) {
        this.imageNum = imageNum;
    }

    public String getStrUrl() {
        return strUrl;
    }

    public void setStrUrl(String strUrl) {
        this.strUrl = strUrl;
    }
}
