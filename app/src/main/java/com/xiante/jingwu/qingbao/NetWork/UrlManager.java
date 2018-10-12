package com.xiante.jingwu.qingbao.NetWork;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.xiante.jingwu.qingbao.Util.Global;

/**
 * Created by zhong on 2018/5/2.
 */

public class UrlManager  {

    String token="";
    String extraStr="";
    String data_url="";
    String file_url="";
    public UrlManager(Context context){
        token=context.getSharedPreferences(Global.SHARE_TOKEN,Context.MODE_PRIVATE).getString(Global.SHARE_TOKEN,"");
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
        }
        String mVersion="";
        if (info != null)
            mVersion = info.versionName;

        StringBuilder builder=new StringBuilder();
        builder.append("token="+token);
        builder.append("&version="+mVersion);
        builder.append("&brand="+Build.BRAND);
        builder.append("&strApp=hexi&appUserTypeCode=mj");
        extraStr=builder.toString();
        data_url=context.getSharedPreferences(Global.MAIN_URL,Context.MODE_PRIVATE).getString(Global.MAIN_URL,"");
        file_url=context.getSharedPreferences(Global.FILE_Server_URL,Context.MODE_PRIVATE).getString(Global.FILE_Server_URL,"");

    }

    public String getBaseUrl(){
        return new StringBuilder().append(Global.baseurl).append("?").append(extraStr).toString();
    }
    //登入
    public String getLoginUrl(){
        return  new StringBuilder().append(data_url).append(Global.LOGIN_URL).append("?").append(extraStr).toString();
    }
//获取首页接口地址
    public String getShouyeUrl(){
        return new StringBuilder().append(data_url).append(Global.Shouye_URL).append("?").append(extraStr).toString();
    }
//未读消息地址
    public String getUnReadMsgUrl(){
        return new StringBuilder(data_url).append(Global.UNREAD_MSG_COUNT).append("?").append(extraStr).toString();
    }
//个人信息
    public String getUserInformation(){
        return new StringBuilder(data_url).append(Global.USER_INFORMATION).append("?").append(extraStr).toString();
    }

//常用联系人
    public String getUsualContact(){
        return new StringBuilder(data_url).append(Global.USUAL_CONTACT).append("?").append(extraStr).toString();
    }
//安全在身边
    public String getMy_Security(){
        return new StringBuilder(data_url).append(Global.MY_SECURITY).append("?").append(extraStr).toString();
    }
//力量源列表
    public String getPowerSource(){
        return new StringBuilder(data_url).append(Global.POWER_SOURCE).append("?").append(extraStr).toString();
    }
//获取力量源成员列表
    public String getPowerMember(){
        return new StringBuilder(data_url).append(Global.POWER_MEMBER).append("?").append(extraStr).toString();
    }
    //退出力量源
    public String getLeftPowerGroup(){
        return new StringBuilder(data_url).append(Global.LEFT_FROM_POWER_GROUP).append("?").append(extraStr).toString();
    }
//修改密码
    public String getModifyPsd(){
        return new StringBuilder(data_url).append(Global.MODIFY_PSD).append("?").append(extraStr).toString();
    }
//巡逻开始
    public String getXunluoStart(){
        return new StringBuilder(data_url).append(Global.XUNLUO_START).append("?").append(extraStr).toString();
    }
    //巡逻中数据上传
    public String getXunluo_ReportLocation(){
        return new StringBuilder(data_url).append(Global.XUNLUO_REPORT_LOCATION).append("?").append(extraStr).toString();
    }
    //巡逻结束
    public String getXunluoEnd(){
        return new StringBuilder(data_url).append(Global.XUNLUO_END).append("?").append(extraStr).toString();
    }
   //历史巡逻列表
    public String getXunluo_History(){
        return new StringBuilder(data_url).append(Global.XUNLUO_HISTORY).append("?").append(extraStr).toString();
    }
   //历史巡逻轨迹页面
    public String getXunluo_Track(){
        return new StringBuilder(data_url).append(Global.XUNLUO_TRACK).append("?").append(extraStr).toString();
    }
    //获取我的积分页面数据
    public String getScoreList(){
        return new StringBuilder(data_url).append(Global.MY_SCORELIST).append("?").append(extraStr).toString();

    }
    //获取绑定信息员列表
    public String getBindUserList(){
        return new StringBuilder(data_url).append(Global.BIND_USER).append("?").append(extraStr).toString();

    }



    public String getExtraStr() {
        return extraStr;
    }

    public String getData_url(){
        return data_url;
    }

    public String getToken() {
        return token;
    }


    public String getFile_url() {
        return file_url;
    }

    public  String getRegisterUrl(){
        return  new StringBuilder(data_url).append(Global.REGISTER_URL).append("?").append(extraStr).toString();
    }
    public  String getStepOneUrl(){
        return  new StringBuilder(data_url).append(Global.STEP_ONE).append("?").append(extraStr).toString();
    }
    public  String getStepTwoUrl(){
        return  new StringBuilder(data_url).append(Global.STEP_TWO).append("?").append(extraStr).toString();
    }
    public  String getScanPowerUrl(){
        return  new StringBuilder(data_url).append(Global.SCAN_ADD).append("?").append(extraStr).toString();
    }
    public  String getModifyPhoneUrl(){
        return  new StringBuilder(data_url).append(Global.MODIFYMOBILE).append("?").append(extraStr).toString();
    }

    public  String getRefreshReportUrl(){
        return  new StringBuilder(data_url).append(Global.REFRESH_REPORTER).append("?").append(extraStr).toString();
    }
   //设置力量源管理员
    public String getSetMemberManager() {

        return  new StringBuilder(data_url).append(Global.SET_MEMBER_MANAGER).append("?").append(extraStr).toString();
    }
    //设置力量源管理员
    public String getCancelManagerPower() {

        return  new StringBuilder(data_url).append(Global.CANCEL_MANAGER_POWER).append("?").append(extraStr).toString();
    }
    //删除力量源管理员
    public String deleteMemberManager() {

        return  new StringBuilder(data_url).append(Global.DELETE_MEMBER_MANAGER).append("?").append(extraStr).toString();
    }
    public String getFileUrl() {

        return  new StringBuilder(data_url).append(Global.SET_MEMBER_MANAGER).append("?").append(extraStr).toString();
    }
    public  String getCheckExistUrl(){
        return  new StringBuilder(data_url).append(Global.CHECK_EXIST).append("?").append(extraStr).toString();
    }
    public  String getQueryUserUrl(){
        return  new StringBuilder(data_url).append(Global.QUERY_USER).append("?").append(extraStr).toString();
    }

    public String getMyPosition(){
        return new StringBuilder(data_url).append(Global.MY_POSITION).append("?").append(extraStr).toString();
    }

    public String getPoliceInformer() {
        return new StringBuilder(data_url).append(Global.POLICE_INFORMER).append("?").append(extraStr).toString();
    }
    public String getUpdatePowerUnityUrl() {
        return new StringBuilder(data_url).append(Global.CHANGE_POWERUNITY).append("?").append(extraStr).toString();
    }

    public String getPoliceXXY(){
        return  new StringBuilder(data_url).append(Global.POLICE_XXY).append("?").append(extraStr).toString();
    }

}
