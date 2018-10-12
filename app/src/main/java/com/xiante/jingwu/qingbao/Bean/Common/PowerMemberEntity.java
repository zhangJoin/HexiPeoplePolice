package com.xiante.jingwu.qingbao.Bean.Common;

import java.io.Serializable;

/**
 * Created by zhong on 2018/5/15.
 */

public class PowerMemberEntity implements Serializable {

    private String
            strUnityGuid="",// 社区表ID
            strUserGuid="",// 用户id
            strUserName="",// 用户姓拼上类型)
            strMobile="",// 电话
            strUserType="",// 人员类型(1群主，2管理，3普通)
            strUserTypeName="",// 人员类型中文
            strPortrait="",
            dtCreateTime="",
            strName="";// 创建时间

    public PowerMemberEntity() {
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrPortrait() {
        return strPortrait;
    }

    public void setStrPortrait(String strPortrait) {
        this.strPortrait = strPortrait;
    }



    public String getStrUnityGuid() {
        return strUnityGuid;
    }

    public void setStrUnityGuid(String strUnityGuid) {
        this.strUnityGuid = strUnityGuid;
    }

    public String getStrUserGuid() {
        return strUserGuid;
    }

    public void setStrUserGuid(String strUserGuid) {
        this.strUserGuid = strUserGuid;
    }

    public String getStrUserName() {
        return strUserName;
    }

    public void setStrUserName(String strUserName) {
        this.strUserName = strUserName;
    }

    public String getStrMobile() {
        return strMobile;
    }

    public void setStrMobile(String strMobile) {
        this.strMobile = strMobile;
    }

    public String getStrUserType() {
        return strUserType;
    }

    public void setStrUserType(String strUserType) {
        this.strUserType = strUserType;
    }

    public String getStrUserTypeName() {
        return strUserTypeName;
    }

    public void setStrUserTypeName(String strUserTypeName) {
        this.strUserTypeName = strUserTypeName;
    }

    public String getDtCreateTime() {
        return dtCreateTime;
    }

    public void setDtCreateTime(String dtCreateTime) {
        this.dtCreateTime = dtCreateTime;
    }
}
