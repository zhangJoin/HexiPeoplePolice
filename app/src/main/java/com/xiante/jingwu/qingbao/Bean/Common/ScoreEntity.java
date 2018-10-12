package com.xiante.jingwu.qingbao.Bean.Common;

/**
 * Created by zhong on 2018/5/14.
 */

public class ScoreEntity {
    private String strIco="",
    strRuleName="",
    dtCeateTime="",
    intScore="";
    public ScoreEntity() {
    }

    public String getStrIco() {
        return strIco;
    }

    public void setStrIco(String strIco) {
        this.strIco = strIco;
    }

    public String getStrRuleName() {
        return strRuleName;
    }

    public void setStrRuleName(String strRuleName) {
        this.strRuleName = strRuleName;
    }

    public String getDtCeateTime() {
        return dtCeateTime;
    }

    public void setDtCeateTime(String dtCeateTime) {
        this.dtCeateTime = dtCeateTime;
    }

    public String getIntScore() {
        return intScore;
    }

    public void setIntScore(String intScore) {
        this.intScore = intScore;
    }
}
