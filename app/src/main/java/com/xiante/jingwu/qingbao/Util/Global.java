package com.xiante.jingwu.qingbao.Util;

/**
 * Created by zhong on 2018/4/20.
 */

public class Global {
    public  final static String TEXT="TEXT",IMAGE="IMAGE",VOICE="VOICE",
             TIME="TIME", VIDEO="VIDEO",RENTER="RENTER",MULTIMIDEA="MULTIMIDEA",
            MULTILINE_TEXT="MULTILINE_TEXT",MY_LOCATION="MY_LOCATION", LOCATION="LOCATION",LABEL="LABEL",
            SCAN_IDCARD="SCAN_IDCARD",HOUSE_OWNER="HOUSE_OWNER",PUBLISH_TYPE="PUBLISH_TYPE",SINGLE_SELECT="SINGLESELECT";
    public final static String INPUTKEY="INPUTKEY";
    public final static String VIEW_ID="VIEW_ID";
    public final static int MODEL_ONE=0x56,MODEL_TWO=0x57;
    public final static String SEX_MAN="男";
    public final static String SEX_WOMEN="女";
    public final static String SHARE_TOKEN="SHARE_TOKEN";
    public final static String MAIN_URL="MAIN_URL";
    public final static String IS_UPDATE = "IS_UPDATE";
    public final static String APP_URL = "APP_URL";
    public final static String UPDATE_MUSE = "UPDATE_MUSE";
    public final static String FILE_Server_URL="file_url";
    public final static String SEND_PHONE_VERIFYCODE="sms/verify/sendSmsCode.do";
    public static String baseurl = "http://service.weijingyuan.com.cn/appsettings/main/getsettings.do";
    public final static String VERYFY_PHONE_CODE="sms/verify/validateSmsCode.do";
//      public static String baseurl = "http://192.168.1.115/JinMen/android/login/loginServerPath.do";
    public final static String LOGIN_URL="android/login/login.do";
    public final static String Shouye_URL="android/login/initIndexDataMJ.do";
    public final static String UNREAD_MSG_COUNT="android/infoUserMessage/userMessageCount.do";
    public final static String USER_INFORMATION="android/login/queryUser.do";
    public final static String MY_MSGTYPE="android/infoUserMessage/userMessagePath.do";
    public final static String MY_REPORTER="android/baseProcessOperation/queryProcessOperation.do";
    public final static String Verify_Record="android/infoInformation/queryMjInformationMenu.do";
    public final static String MY_TASK="android/infoNews/queryNewsMenu.do";
    public final static String USUAL_CONTACT="android/tel/selectAreaTel.do";
    public final static String MY_SECURITY="android/unitShow/selectAreaUnitShow.do";
    public final static String POWER_SOURCE="android/power/message/queryPowerMessageList.do";
    public final static String POWER_MEMBER="android/power/person/queryPowerPersonList.do";
    public final static String LEFT_FROM_POWER_GROUP="android/power/person/delPowerPerson.do";
    public final static String XUNLUO_REPORT_LOCATION="android/taskTrackPoint/saveTrackPoint.do";
    public final static String XUNLUO_HISTORY="android/taskTrack/queryTrackList.do";
    public final static String MODIFY_PSD="android/user/modifyPassword.do";
    public final static String XUNLUO_START="android/taskTrack/startTrack.do";
    public final static String XUNLUO_END="android/taskTrack/endTrack.do";
    public final static String XUNLUO_TRACK="android/taskTrackPoint/queryTrackPointList.do";
    public final static String MY_SCORELIST="android/user/queryScorePath.do";
    public final static String USER_ACCOUNT="user_account";
    public final static String USER_PASSWORD="user_password";
    public final static String CLICK_ACTION="CLICK_ACTION";
    public final static String ME_AVATAR="ME_AVATAR";
    public final static String POLICE_INFORMER="android/user/queryBindUserList.do";
    //拍照图片路径
    public final static String CAMER_IMAGE_PATH="CAMER_IMAGE_PATH";
    //注册
    public final static String REGISTER_URL="android/user/registUser.do";
    //下一步
    public final static String STEP_ONE="android/user/complementInfoStepOne.do";
    //第二步
    public final static String STEP_TWO="android/user/complementInfoStepTwo.do";
    //扫码添加成员
    public final static String SCAN_ADD="android/power/person/addPowerPerson.do";
    //修改绑定手机
    public final static String MODIFYMOBILE="android/user/modifyMobile.do";
    //判断手机号是否已存在信息员或者群众已经使用
    public final static String CHECK_EXIST="android/login/queryUser.do";
    //获取发布信息员类型和标签
    public final static String QUERY_USER="android/user/queryUserLabelAndTrade.do";
    //获取绑定信息员列表
    public final static String BIND_USER="android/user/queryBindUserList.do";
    public final static String REFRESH_REPORTER="";

    public final static String SET_MEMBER_MANAGER="android/power/person/addPowerManager.do";
    public final static String CANCEL_MANAGER_POWER="android/power/person/delPowerManager.do";
    public final static String DELETE_MEMBER_MANAGER="android/power/person/delPowerPerson.do";

    public final static String POWER_PUBLISH="android/form/queryFormFieldInfo.do?strTypeGuid=powerMessage&strApp=hexi&";
    public final static String MY_POSITION="android/userLocation/saveUserLocation.do";
    public final static String CHANGE_POWERUNITY="android/power/unity/updatePowerUnity.do";
    public final static String POLICE_XXY="android/power/unity/updatePowerUnity.do";
}
