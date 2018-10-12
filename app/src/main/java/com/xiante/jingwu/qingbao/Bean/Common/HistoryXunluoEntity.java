package com.xiante.jingwu.qingbao.Bean.Common;

import java.io.Serializable;

/**
 * Created by zhong on 2018/5/17.
 */

public class HistoryXunluoEntity implements Serializable {

    private String strGuid="",
    strTaskGuid="",
    strUserGuid="",
    strCreateTime="",
    strDistance="",
    strDuration="",
    strSpeed="";

    public HistoryXunluoEntity() {
    }

    public String getStrGuid() {
        return strGuid;
    }

    public void setStrGuid(String strGuid) {
        this.strGuid = strGuid;
    }

    public String getStrTaskGuid() {
        return strTaskGuid;
    }

    public void setStrTaskGuid(String strTaskGuid) {
        this.strTaskGuid = strTaskGuid;
    }

    public String getStrUserGuid() {
        return strUserGuid;
    }

    public void setStrUserGuid(String strUserGuid) {
        this.strUserGuid = strUserGuid;
    }

    public String getStrCreateTime() {
        return strCreateTime;
    }

    public void setStrCreateTime(String strCreateTime) {
        this.strCreateTime = strCreateTime;
    }


    public String getStrDistance() {
        return strDistance;
    }

    public void setStrDistance(String strDistance) {
        this.strDistance = strDistance;
    }

    public String getStrDuration() {
        return strDuration;
    }

    public void setStrDuration(String strDuration) {
        this.strDuration = strDuration;
    }

    public String getStrSpeed() {
        return strSpeed;
    }

    public void setStrSpeed(String strSpeed) {
        this.strSpeed = strSpeed;
    }
}
