package com.xiante.jingwu.qingbao.Manager;

import android.app.Activity;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhong on 2018/5/19.
 */

public class ActivityManager {

    private static ActivityManager manager;
    private List<Activity> activityList;

    private ActivityManager(){
        activityList=new ArrayList<>();
    }

    public  static ActivityManager getInstance(){
        if(manager==null){
            synchronized (ActivityManager.class){
                if(manager==null){
                    manager=new ActivityManager();
                }
            }
        }
        return manager;
    }

    public void addActivity(Activity activity){
        activityList.add(activity);
    }

    public void finishAll(){
        String root= Environment.getExternalStorageDirectory().getAbsolutePath();
        deletemediaFile(root+"/xiantevideo/");
        deletemediaFile(root+"/DCIM/Camera/Xiante/");
        deletemediaFile(root+"/power/record/");
        deletemediaFile(root+"/TEMPLE_IMAGE/");
        deletemediaFile(root+"/app/");
        for (Activity a:activityList) {
            a.finish();
        }

    }

    public void finishOthers(Activity activity){
        for (Activity a:activityList) {
            if(a!=activity){
                a.finish();
            }
        }
    }


    public void  deletemediaFile(String path){
        File file=new File(path);
        if(file.exists()){
            File[] subfile=file.listFiles();
            if(subfile!=null){
                for (File f:subfile) {
                    f.delete();
                }
            }
        }

    }

}
