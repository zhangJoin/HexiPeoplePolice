package com.xiante.jingwu.qingbao.Bean.Common;

/**
 * Created by zhong on 2018/5/15.
 */

public class Power {
    private String
            strGuid="",// 编号（预留）guid
            strUnityName="",// 社区名称(唯一)
            strUnitGuid="";// 所属派出所单位guid
    private int total=0;
    public Power() {
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getStrGuid() {
        return strGuid;
    }

    public void setStrGuid(String strGuid) {
        this.strGuid = strGuid;
    }

    public String getStrUnityName() {
        return strUnityName;
    }

    public void setStrUnityName(String strUnityName) {
        this.strUnityName = strUnityName;
    }

    public String getStrUnitGuid() {
        return strUnitGuid;
    }

    public void setStrUnitGuid(String strUnitGuid) {
        this.strUnitGuid = strUnitGuid;
    }
}
