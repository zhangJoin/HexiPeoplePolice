package com.xiante.jingwu.qingbao.Bean;

import com.xiante.jingwu.qingbao.Bean.Common.FileUploadBean;

/**
 * Created by zhong on 2018/6/11.
 */

public class InputModifyBean {
    //1代表网络，0代表本地
    public static final String NET_IMAGE="1",LOCAL_IMAGE="0";
    private String imageType="";
    private String localpath="";
    private FileUploadBean uploadBean;

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getLocalpath() {
        return localpath;
    }

    public void setLocalpath(String localpath) {
        this.localpath = localpath;
    }

    public FileUploadBean getUploadBean() {
        return uploadBean;
    }

    public void setUploadBean(FileUploadBean uploadBean) {
        this.uploadBean = uploadBean;
    }

}
