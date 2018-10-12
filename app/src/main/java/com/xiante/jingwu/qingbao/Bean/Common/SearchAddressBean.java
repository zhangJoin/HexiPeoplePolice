package com.xiante.jingwu.qingbao.Bean.Common;

import java.io.Serializable;

/**
 * Created by zhong on 2018/5/22.
 */

public class SearchAddressBean implements Serializable {

    private String firstName="",detailName="",adcode="",latitude="",longtitude="",address="";
    private boolean selectState=false;

    public SearchAddressBean(){}
    public SearchAddressBean(String firstName, String detailName, String adcode, boolean selectState) {
        this.firstName = firstName;
        this.detailName = detailName;
        this.adcode = adcode;
        this.selectState = selectState;
        address=firstName+detailName;
    }

    public SearchAddressBean(String firstName, String detailName, String adcode, String latitude, String longtitude, boolean selectState) {
        this.firstName = firstName;
        this.detailName = detailName;
        this.adcode = adcode;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.selectState = selectState;
        address=firstName+detailName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public boolean getSelectState() {
        return selectState;
    }

    public void setSelectState(boolean selectState) {
        this.selectState = selectState;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
