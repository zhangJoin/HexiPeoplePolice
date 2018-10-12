package com.xiante.jingwu.qingbao.Bean.Common;

import java.io.Serializable;

/**
 * @author 郑靖廷 Yoda_T
 * @email 17600203706@163.com
 * @time $date$ $time$
 */
public class SearchTypeBean implements Serializable {

    private String strGuid="";
    private String strValue="";
    private String strParentINo="";

    @Override
    public String toString() {
        return "SearchTypeBean{" +
                "strGuid='" + strGuid + '\'' +
                ", strValue='" + strValue + '\'' +
                ", strParentINo='" + strParentINo + '\'' +
                '}';
    }

    public String getStrGuid() {
        return strGuid;
    }

    public void setStrGuid(String strGuid) {
        this.strGuid = strGuid;
    }

    public String getStrValue() {
        return strValue;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }

    public String getStrParentINo() {
        return strParentINo;
    }

    public void setStrParentINo(String strParentINo) {
        this.strParentINo = strParentINo;
    }
}
