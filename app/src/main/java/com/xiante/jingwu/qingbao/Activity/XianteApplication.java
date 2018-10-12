package com.xiante.jingwu.qingbao.Activity;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.mob.MobSDK;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by zhong on 2018/5/18.
 */

public class XianteApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MobSDK.init(this, "25d7e6655c536", "bb09590f513f32d466c49685b15363c3");
        CrashReport.initCrashReport(getApplicationContext(), "7e70677ca8", false);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
