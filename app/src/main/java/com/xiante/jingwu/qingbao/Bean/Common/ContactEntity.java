package com.xiante.jingwu.qingbao.Bean.Common;

import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;

import java.io.Serializable;

/**
 * Created by zhong on 2018/5/9.
 */

public class ContactEntity extends BaseIndexPinyinBean implements Serializable{

    private String strUnitName="",// 单位名称
            isDeleted="",// 是否已删除 0 未删除 1 已删除
            strBranchName="", // 分局名称
            strType="",// 分类
            intOrder="",// 序号
            strCreator="",// 创建人
            strTelNum="",//电话号码
            dtCreateTime="",// 创建时间
            iNo="",// 主键自增
            strGuid="",// 唯一id
            isEnable="";// 是否可用 1可用；0不可用；

    public ContactEntity() {
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

    public String getStrBranchName() {
        return strBranchName;
    }

    public void setStrBranchName(String strBranchName) {
        this.strBranchName = strBranchName;
    }

    public String getStrType() {
        return strType;
    }

    public void setStrType(String strType) {
        this.strType = strType;
    }

    public String getIntOrder() {
        return intOrder;
    }

    public void setIntOrder(String intOrder) {
        this.intOrder = intOrder;
    }

    public String getStrCreator() {
        return strCreator;
    }

    public void setStrCreator(String strCreator) {
        this.strCreator = strCreator;
    }

    public String getStrTelNum() {
        return strTelNum;
    }

    public void setStrTelNum(String strTelNum) {
        this.strTelNum = strTelNum;
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

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    @Override
    public String getTarget() {
        return strUnitName;
    }
}
