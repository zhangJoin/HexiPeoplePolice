package com.xiante.jingwu.qingbao.Bean.Common;

import java.io.Serializable;

/**
 * Created by zhong on 2018/5/9.
 */

public class UserEntity implements Serializable {
    private String strName = "",
            strSex = "",//性别 male男; female 女;
            strNation = "",
            dtBirthDate = "",//
            strCardNo = "",
            strAddress = "",
            strMobile = "",
            strPolice = "",//民警姓名
            dtCreateTime = "",
            strPortrait = "",
            intTotalScore = "",
            xxyNum = "",
            strUnityGuid = "",
            strCreateTime = "",
            strBirthDate="";

    public String getStrCreateTime() {
        return strCreateTime;
    }

    public void setStrCreateTime(String strCreateTime) {
        this.strCreateTime = strCreateTime;
    }

    public String getStrBirthDate() {
        return strBirthDate;
    }

    public void setStrBirthDate(String strBirthDate) {
        this.strBirthDate = strBirthDate;
    }

    public UserEntity() {
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrSex() {
        return strSex;
    }

    public void setStrSex(String strSex) {
        this.strSex = strSex;
    }

    public String getStrNation() {
        return strNation;
    }

    public void setStrNation(String strNation) {
        this.strNation = strNation;
    }

    public String getDtBirthDate() {
        return dtBirthDate;
    }

    public void setDtBirthDate(String dtBirthDate) {
        this.dtBirthDate = dtBirthDate;
    }

    public String getStrCardNo() {
        return strCardNo;
    }

    public void setStrCardNo(String strCardNo) {
        this.strCardNo = strCardNo;
    }

    public String getStrAddress() {
        return strAddress;
    }

    public void setStrAddress(String strAddress) {
        this.strAddress = strAddress;
    }

    public String getStrMobile() {
        return strMobile;
    }

    public void setStrMobile(String strMobile) {
        this.strMobile = strMobile;
    }

    public String getStrPolice() {
        return strPolice;
    }

    public void setStrPolice(String strPolice) {
        this.strPolice = strPolice;
    }

    public String getDtCreateTime() {
        return dtCreateTime;
    }

    public void setDtCreateTime(String dtCreateTime) {
        this.dtCreateTime = dtCreateTime;
    }

    public String getStrPortrait() {
        return strPortrait;
    }

    public void setStrPortrait(String strPortrait) {
        this.strPortrait = strPortrait;
    }

    public String getIntTotalScore() {
        return intTotalScore;
    }

    public void setIntTotalScore(String intTotalScore) {
        this.intTotalScore = intTotalScore;
    }

    public String getXxyNum() {
        return xxyNum;
    }

    public void setXxyNum(String xxyNum) {
        this.xxyNum = xxyNum;
    }

    public String getStrUnityGuid() {
        return strUnityGuid;
    }

    public void setStrUnityGuid(String strUnityGuid) {
        this.strUnityGuid = strUnityGuid;
    }
}
