package com.xiante.jingwu.qingbao.Bean.Common;

import java.io.Serializable;

/**
 * @author 郑靖廷 Yoda_T
 * @email 17600203706@163.com
 * @time $date$ $time$
 */
public class RegionBean implements Serializable {
    private String strRegionPhone="";
    private String strRegionName="";
    private String strAddr="";
    private String isEnable="";
    private String strGuid="";

    public String getStrRegionPhone() {
        return strRegionPhone;
    }

    public void setStrRegionPhone(String strRegionPhone) {
        this.strRegionPhone = strRegionPhone;
    }

    public String getStrRegionName() {
        return strRegionName;
    }

    public void setStrRegionName(String strRegionName) {
        this.strRegionName = strRegionName;
    }

    public String getStrAddr() {
        return strAddr;
    }

    public void setStrAddr(String strAddr) {
        this.strAddr = strAddr;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getStrGuid() {
        return strGuid;
    }

    public void setStrGuid(String strGuid) {
        this.strGuid = strGuid;
    }
}
