package com.xiante.jingwu.qingbao.Bean.Common;

import java.io.Serializable;

/**
 * Created by zhong on 2018/5/11.
 */

public class SecurityEntity implements Serializable {
   private String  strUnitGuid= "",// 单位唯一guid
             strUnitName="11111111111",// 单位名称
             isDeleted="",// 是否已删除 0 未删除 1 已删除
             strAddress="",// 单位地址
             strCreator="",// 创建人
             strLongitude="",// 单位经度
             dtCreateTime="",// 创建时间
             iNo="",// 主键自增
             strGuid="",// 唯一id
             strLatitude="",// 单位纬度
             strTel="",// 单位电话
             isEnable="";// 是否可用 1可用；0不可用；
    public SecurityEntity() {
    }

    public String getStrUnitGuid() {
        return strUnitGuid;
    }

    public void setStrUnitGuid(String strUnitGuid) {
        this.strUnitGuid = strUnitGuid;
    }

    public String getStrUnitName() {
        return strUnitName;
    }

    public void setStrUnitName(String strUnitName) {
        this.strUnitName = strUnitName;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getStrAddress() {
        return strAddress;
    }

    public void setStrAddress(String strAddress) {
        this.strAddress = strAddress;
    }

    public String getStrCreator() {
        return strCreator;
    }

    public void setStrCreator(String strCreator) {
        this.strCreator = strCreator;
    }

    public String getStrLongitude() {
        return strLongitude;
    }

    public void setStrLongitude(String strLongitude) {
        this.strLongitude = strLongitude;
    }

    public String getDtCreateTime() {
        return dtCreateTime;
    }

    public void setDtCreateTime(String dtCreateTime) {
        this.dtCreateTime = dtCreateTime;
    }

    public String getiNo() {
        return iNo;
    }

    public void setiNo(String iNo) {
        this.iNo = iNo;
    }

    public String getStrGuid() {
        return strGuid;
    }

    public void setStrGuid(String strGuid) {
        this.strGuid = strGuid;
    }

    public String getStrLatitude() {
        return strLatitude;
    }

    public void setStrLatitude(String strLatitude) {
        this.strLatitude = strLatitude;
    }

    public String getStrTel() {
        return strTel;
    }

    public void setStrTel(String strTel) {
        this.strTel = strTel;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }
}
